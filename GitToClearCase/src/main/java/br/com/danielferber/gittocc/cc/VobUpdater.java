/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.cc;

import br.com.danielferber.gittocc.git.GitHistory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author X7WS
 */
public class VobUpdater implements Callable<Void> {

    final GitHistory gitHistory;
    final ClearToolCommander commander;
    final File vodDir;
    final File commitFile;
    boolean createActivity = false;
    String headline = null;
    boolean deleteEmptyDirs = false;

    public VobUpdater(GitHistory gitHistory, ClearToolProcessBuilder pb, File vodDir) {
        this.gitHistory = gitHistory;
        this.commander = new ClearToolCommander(pb);
        this.vodDir = vodDir;
        this.commitFile = new File(vodDir, "commit.txt");
        this.createActivity = createActivity;
    }

    public void setCreateActivity(boolean createActivity) {
        this.createActivity = createActivity;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public void setDeleteEmptyDirs(boolean deletEmptyDirs) {
        this.deleteEmptyDirs = deletEmptyDirs;
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

    public Void call() throws Exception {
        final Collection<File> moveToParentDirs = parentDirs(gitHistory.getFilesMovedTo());
        final Collection<File> moveFromParentDirs = parentDirs(gitHistory.getFilesMovedFrom());
        final Collection<File> deleteFileParentDirs = parentDirs(gitHistory.getFilesDeleted());

        if (createActivity && headline != null) {
            commander.createActivity(headline);
        }

        commander.checkout(commitFile);

        commander.makeElements(null, gitHistory.getFilesAdded());
        commander.makeElements(moveToParentDirs, null);
        commander.makeElements(null, gitHistory.getFilesCopiedTo());

        commander.checkout(deleteFileParentDirs);
        commander.checkout(moveToParentDirs);
        commander.checkout(moveFromParentDirs);

        Iterator<File> sourceIterator = gitHistory.getFilesMovedFrom().iterator();
        Iterator<File> targetIterator = gitHistory.getFilesMovedTo().iterator();
        while (sourceIterator.hasNext()) {
            File source = sourceIterator.next();
            File target = targetIterator.next();
            commander.moveFile(source, target);
        }

        commander.remove(gitHistory.getFilesDeleted());

        commander.checkout(gitHistory.getFilesModified());
        copyFilesFromGit(gitHistory.getFilesAdded());
        copyFilesFromGit(gitHistory.getFilesModified());
        copyFilesFromGit(gitHistory.getFilesCopiedTo());

        if (deleteEmptyDirs) {
//            TreeSet<File> dirsToDelete = new TreeSet<File>();
//            TreeSet<File> candidateDirs = new TreeSet(deleteFileParentDirs);
//            candidateDirs.addAll(moveFromParentDirs);
//
//            while (!candidateDirs.isEmpty()) {
//                File dir = candidateDirs.pollLast();
//                if (!dir.exists()) {
//                    dirsToDelete.add(dir);
//                    File parentDir = dir.getParentFile();
//                    if (parentDir != null) {
//                        candidateDirs.add(parentDir);
//                    }
//                } else {
//                    String[] children = dir.list();
//                    if (children.length == 0) {
//                        dirsToDelete.add(dir);
//                        File parentDir = dir.getParentFile();
//                        if (parentDir != null) {
//                            candidateDirs.add(parentDir);
//                        }
//                    }
//                }
//            }
//            
//            
//            commander.remove(dirsToDelete);
        }

        FileWriter writer = new FileWriter(commitFile);
        writer.write(gitHistory.getToCommit() + "\n");
        writer.close();

        commander.checkinAll();
        return null;
    }

    private void copyFilesFromGit(final List<File> files) throws IOException {
        for (File file : files) {
            File gitSourceFile = new File(gitHistory.getRepositoryDir(), file.getPath());
            File ccTargetFile = new File(vodDir, file.getPath());
            FileUtils.copyFile(gitSourceFile, ccTargetFile);
        }
    }
}
