package br.com.danielferber.gittocc2;

import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author daniel
 */
class CompareTreeDiffTask implements Callable<TreeDiff> {

    private final File gitRootDir;
    private final File clearCaseRootDir;
    private final Meter globalMeter;

    CompareTreeDiffTask(File gitRootDir, File clearCaseRootDir, Meter outerMeter) {
        this.gitRootDir = gitRootDir;
        this.clearCaseRootDir = clearCaseRootDir;
        this.globalMeter = outerMeter.sub("CompareDiff").m("Compare GIT repository with ClearCase vob directory.");
    }

    @Override
    public TreeDiff call() throws Exception {
        globalMeter.start();

        Meter m2 = null;
        final Set<File> vobDirs = new TreeSet<File>();
        final Set<File> vobFiles = new TreeSet<File>();
        final Set<File> repositoryDirs = new TreeSet<File>();
        final Set<File> repositoryFiles = new TreeSet<File>();

        try {

            FileVisitor vobVisitor = new SimpleFileVisitor<Path>() {
                Path rootPath = clearCaseRootDir.toPath();

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path path = rootPath.relativize(file);
                    vobFiles.add(path.toFile());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path path = rootPath.relativize(dir);
                    vobDirs.add(path.toFile());
                    return FileVisitResult.CONTINUE;
                }
            };

            FileVisitor repositoryVisitor = new SimpleFileVisitor<Path>() {
                Path rootPath = gitRootDir.toPath();

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path path = rootPath.relativize(file);
                    repositoryFiles.add(path.toFile());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path path = rootPath.relativize(dir);
                    repositoryDirs.add(path.toFile());
                    return FileVisitResult.CONTINUE;
                }
            };

            m2 = globalMeter.sub("vobScan").m("Execute ClearCase VOB scan.").start();
            Files.walkFileTree(clearCaseRootDir.toPath(), vobVisitor);
            m2.ok();

            m2 = globalMeter.sub("gitScan").m("Execute GIT repository scan.").start();
            Files.walkFileTree(gitRootDir.toPath(), repositoryVisitor);
            m2.ok();

        } catch (Exception e) {
            globalMeter.fail(e);
            throw e;
        }

        Set<File> dirsAdded = new TreeSet<File>(repositoryDirs);
        dirsAdded.removeAll(vobDirs);
        Set<File> dirsDeleted = new TreeSet<File>(vobDirs);
        dirsDeleted.removeAll(repositoryDirs);
        Set<File> filesAdded = new TreeSet<File>(repositoryFiles);
        filesAdded.removeAll(vobFiles);
        Set<File> filesDeleted = new TreeSet<File>(vobFiles);
        filesDeleted.removeAll(repositoryFiles);

        Set<File> filesToCompare = new TreeSet<File>(vobFiles);
        vobFiles.retainAll(repositoryFiles);
        Set<File> filesModified = new TreeSet<File>();
        for (File file : filesToCompare) {
            File gitSourceFile = new File(gitRootDir, file.getPath());
            File ccTargetFile = new File(clearCaseRootDir, file.getPath());
            if (gitSourceFile.length() != ccTargetFile.length()) {
                filesModified.add(file);
                continue;
            }
            try (
                InputStream i1 = new FileInputStream(gitSourceFile) ;
                InputStream i2 = new FileInputStream(ccTargetFile) ) {
                if (!compare(i1, i2)) {
                    filesModified.add(file);
                    continue;
                }
            } catch (IOException e) {
                globalMeter.getLogger().error("Failed to copy file.", e);
            }

        }
        Set<File> filesMovedFrom = Collections.emptySet();
        Set<File> filesMovedTo = Collections.emptySet();
        Set<File> filesMovedModified = Collections.emptySet();
        Set<File> filesCopiedFrom = Collections.emptySet();
        Set<File> filesCopiedTo = Collections.emptySet();
        Set<File> filesCopiedModified = Collections.emptySet();

        return new TreeDiff(
                new ArrayList<File>(dirsAdded),
                new ArrayList<File>(dirsDeleted),
                new ArrayList<File>(filesAdded),
                new ArrayList<File>(filesDeleted),
                new ArrayList<File>(filesModified),
                new ArrayList<File>(filesMovedFrom),
                new ArrayList<File>(filesMovedTo),
                new ArrayList<File>(filesMovedModified),
                new ArrayList<File>(filesCopiedFrom),
                new ArrayList<File>(filesCopiedTo),
                new ArrayList<File>(filesCopiedModified));
    }

    public static void main(String[] argv) {
        try {
            new CompareTreeDiffTask(new File("/home/daniel/Git"), new File("/home/daniel/Vob"), MeterFactory.getMeter("teste")).call();
        } catch (Exception ex) {
            Logger.getLogger(CompareTreeDiffTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static boolean compare(InputStream input1, InputStream input2) throws IOException {
        boolean error = false;
        try {
            byte[] buffer1 = new byte[1024];
            byte[] buffer2 = new byte[1024];
            try {
                int numRead1 = 0;
                int numRead2 = 0;
                while (true) {
                    numRead1 = input1.read(buffer1);
                    numRead2 = input2.read(buffer2);
                    if (numRead1 > -1) {
                        if (numRead2 != numRead1) {
                            return false;
                        }
                        // Otherwise same number of bytes read
                        if (!Arrays.equals(buffer1, buffer2)) {
                            return false;
                        }
                        // Otherwise same bytes read, so continue ...
                    } else {
                        // Nothing more in stream 1 ...
                        return numRead2 < 0;
                    }
                }
            } finally {
                input1.close();
            }
        } catch (IOException e) {
            error = true; // this error should be thrown, even if there is an error closing stream 2
            throw e;
        } catch (RuntimeException e) {
            error = true; // this error should be thrown, even if there is an error closing stream 2
            throw e;
        } finally {
            try {
                input2.close();
            } catch (IOException e) {
                if (!error) {
                    throw e;
                }
            }
        }
    }
}
