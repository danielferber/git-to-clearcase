package br.com.danielferber.gittocc2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.text.StrSubstitutor;

import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigSource;
import br.com.danielferber.gittocc2.config.git.GitConfigSource;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;

/**
 *
 * @author daniel
 */
public class ClearCaseChangeTask implements Callable<Void> {

    private final ClearToolConfigSource cleartoolConfig;
    private final GitConfigSource gitConfig;
    private final ClearToolCommander ctCommander;
    private final TreeDiff gitTreeDiff;
    private final long syncCounter;
    private final String syncToCommit;
    private final Meter globalMeter;

    public ClearCaseChangeTask(final ClearToolConfigSource environmentConfig, final GitConfigSource gitConfig, final ClearToolCommander ctCommander,
            final TreeDiff gitTreeDiff, final String syncToCommit, final long syncCounter, final Meter outerMeter) {
        this.cleartoolConfig = environmentConfig;
        this.gitConfig = gitConfig;
        this.ctCommander = ctCommander;
        this.gitTreeDiff = gitTreeDiff;
        this.syncCounter = syncCounter;
        this.globalMeter = outerMeter.sub("UpdateVob");
        this.syncToCommit = syncToCommit;
    }

    @Override
    public Void call() throws Exception {
        globalMeter.start();
        try {
            /* ClearCase update task. */
            checkoutCommitStampFile();
            checkoutCounterStampFile();

            applyDiff(gitTreeDiff, syncCounter);

            writeCommitStampFile(syncToCommit);
            writeSyncCounter(syncCounter);

            chkeckinAllChanges();

            globalMeter.ok();
        } catch (final Exception e) {
            globalMeter.fail(e);
            throw e;
        }
        return null;
    }

    private void chkeckinAllChanges() {
        if (ctCommander.checkinDirsRequired()) {
            final Meter m = globalMeter.sub("checkin.dirs").m("Checkin all directories.").iterations(ctCommander.checkinDirsCount()).start();
            ctCommander.checkinDirs();
            m.ok();
        }
        if (ctCommander.checkinFilesRequired()) {
            final Meter m = globalMeter.sub("checkin.files").m("Checkin all files.").iterations(ctCommander.checkinFilesCount()).start();
            ctCommander.checkinFiles();
            m.ok();
        }
    }

    private void writeCommitStampFile(final String commit) throws SyncTaskException {
        final File commitStampFile = cleartoolConfig.getCommitStampAbsoluteFile();
        final Meter m = globalMeter.sub("write.commitFile").m("Write sync commit control file.").ctx("file", commitStampFile).start();
        try (FileWriter writer = new FileWriter(commitStampFile)) {
            writer.write(commit + "\n");
            m.ok();
        } catch (final IOException ex) {
            m.fail(ex);
            throw new SyncTaskException("Commit stamp file not writeable.", ex);
        }
    }

    private void checkoutCommitStampFile() throws SyncTaskException {
        final File commitStampFile = cleartoolConfig.getCommitStampAbsoluteFile();
        final Meter m = globalMeter.sub("checkout.commitFile").m("Checkout  sync commit control file.").ctx("file", commitStampFile).start();
        ctCommander.checkoutFile(commitStampFile);
        m.ok();
    }

    private void writeSyncCounter(final long counter) throws SyncTaskException {
        final File counterStampFile = cleartoolConfig.getCounterStampAbsoluteFile();
        final Meter m = globalMeter.sub("write.commitFile").m("Write sync counter control file.").ctx("file", counterStampFile).start();
        try (FileWriter writer = new FileWriter(counterStampFile)) {
            writer.write(Long.toString(counter) + "\n");
            m.ok();
        } catch (final IOException ex) {
            m.fail(ex);
            throw new SyncTaskException("Counter stamp file not writeable.", ex);
        }
    }

    private void checkoutCounterStampFile() throws SyncTaskException {
        final File counterStampFile = cleartoolConfig.getCounterStampAbsoluteFile();
        final Meter m = globalMeter.sub("checkout.counterFile").m("Checkout sync counter control file.").ctx("file", counterStampFile).start();
        ctCommander.checkoutFile(counterStampFile);
        m.ok();
    }

    private void applyDiff(final TreeDiff diff, final long syncCounter) throws Exception {
        final Meter m = MeterFactory.getMeter("VobUpdate").m("Atualizar VOB.").start();
        Meter m2 = null;

        try {
            if (cleartoolConfig.getUseSyncActivity()) {
                final HashMap<String, Object> map = new HashMap<>();
                map.put("commit", syncToCommit);
                map.put("date", new Date());
                map.put("count", syncCounter);
                final StrSubstitutor sub = new StrSubstitutor(map);
                final String resolvedString = sub.replace(cleartoolConfig.getSyncActivityName());
                m2 = m.sub("createActivity").m("Criar atividade.").ctx("headline", resolvedString).start();
                ctCommander.createActivity(resolvedString);
                m2.ok();
            }

            if (!diff.dirsAdded.isEmpty()) {

                m2 = m.sub("checkout.roots.dirsAdded").m("Checkout de raizes com diretórios novos.").start();
                final Collection<File> dirs = parentDirs(roots(diff.dirsAdded));
                ctCommander.checkoutDirs(dirs);
                m2.ok();

                m2 = m.sub("make.dirsAdded").m("Criar novos diretórios.").start();
                ctCommander.makeDirs(diff.dirsAdded);
                m2.ok();
            }

            if (!diff.filesAdded.isEmpty()) {
                m2 = m.sub("checkout.parent.filesAdded").m("Checkout de diretórios com arquivos novos.").start();
                ctCommander.checkoutDirs(parentDirs(diff.filesAdded));
                m2.ok();

                m2 = m.sub("make.filesAdded").m("Criar arquivos novos.").start();
                ctCommander.makeFiles(diff.filesAdded);
                m2.ok();

                m2 = m.sub("write.filesAdded").m("Escrever arquivos novos.").iterations(diff.filesAdded.size()).start();
                copyFilesFromGit(diff.filesAdded);
                m2.ok();
            }

            if (!diff.filesCopiedTo.isEmpty()) {
                m2 = m.sub("checkout.parent.filesCopiedTo").m("Checkout de diretórios com arquivos copiados.").start();
                ctCommander.checkoutDirs(parentDirs(diff.filesCopiedTo));
                m2.ok();

                m2 = m.sub("make.filesCopiedTo").m("Criar arquivos copiados.").start();
                ctCommander.makeFiles(diff.filesCopiedTo);
                m2.ok();

                if (!diff.filesCopiedTo.isEmpty()) {
                    m2 = m.sub("write.filesCopiedTo").m("Escrever arquivos copiados.").iterations(diff.filesCopiedTo.size()).start();
                    copyFilesFromGit(diff.filesCopiedTo);
                    m2.ok();
                }
            }

            if (!diff.filesMovedFrom.isEmpty()) {
                m2 = m.sub("checkout.parent.filesMovedFrom").m("Checkout de diretórios origem com arquivos movidos.").start();
                ctCommander.checkoutDirs(parentDirs(diff.filesMovedFrom));
                m2.ok();

                m2 = m.sub("checkout.parent.filesMovedTo").m("Checkout de diretórios destino com arquivos movidos.").start();
                ctCommander.checkoutDirs(parentDirs(diff.filesMovedTo));
                m2.ok();

                m2 = m.sub("move.filesMoved").m("Mover arquivos.").iterations(diff.filesMovedTo.size()).start();
                final Iterator<File> sourceIterator = diff.filesMovedFrom.iterator();
                final Iterator<File> targetIterator = diff.filesMovedTo.iterator();
                while (sourceIterator.hasNext()) {
                    final File source = sourceIterator.next();
                    final File target = targetIterator.next();
                    ctCommander.moveFile(source, target);
                }

                if (!diff.filesMovedModified.isEmpty()) {
                    m2 = m.sub("checkout.filesMovedModified").m("Checkout de arquivos movidos e modificados.").start();
                    ctCommander.checkoutFiles(diff.filesMovedModified);
                    m2.ok();

                    m2 = m.sub("write.filesMovedModified").m("Escrever arquivos movidos e modificados.").iterations(diff.filesMovedModified.size()).start();
                    copyFilesFromGit(diff.filesMovedModified);
                    m2.ok();
                }
            }

            if (!diff.filesModified.isEmpty()) {
                m2 = m.sub("checkout.filesModified").m("Checkout de arquivos modificados.").start();
                ctCommander.checkoutFiles(diff.filesModified);
                m2.ok();

                m2 = m.sub("write.filesModified").m("Modificar arquivos.").start();
                copyFilesFromGit(diff.filesModified);
                m2.ok();
            }

            if (!diff.dirsDeleted.isEmpty()) {
                final Collection<File> dirsToDelete = roots(diff.dirsDeleted);

                if (!dirsToDelete.isEmpty()) {
                    m2 = m.sub("checkout.roots.dirsDeleted").m("Checkout de raizes de diretórios removidos.").start();
                    ctCommander.checkoutDirs(parentDirs(dirsToDelete));
                    m2.ok();

                    m2 = m.sub("delete.dirs").m("Apagar diretórios.").start();
                    ctCommander.removeDirs(dirsToDelete);
                    m2.ok();
                }
            }

            if (!diff.filesDeleted.isEmpty()) {
                final TreeSet<File> filesToDelete = leafes(diff.dirsDeleted, diff.filesDeleted);

                if (!filesToDelete.isEmpty()) {
                    m2 = m.sub("delete.parent.filesDeleted").m("Checkout de diretórios com arquivos removidos.").start();
                    ctCommander.checkoutDirs(parentDirs(filesToDelete));
                    m2.ok();

                    m2 = m.sub("delete.files").m("Apagar arquivos.").start();
                    ctCommander.removeFiles(filesToDelete);
                    m2.ok();
                }
            }

        } catch (final Exception e) {
            if (m2 != null) {
                m2.fail(e);
            }
            throw e;
        }
    }

    private static Collection<File> roots(final List<File> files) {
        final TreeSet<File> roots = new TreeSet<>();

        for (final File file : files) {
            File parent = file.getParentFile();
            if (parent == null) {
                parent = new File(".");
            }
            if (!files.contains(parent)) {
                roots.add(file);
            }
        }

        return roots;
    }

    private static TreeSet<File> leafes(final List<File> dirs, final List<File> files) {
        final TreeSet<File> leafes = new TreeSet<>();

        for (final File file : files) {
            File parentDir = file.getParentFile();
            if (parentDir == null) {
                parentDir = new File(".");
            }
            if (!dirs.contains(parentDir)) {
                leafes.add(file);
            }
        }
        return leafes;
    }

    private static Collection<File> parentDirs(final Collection<File> files) {
        final TreeSet<File> dirs = new TreeSet<>();
        for (final File file : files) {
            File parentFile = file.getParentFile();
            if (parentFile == null) {
                parentFile = new File(".");
            }
            dirs.add(parentFile);
        }
        return dirs;
    }

    private void copyFilesFromGit(final List<File> files) {
        for (final File file : files) {
            final File gitSourceFile = new File(gitConfig.getRepositoryDir(), file.getPath());
            final File ccTargetFile = new File(cleartoolConfig.getVobViewDir(), file.getPath());
            try {
                Files.copy(gitSourceFile.toPath(), ccTargetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (final IOException e) {
                globalMeter.getLogger().error("Failed to copy file.", e);
            }
        }
    }

}
