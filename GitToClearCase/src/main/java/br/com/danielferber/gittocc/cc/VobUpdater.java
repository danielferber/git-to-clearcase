///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package br.com.danielferber.gittocc.cc;
//
//import br.com.danielferber.gittocc.sync.GitHistory;
//import br.com.danielferber.gittocc.sync.GitHistoryBuilder;
//import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
//import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.List;
//import java.util.TreeSet;
//import java.util.concurrent.Callable;
//import org.apache.commons.io.FileUtils;
//
///**
// *
// * @author X7WS
// */
//public class VobUpdater implements Callable<Void> {
//
//    final GitHistory gitHistory;
//    final ClearToolCommander commander;
//    final File vodDir;
//    final File commitFile;
//    boolean createActivity = false;
//    String headline = null;
//    boolean deleteEmptyDirs = false;
//
//    public VobUpdater(GitHistory gitHistory, ClearToolCommander commander, File vodDir) {
//        this.gitHistory = gitHistory;
//        this.commander = commander;
//        this.vodDir = vodDir;
//        this.commitFile = new File(vodDir, "commit.txt");
//    }
//
//    public void setCreateActivity(boolean createActivity) {
//        this.createActivity = createActivity;
//    }
//
//    public void setHeadline(String headline) {
//        this.headline = headline;
//    }
//
//    public void setDeleteEmptyDirs(boolean deletEmptyDirs) {
//        this.deleteEmptyDirs = deletEmptyDirs;
//    }
//
//
//
//    public Void call() throws Exception {
//        final Collection<File> moveToParentDirs = parentDirs(gitHistory.getFilesMovedTo());
//        final Collection<File> moveFromParentDirs = parentDirs(gitHistory.getFilesMovedFrom());
//        final Collection<File> deleteParentDirs = parentDirs(gitHistory.getFilesDeleted());
//        TreeSet<File> rootDirsToDelete = new TreeSet<File>();
//        TreeSet<File> emptyDirParentDirs = new TreeSet<File>();
//        if (deleteEmptyDirs) {
//            rootDirsToDelete = calculateEmptyDirsToDelete(deleteParentDirs, moveFromParentDirs);
//            emptyDirParentDirs = new TreeSet<File>(parentDirs(rootDirsToDelete));
//        }
//
//
//        Meter m = MeterFactory.getMeter(GitHistoryBuilder.class).m("Atualizar VOB.").ctx("commit.from", gitHistory.getFromCommit()).ctx("commit.to", gitHistory.getToCommit()).start();
//        Meter m2 = null;
//        try {
//
//            if (createActivity && headline != null) {
//                m2 = m.sub("newActivity").m("Criar atividade.").ctx("headline", headline).start();
//                commander.createActivity(headline);
//                m.ok();
//            }
//
//            m2 = m.sub("checkoutCommitFile").m("Bloquear arquivo de controle.").start();
//            commander.checkout(commitFile);
//            m2.ok();
//
//            m2 = m.sub("makeElements").m("Criar novos arquivos e diretórios.")
//                    .ctx("#addFiles", gitHistory.getFilesAdded().size())
//                    .ctx("#copyToFiles", gitHistory.getFilesCopiedTo().size())
//                    .ctx("#moveToParentDirs", moveToParentDirs.size()).start();
//            commander.makeElements(null, gitHistory.getFilesAdded());
//            commander.makeElements(moveToParentDirs, null);
//            commander.makeElements(null, gitHistory.getFilesCopiedTo());
//            m2.ok();
//
//            m2 = m.sub("checkout").m("Fazer Checkout de arquivos e diretórios.")
//                    .ctx("#deleteParentDirs", deleteParentDirs.size())
//                    .ctx("#moveToParentDirs", moveToParentDirs.size())
//                    .ctx("#moveFromParentDirs", moveFromParentDirs.size())
//                    .ctx("#emptyDirParentDirs", emptyDirParentDirs.size()).start();
//            commander.checkout(deleteParentDirs);
//            commander.checkout(moveToParentDirs);
//            commander.checkout(moveFromParentDirs);
//            if (deleteEmptyDirs) {
//                commander.checkout(emptyDirParentDirs);
//            }
//            m2.ok();
//
//            m2 = m.sub("move").m("Mover arquivos arquivos.").ctx("#moveToFiles", gitHistory.getFilesMovedFrom().size()).start();
//            Iterator<File> sourceIterator = gitHistory.getFilesMovedFrom().iterator();
//            Iterator<File> targetIterator = gitHistory.getFilesMovedTo().iterator();
//            while (sourceIterator.hasNext()) {
//                File source = sourceIterator.next();
//                File target = targetIterator.next();
//                commander.moveFile(source, target);
//            }
//            m2.ok();
//
//            m2 = m.sub("move").m("Apagar arquivos.").ctx("#deleteFiles", gitHistory.getFilesDeleted().size()).start();
//            commander.remove(gitHistory.getFilesDeleted());
//            m2.ok();
//
//            m2 = m.sub("move").m("Atualizar arquivos.").ctx("#deleteFiles", gitHistory.getFilesDeleted().size()).start();
//            commander.checkout(gitHistory.getFilesModified());
//            copyFilesFromGit(gitHistory.getFilesAdded());
//            copyFilesFromGit(gitHistory.getFilesModified());
//            copyFilesFromGit(gitHistory.getFilesCopiedTo());
//            m2.ok();
//
//            m2 = m.sub("deleteDirs").m("Apagar diretórios vazios.").ctx("#rootDirsToDelete", rootDirsToDelete.size()).start();
//            if (deleteEmptyDirs) {
//                commander.remove(rootDirsToDelete);
//            }
//            m2.ok();
//
//            FileWriter writer = new FileWriter(commitFile);
//            writer.write(gitHistory.getToCommit() + "\n");
//            writer.close();
//
//            m2 = m.sub("checkin").m("Fazer Checkin de arquivos e diretórios.").start();
//            commander.checkinAll();
//            m2.ok();
//        } catch (Exception e) {
//            if (m2 != null) {
//                m2.fail(e);
//            }
//            m.fail(e);
//            throw e;
//        }
//
//        return null;
//    }
//
//    private void copyFilesFromGit(final List<File> files) throws IOException {
//        for (File file : files) {
//            File gitSourceFile = new File(gitHistory.getRepositoryDir(), file.getPath());
//            File ccTargetFile = new File(vodDir, file.getPath());
//            FileUtils.copyFile(gitSourceFile, ccTargetFile);
//        }
//    }
//
//    private TreeSet<File> calculateEmptyDirsToDelete(final Collection<File> deleteFileParentDirs, final Collection<File> moveFromParentDirs) {
//        TreeSet<File> leafDirsToDelete = new TreeSet<File>();
//        TreeSet<File> candidateDirs = new TreeSet(deleteFileParentDirs);
//        candidateDirs.addAll(moveFromParentDirs);
//        for (File dir : candidateDirs) {
//            final File root = gitHistory.getRepositoryDir();
//
//            for (File candidateDir : candidateDirs) {
//                /* Only queue one directory for deletion if it is not the
//                 * root of the repository itself and if it does not exist
//                 * anymore in the repository. */
//                File gitCandidateDir = new File(root, candidateDir.getPath());
//
//                while (!root.equals(gitCandidateDir) && !gitCandidateDir.exists()) {
//                    leafDirsToDelete.add(candidateDir);
//                    gitCandidateDir = gitCandidateDir.getParentFile();
//                }
//            }
//        }
//        TreeSet<File> rootDirsToDelete = new TreeSet<File>();
//        File previousDir = null;
//        for (File dir : leafDirsToDelete) {
//            if (previousDir == null || !dir.getPath().startsWith(previousDir.getPath())) {
//                rootDirsToDelete.add(dir);
//            }
//        }
//        return rootDirsToDelete;
//    }
//}
