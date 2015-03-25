/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author x7ws
 */
public class CompareTask implements Runnable {

    private final Context context;

    private final Path sourceRootDir;
    private final Path destRootDir;
    private final Path innerPath;

    public CompareTask(Context context, final Path sourceRootDir, final Path destRootDir, final Path innerPath) {
        this.context = context;
        this.sourceRootDir = sourceRootDir;
        this.destRootDir = destRootDir;
        this.innerPath = innerPath;
    }

    private static class Visitor extends SimpleFileVisitor<Path> implements Runnable {

        final Path outerDir;
        final Set<Path> relativeDirCollection;
        final Set<Path> relativeFileCollection;
        final Path innerDir;

        public Visitor(Path outerDir, Path innerDir, Set<Path> dirCollection, Set<Path> fileCollection) {
            this.outerDir = outerDir;
            this.relativeDirCollection = dirCollection;
            this.relativeFileCollection = fileCollection;
            this.innerDir = innerDir;
        }

        @Override
        public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
            final Path path = outerDir.relativize(file);
            relativeFileCollection.add(path);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
            final Path path = outerDir.relativize(dir);
            relativeDirCollection.add(path);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public void run() {
            try {
                Files.walkFileTree(innerDir, this);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    };

    @Override
    public void run() {
        Meter meter = Meter.getCurrentInstance();

        final Set<Path> relativeDestDirs = new TreeSet<>();
        final Set<Path> relativeDestFiles = new TreeSet<>();
        final Set<Path> relativeSourceDirs = new TreeSet<>();
        final Set<Path> relativeSourceFiles = new TreeSet<>();

        /* Scan both directories. */
        meter.sub("scan.vob").m("ClearCase VOB scan.").run(new Visitor(destRootDir, destRootDir.resolve(innerPath), relativeDestDirs, relativeDestFiles));
        meter.sub("scan.git").m("GIT repository scan.").run(new Visitor(sourceRootDir, sourceRootDir.resolve(innerPath), relativeSourceDirs, relativeSourceFiles));

        /* Calculate trivial changes. */
        final Set<Path> relativeDirsAdded = new TreeSet<>(relativeSourceDirs);
        relativeDirsAdded.removeAll(relativeDestDirs);
        final Set<Path> relativeDirsDeleted = new TreeSet<>(relativeDestDirs);
        relativeDirsDeleted.removeAll(relativeSourceDirs);
        final Set<Path> relativeFilesAdded = new TreeSet<>(relativeSourceFiles);
        relativeFilesAdded.removeAll(relativeDestFiles);
        final Set<Path> relativeFilesDeleted = new TreeSet<>(relativeDestFiles);
        relativeFilesDeleted.removeAll(relativeSourceFiles);

        /* Calculate change on compasiron. */
        final Set<Path> relativeFilesToCompare = new TreeSet<>(relativeDestFiles);
        relativeFilesToCompare.retainAll(relativeSourceFiles);
        final Set<Path> relativeFilesModified = new TreeSet<>();

        final Meter m2 = meter.sub("scan.compare").m("Compare file by file.").iterations(relativeFilesToCompare.size());
        m2.run(() -> {
            for (final Path relativeFile : relativeFilesToCompare) {
                final Path sourceFile = sourceRootDir.resolve(relativeFile);
                final Path destFile = destRootDir.resolve(relativeFile);
                try {
                    final long sourceSize = Files.size(sourceFile);
                    final long destSize = Files.size(destFile);
                    if (sourceSize != destSize) {
                        relativeFilesModified.add(relativeFile);
                        continue;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                m2.inc().progress();
            }
        });

        /* Comparison does not detect moved or copied files. */
        final List<Path> dirsAdded = new ArrayList<>(relativeDirsAdded);
        final List<Path> dirsDeleted = new ArrayList<>(relativeDirsDeleted);
        final List<Path> filesAdded = new ArrayList<>(relativeFilesAdded.size());
        final List<Path> filesAddedSource = new ArrayList<>(relativeFilesAdded.size());
        for (Path file : relativeFilesAdded) {
            filesAdded.add(file);
            filesAddedSource.add(sourceRootDir.resolve(file));
        }
        final List<Path> filesDeleted = new ArrayList<>(relativeFilesDeleted);
        final List<Path> filesModified = new ArrayList<>(relativeFilesModified.size());
        final List<Path> filesModifiedSource = new ArrayList<>(relativeFilesModified.size());
        for (Path file : relativeFilesModified) {
            filesModified.add(file);
            filesModifiedSource.add(sourceRootDir.resolve(file));
        }
        final List<Path> filesMovedFrom = Collections.emptyList();
        final List<Path> filesMovedTo = Collections.emptyList();
        final List<Path> filesMovedModified = Collections.emptyList();
        final List<Path> filesMovedSource = Collections.emptyList();
        final List<Path> filesCopiedFrom = Collections.emptyList();
        final List<Path> filesCopiedTo = Collections.emptyList();
        final List<Path> filesCopiedModified = Collections.emptyList();
        final List<Path> filesCopiedSource = Collections.emptyList();

        /* Report change set. */
        ChangeSet changeSet = ChangeSet.createFileChangeSet(
            context.getCurrentCommitStamp(),
            dirsAdded,
            dirsDeleted,
            filesAdded, filesAddedSource,
            filesDeleted,
            filesModified, filesModifiedSource,
            filesMovedFrom, filesMovedTo, filesMovedModified, filesMovedSource,
            filesCopiedFrom, filesCopiedTo, filesCopiedModified, filesCopiedSource);
        context.setChangeSet(changeSet);
    }

//    private static boolean compare(final InputStream input1, final InputStream input2) throws IOException {
    //                try (
//                        InputStream i1 = new FileInputStream(gitSourceFile);
//                        InputStream i2 = new FileInputStream(ccTargetFile)) {
//                    if (!compare(i1, i2)) {
//                        filesModified.add(file);
//                    }
//                } catch (final IOException e) {
//                    m2.getLogger().error("Failed to compare file.", e);
//                }  boolean error = false;
//        try {
//            final byte[] buffer1 = new byte[1_024];
//            final byte[] buffer2 = new byte[1_024];
//            try {
//                int numRead1 = 0;
//                int numRead2 = 0;
//                while (true) {
//                    numRead1 = input1.read(buffer1);
//                    numRead2 = input2.read(buffer2);
//                    if (numRead1 > -1) {
//                        if (numRead2 != numRead1) {
//                            return false;
//                        }
//                        // Otherwise same number of bytes read
//                        if (!Arrays.equals(buffer1, buffer2)) {
//                            return false;
//                        }
//                        // Otherwise same bytes read, so continue ...
//                    } else {
//                        // Nothing more in stream 1 ...
//                        return numRead2 < 0;
//                    }
//                }
//            } finally {
//                input1.close();
//            }
//        } catch (IOException | RuntimeException e) {
//        } finally {
//            try {
//                input2.close();
//            } catch (final IOException e) {
//            }
//        }
//    }
}
