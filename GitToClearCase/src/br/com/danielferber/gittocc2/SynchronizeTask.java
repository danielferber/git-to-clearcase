/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigSource;
import br.com.danielferber.gittocc2.config.git.GitConfigSource;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.text.StrSubstitutor;

/**
 *
 * @author Daniel Felix Ferber
 */
class SynchronizeTask implements Callable<Void> {

    private final GitConfigSource syncConfig;
    private final ClearToolConfigSource environmentConfig;
    private final GitCommander gitCommander;
    private final ClearToolCommander ctCommander;
    private final File commitStampFile;
    private final File counterStampFile;
    private final Meter globalMeter;

    SynchronizeTask(ClearToolConfigSource environmentConfig, GitConfigSource syncConfig) {
        this.gitCommander = new GitCommander(environmentConfig);
        this.ctCommander = new ClearToolCommander(environmentConfig);
        this.environmentConfig = environmentConfig;
        this.syncConfig = syncConfig;
        this.commitStampFile = new File(environmentConfig.getVobViewDir(), syncConfig.getCommitStampFile().getPath());
        this.counterStampFile = new File(environmentConfig.getVobViewDir(), syncConfig.getCounterStampFile().getPath());
        this.globalMeter = MeterFactory.getMeter("SyncTask").m("Sincronizar from Git to ClearCase.");
    }

    @Override
    public Void call() throws Exception {
        globalMeter.start();
        Meter m2;
        try {
            /* ClearCase Tasks */

            /* Git Tasks */
            if (syncConfig.getResetLocalGitRepository()) {
                resetLocalGitRepository();
            }
            if (syncConfig.getCleanLocalGitRepository()) {
                cleanLocalGitRepository();
            }
            if (syncConfig.getFetchRemoteGitRepository()) {
                fetchRemoteGitRepository();
            }
            if (syncConfig.getFastForwardLocalGitRepository()) {
                fastForwardLocalGitRepository();
            }

            /* Collect task data */
            long syncCounter;
            if (syncConfig.getOverriddenSyncCounter() == null) {
                syncCounter = readSyncCounter() + 1;
            } else {
                syncCounter = syncConfig.getOverriddenSyncCounter();
            }
            String syncFromCommit;
            if (syncConfig.getOverriddenSyncCounter() == null) {
                syncFromCommit = readCommitStampFile();
            } else {
                syncFromCommit = syncConfig.getOverriddenSyncFromCommit();
            }

            /* TreeDiff Task */
            GitTreeDiff diff = buildGitDiff(syncFromCommit);

            checkoutCommitStampFile();
            checkoutCounterStampFile();

            if (diff.hasStuff()) {
                applyDiff(diff, syncCounter);
            }

            writeCommitStampFile(diff.toCommit);
            writeSyncCounter(syncCounter);

            chkeckinAllChanges();

            globalMeter.ok();

            return null;
        } catch (Exception e) {
            globalMeter.fail(e);
            throw e;
        }

    }

    private void cleanLocalGitRepository() {
        Meter m = globalMeter.sub("clean").m("Clean local GIT repository.").start();
        gitCommander.cleanLocal();
        m.ok();
    }

    private void resetLocalGitRepository() {
        Meter m = globalMeter.sub("reset").m("Reset local GIT repository.").start();
        gitCommander.resetLocal();
        m.ok();
    }

    private void fastForwardLocalGitRepository() {
        Meter m = globalMeter.sub("fastForward").m("Fast forward commits on local GIT repository.").start();
        gitCommander.fastForward();
        m.ok();
    }

    private void fetchRemoteGitRepository() {
        Meter m = globalMeter.sub("fetch").m("Fetch new commits from remote GIT rempository.").start();
        gitCommander.fetchRemote();
        m.ok();
    }

    private void chkeckinAllChanges() {
        if (ctCommander.checkinDirsRequired()) {
            Meter m = globalMeter.sub("checkin.dirs").m("Checkin all directories.").iterations(ctCommander.checkinDirsCount()).start();
            ctCommander.checkinDirs();
            m.ok();
        }
        if (ctCommander.checkinFilesRequired()) {
            Meter m = globalMeter.sub("checkin.files").m("Checkin all files.").iterations(ctCommander.checkinFilesCount()).start();
            ctCommander.checkinFiles();
            m.ok();
        }
    }

    private void updateFullVob() {
        ctCommander.updateVobViewDir();
    }

    private String readCommitStampFile() throws SyncTaskException {
        Meter m = globalMeter.sub("read.commitFile").m("Read sync commit control file.").ctx("file", this.commitStampFile).start();
        try (Scanner scanner = new Scanner(this.commitStampFile)) {
            final String result = scanner.next();
            m.ctx("fromCommit", result).ok();
            return result;
        } catch (FileNotFoundException ex) {
            m.fail(ex);
            throw new SyncTaskException("Commit stamp file not readable.", ex);
        }
    }

    private void writeCommitStampFile(String commit) throws SyncTaskException {
        Meter m = globalMeter.sub("write.commitFile").m("Write sync commit control file.").ctx("file", this.commitStampFile).start();
        try (FileWriter writer = new FileWriter(this.commitStampFile)) {
            writer.write(commit + "\n");
            m.ok();
        } catch (IOException ex) {
            m.fail(ex);
            throw new SyncTaskException("Commit stamp file not writeable.", ex);
        }
    }

    private void updateCommitStampFile() throws SyncTaskException {
        Meter m = globalMeter.sub("update.commitFile").m("Update sync commit control file.").ctx("file", this.commitStampFile).start();
        ctCommander.updateFile(commitStampFile);
        m.ok();
    }

    private void checkoutCommitStampFile() throws SyncTaskException {
        Meter m = globalMeter.sub("checkout.commitFile").m("Checkout  sync commit control file.").ctx("file", this.commitStampFile).start();
        ctCommander.checkoutFile(commitStampFile);
        m.ok();
    }

    private long readSyncCounter() throws SyncTaskException {
        Meter m = globalMeter.sub("read.counterFile").m("Read sync counter control file.").ctx("file", this.commitStampFile).start();
        try (Scanner scanner = new Scanner(this.counterStampFile)) {
            final long result = scanner.nextLong();
            m.ctx("counter", result).ok();
            return result;
        } catch (FileNotFoundException ex) {
            m.fail(ex);
            throw new SyncTaskException("Counter stamp file not readable.", ex);
        }
    }

    private void writeSyncCounter(long counter) throws SyncTaskException {
        Meter m = globalMeter.sub("write.commitFile").m("Write sync counter control file.").ctx("file", this.commitStampFile).start();
        try (FileWriter writer = new FileWriter(this.counterStampFile)) {
            writer.write(Long.toString(counter) + "\n");
            m.ok();
        } catch (IOException ex) {
            m.fail(ex);
            throw new SyncTaskException("Counter stamp file not writeable.", ex);
        }
    }

    private void updateCounterStampFile() throws SyncTaskException {
        Meter m = globalMeter.sub("update.counterFile").m("Update sync counter control file.").ctx("file", this.counterStampFile).start();
        ctCommander.updateFile(counterStampFile);
        m.ok();
    }

    private void checkoutCounterStampFile() throws SyncTaskException {
        Meter m = globalMeter.sub("checkout.counterFile").m("Checkout sync counter control file.").ctx("file", this.counterStampFile).start();
        ctCommander.checkoutFile(counterStampFile);
        m.ok();
    }

    private GitTreeDiff buildGitDiff(String fromCommit) throws Exception {
        Meter m = MeterFactory.getMeter("GitDiff").m("Criar histórico Git.").ctx("fromCommit", fromCommit).start();
        Meter m2 = null;

        try {

            m2 = m.sub("currentCommit").m("Read current commit hash.").start();
            String gitCommit = gitCommander.currentCommit();
            m2.ctx("commit", gitCommit).ok();

            m2 = m.sub("report").m("Gerar relatório de commits.").ctx("fromCommit", fromCommit).ctx("toCommit", gitCommit).start();
            String report = gitCommander.commitMessagesReport(fromCommit, gitCommit, "%an (%ad):%n%s%n");
            m2.ok();

            m2 = m.sub("changeSet").m("Listar modificações.").ctx("fromCommit", fromCommit).ctx("toCommit", gitCommit).start();
            GitTreeDiff diff = gitCommander.treeDif(fromCommit, gitCommit);
            m2.ok();

            m.ok();

            return diff;
        } catch (Exception e) {
            m.fail(e);
            throw e;
        }
    }

    private void applyDiff(GitTreeDiff diff, long syncCounter) throws Exception {
        Meter m = MeterFactory.getMeter("VobUpdate").m("Atualizar VOB.").start();
        Meter m2 = null;

        try {
            if (syncConfig.getCreateActivity()) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("gitCommitTo", diff.toCommit);
                map.put("gitCommitFrom", diff.fromCommit);
                map.put("syncDate", new Date());
                map.put("syncCount", syncCounter);
                StrSubstitutor sub = new StrSubstitutor(map);
                String resolvedString = sub.replace(syncConfig.getActivityMessagePattern());
                m2 = m.sub("createActivity").m("Criar atividade.").ctx("headline", resolvedString).start();
                ctCommander.createActivity(resolvedString);
                m2.ok();
            }

            if (!diff.dirsAdded.isEmpty()) {

                m2 = m.sub("checkout.roots.dirsAdded").m("Checkout de raizes com diretórios novos.").start();
                Collection<File> dirs = parentDirs(roots(diff.dirsAdded));
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
                Iterator<File> sourceIterator = diff.filesMovedFrom.iterator();
                Iterator<File> targetIterator = diff.filesMovedTo.iterator();
                while (sourceIterator.hasNext()) {
                    File source = sourceIterator.next();
                    File target = targetIterator.next();
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
                TreeSet<File> filesToDelete = leafes(diff.dirsDeleted, diff.filesDeleted);

                if (!filesToDelete.isEmpty()) {
                    m2 = m.sub("delete.parent.filesDeleted").m("Checkout de diretórios com arquivos removidos.").start();
                    ctCommander.checkoutDirs(parentDirs(filesToDelete));
                    m2.ok();

                    m2 = m.sub("delete.files").m("Apagar arquivos.").start();
                    ctCommander.removeFiles(filesToDelete);
                    m2.ok();
                }
            }


        } catch (Exception e) {
            if (m2 != null) {
                m2.fail(e);
            }
            throw e;
        }
    }

    private static Collection<File> roots(List<File> files) {
        TreeSet<File> roots = new TreeSet<File>();

        for (File file : files) {
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

    private static TreeSet<File> leafes(List<File> dirs, List<File> files) {
        TreeSet<File> leafes = new TreeSet<File>();

        for (File file : files) {
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

    private static Collection<File> parentDirs(Collection<File> files) {
        TreeSet<File> dirs = new TreeSet<File>();
        for (File file : files) {
            File parentFile = file.getParentFile();
            if (parentFile == null) {
                parentFile = new File(".");
            }
            dirs.add(parentFile);
        }
        return dirs;
    }

    private void copyFilesFromGit(final List<File> files) throws IOException {
        for (File file : files) {
            File gitSourceFile = new File(environmentConfig.getRepositoryDir(), file.getPath());
            File ccTargetFile = new File(environmentConfig.getVobViewDir(), file.getPath());
            Files.copy(gitSourceFile.toPath(), ccTargetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
