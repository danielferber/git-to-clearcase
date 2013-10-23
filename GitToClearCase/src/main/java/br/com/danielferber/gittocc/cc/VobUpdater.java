/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.cc;

import br.com.danielferber.gittocc.git.GitHistory;
import java.io.File;
import java.io.IOException;
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

    public VobUpdater(GitHistory gitHistory, ClearToolProcessBuilder pb, File vodDir) {
        this.gitHistory = gitHistory;
        this.commander = new ClearToolCommander(pb);
        this.vodDir = vodDir;
        this.commitFile = new File(vodDir, "commit.txt");
    }

    private static Collection<File> parentDirs(Collection<File> files) {
        TreeSet<File> dirs = new TreeSet<File>();
        for (File file : files) {
            dirs.add(file.getParentFile());
        }
        return dirs;
    }

    public Void call() throws Exception {
        final Collection<File> moveToParentDirs = parentDirs(gitHistory.getFilesMovedTo());
        final Collection<File> moveFromParentDirs = parentDirs(gitHistory.getFilesMovedFrom());
        final Collection<File> deleteFileParentDirs = parentDirs(gitHistory.getFilesDeleted());

        String headline = String.format("%2$s: %1$te/%1$tm/%1$tY %1$tH:%1$tM:%1$tS%n", new Date(), gitHistory.getToCommit());
//        commander.createActivity(headline);

        commander.makeElements(null, gitHistory.getFilesAdded());
        commander.makeElements(moveToParentDirs, null);
        commander.makeElements(gitHistory.getFilesCopiedTo(), null);

        commander.checkout(commitFile);
        commander.checkout(gitHistory.getFilesModified());
        commander.checkout(moveToParentDirs);
        commander.checkout(moveFromParentDirs);
        commander.checkout(deleteFileParentDirs);

        copyFilesFromGit(gitHistory.getFilesAdded());
        copyFilesFromGit(gitHistory.getFilesModified());
        copyFilesFromGit(gitHistory.getFilesCopiedTo());

        Iterator<File> sourceIterator = gitHistory.getFilesMovedFrom().iterator();
        Iterator<File> targetIterator = gitHistory.getFilesMovedTo().iterator();
        while (sourceIterator.hasNext()) {
            File source = sourceIterator.next();
            File target = targetIterator.next();
            commander.moveFile(source, target);
        }
        
        commander.remove(gitHistory.getFilesDeleted());
        
        // write new commit to file
        
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
