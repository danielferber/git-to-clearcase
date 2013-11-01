/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.sync;

import br.com.danielferber.gittocc.cc.ClearToolCommander;
import br.com.danielferber.gittocc.git.GitCommander;
import br.com.danielferber.gittocc.git.GitTreeDiff;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author X7WS
 */
public class SyncTask implements Callable<Void> {

    final GitCommander gitCommander;
    final ClearToolCommander ctCommander;
    final File vobDir;
    final File gitDir;
    boolean fetchFromRemote;
    boolean fastForward;
    boolean createActivity;
    String headline = null;
    long sessionCounter;
    File commitFile = new File("atualizacao-hash.txt");

    public SyncTask(GitCommander gitCommander, ClearToolCommander ctCommander, File gitDir, File vobDir) {
        this.gitCommander = gitCommander;
        this.ctCommander = ctCommander;
        this.gitDir = gitDir;
        this.vobDir = vobDir;
    }

    public void setFetchFromRemote(boolean fetchFromRemote) {
        this.fetchFromRemote = fetchFromRemote;
    }

    public void setFastForward(boolean fastForward) {
        this.fastForward = fastForward;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public void setCommitFile(File commitFile) {
        this.commitFile = commitFile;
    }

    public void setCreateActivity(boolean createActivity) {
        this.createActivity = createActivity;
    }

    public void setSessionCounter(long sessionCounter) {
        this.sessionCounter = sessionCounter;
    }

    public Void call() throws Exception {

        Meter m = MeterFactory.getMeter("SyncTask").m("Sincronizar do Git para o ClearCase.").start();
        try {
            String fromCommit = readCommitHash();

            GitTreeDiff diff = buildGitDiff(fromCommit);
            
            if (diff.hasStuff()) {
                applyDiff(diff);
            }

            m.ok();

            return null;
        } catch (Exception e) {
            m.fail(e);
            throw e;
        }
    }

    private GitTreeDiff buildGitDiff(String fromCommit) throws Exception {
        Meter m = MeterFactory.getMeter("GitDiff").m("Criar histórico Git.").ctx("fromCommit", fromCommit).start();
        Meter m2 = null;

        try {

            if (fetchFromRemote) {
                m2 = m.sub("fetch").m("Obter commits remotos.").start();
                gitCommander.fetch();
                m2.ok();
            }

            if (fastForward) {
                m2 = m.sub("fastForward").m("Avançar commits.").start();
                gitCommander.fastForward();
                m2.ok();
            }

            m2 = m.sub("currentCommit").m("Ler commit atual.").start();
            String gitCommit = gitCommander.getCurrentCommit();
            m2.ok("currentCommit", gitCommit);

            m2 = m.sub("report").m("Gerar relatório de commits.").ctx("fromCommit", fromCommit).ctx("toCommit", gitCommit).start();
            String report = gitCommander.commitMessagesReport(fromCommit, gitCommit, "%an (%ad):%n%s%n");
            m2.ok();

            m2 = m.sub("changeSet").m("Listar modificações.").ctx("fromCommit", fromCommit).ctx("toCommit", gitCommit).start();
            GitTreeDiff diff = gitCommander.treeDif(fromCommit, gitCommit);
            m2.ok();

            return diff;
        } catch (Exception e) {
            if (m2 != null) {
                m2.fail(e);
            }
            throw e;
        }
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
            File gitSourceFile = new File(gitDir, file.getPath());
            File ccTargetFile = new File(vobDir, file.getPath());
            FileUtils.copyFile(gitSourceFile, ccTargetFile);
        }
    }

    private void applyDiff(GitTreeDiff diff) throws Exception {
        Meter m = MeterFactory.getMeter("VobUpdate").m("Atualizar VOB.").start();
        Meter m2 = null;

        try {
            if (createActivity) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("gitCommitTo", diff.toCommit);
                map.put("gitCommitFrom", diff.fromCommit);
                map.put("sessionDate", new Date());
                map.put("sessionCounter", sessionCounter);

                m2 = m.sub("createActivity").m("Criar atividade.").ctx("headline", headline).start();
                ctCommander.createActivity(headline);
                m.ok();
            }

            m2 = m.sub("checkout.commitFile").m("Bloquear arquivo de controle.").start();
            ctCommander.checkoutFile(commitFile);
            m2.ok();

            if (!diff.dirsAdded.isEmpty()) {
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

                m2 = m.sub("write.filesAdded").m("Escrever arquivos novos.").start();
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

                m2 = m.sub("write.filesCopiedTo").m("Escrever arquivos copiados.").start();
                copyFilesFromGit(diff.filesCopiedTo);
                m2.ok();
            }

            if (!diff.filesMovedFrom.isEmpty()) {
                m2 = m.sub("checkout.parent.filesMovedFrom").m("Checkout de diretórios origem com arquivos movidos.").start();
                ctCommander.checkoutDirs(parentDirs(diff.filesMovedFrom));
                m2.ok();

                m2 = m.sub("checkout.parent.filesMovedTo").m("Checkout de diretórios destino com arquivos movidos.").start();
                ctCommander.checkoutDirs(parentDirs(diff.filesMovedTo));
                m2.ok();

                m2 = m.sub("move.filesMoved").m("Mover arquivos.").start();
                Iterator<File> sourceIterator = diff.filesMovedFrom.iterator();
                Iterator<File> targetIterator = diff.filesMovedTo.iterator();
                while (sourceIterator.hasNext()) {
                    File source = sourceIterator.next();
                    File target = targetIterator.next();
                    ctCommander.moveFile(source, target);
                }

                if (! diff.filesMovedModified.isEmpty()) {
                    m2 = m.sub("checkout.filesMovedModified").m("Checkout de arquivos movidos e modificados.").start();
                    ctCommander.checkoutFiles(parentDirs(diff.filesMovedModified));
                    m2.ok();

                    m2 = m.sub("write.filesMovedModified").m("Escrever arquivos movidos e modificados.").start();
                    copyFilesFromGit(diff.filesMovedModified);
                    m2.ok();
                }
            }

            if (!diff.filesModified.isEmpty()) {
                m2 = m.sub("checkout.filesModified").m("Checkout de arquivos modificados.").start();
                ctCommander.checkoutFiles(parentDirs(diff.filesModified));
                m2.ok();

                m2 = m.sub("write.filesModified").m("Modificar arquivos.").start();
                copyFilesFromGit(diff.filesModified);
                m2.ok();
            }

            if (!diff.filesDeleted.isEmpty()) {
                m2 = m.sub("delete.files").m("Apagar arquivos.").start();
                ctCommander.removeFiles(diff.filesDeleted);
                m2.ok();
            }

            if (!diff.dirsDeleted.isEmpty()) {
                m2 = m.sub("delete.dirs").m("Apagar diretórios.").start();
                ctCommander.removeFiles(diff.dirsDeleted);
                m2.ok();
            }

            m2 = m.sub("write.commitFile").m("Atualizar arquivo de controle.").start();
            writeCommitHash(diff.toCommit);
            m2.ok();

            m2 = m.sub("checkin.dirs").m("Checkin de diretórios.").start();
            ctCommander.checkinDirs();
            m2.ok();

            m2 = m.sub("checkin.files").m("Checkin de arquivos.").start();
            ctCommander.checkinFiles();
            m2.ok();

        } catch (Exception e) {
            if (m2 != null) {
                m2.fail(e);
            }
            throw e;
        }
    }

    private String readCommitHash() throws FileNotFoundException {
        Scanner scanner = null;
        try {
            scanner = new Scanner(this.commitFile);
            return scanner.next();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private void writeCommitHash(String commit) throws IOException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(this.commitFile);
            writer.write(commit + "\n");
        } finally {
            if (writer != null) {
                IOUtils.closeQuietly(writer);
            }
        }
    }
}
