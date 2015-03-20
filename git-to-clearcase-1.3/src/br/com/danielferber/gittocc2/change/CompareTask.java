/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.change;

import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author x7ws
 */
public class CompareTask implements Runnable {
    private final ChangeContext context;

    private final Path sourceRootDir;
    private final Path destRootDir;
    private final Path innerPath;

    public CompareTask(ChangeContext context, final Path sourceRootDir, final Path destRootDir, final Path innerPath) {
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
        Meter meter = MeterFactory.getMeter("CompareTask").start();

        Meter m2 = null;
        final Set<Path> relativeDestDirs = new TreeSet<>();
        final Set<Path> relativeDestFiles = new TreeSet<>();
        final Set<Path> relativeSourceDirs = new TreeSet<>();
        final Set<Path> relativeSourceFiles = new TreeSet<>();

        final ChangeSet changeSet;
        try {
            meter.sub("scan.vob").m("ClearCase VOB scan.").run(new Visitor(destRootDir, destRootDir.resolve(innerPath), relativeDestDirs, relativeDestFiles));
            meter.sub("scan.git").m("GIT repository scan.").run(new Visitor(sourceRootDir, sourceRootDir.resolve(innerPath),relativeSourceDirs, relativeSourceFiles));

            /* Calculate changes. */
            final Set<Path> dirsAdded = new TreeSet<>(relativeSourceDirs);
            dirsAdded.removeAll(relativeDestDirs);
            final Set<Path> dirsDeleted = new TreeSet<>(relativeDestDirs);
            dirsDeleted.removeAll(relativeSourceDirs);
            final Set<Path> filesAdded = new TreeSet<>(relativeSourceFiles);
            filesAdded.removeAll(relativeDestFiles);
            final Set<Path> filesDeleted = new TreeSet<>(relativeDestFiles);
            filesDeleted.removeAll(relativeSourceFiles);

            final Set<Path> filesToCompare = new TreeSet<>(relativeDestFiles);
            filesToCompare.retainAll(relativeSourceFiles);
            m2 = meter.sub("scan.compare").m("Compare file by file.").iterations(filesToCompare.size()).start();
            final Set<Path> filesModified = new TreeSet<>();
            final Set<Path> filesModifiedSource = new TreeSet<>();
            for (final Path relativeFile : filesToCompare) {
                final Path sourceFile = sourceRootDir.resolve(relativeFile);
                final Path destFile = destRootDir.resolve(relativeFile);
                final long sourceSize = Files.size(sourceFile);
                final long destSize = Files.size(destFile);
                if (sourceSize != destSize) {
                    filesModified.add(relativeFile);
                    filesModifiedSource.add(sourceFile.toAbsolutePath());
                    continue;
                }
//                try (
//                        InputStream i1 = new FileInputStream(gitSourceFile);
//                        InputStream i2 = new FileInputStream(ccTargetFile)) {
//                    if (!compare(i1, i2)) {
//                        filesModified.add(file);
//                    }
//                } catch (final IOException e) {
//                    m2.getLogger().error("Failed to compare file.", e);
//                }
                m2.inc().progress();
            }
            m2.ok();
            final Set<Path> filesMovedFrom = Collections.emptySet();
            final Set<Path> filesMovedTo = Collections.emptySet();
            final Set<Path> filesMovedModified = Collections.emptySet();
            final Set<Path> filesMovedSource = Collections.emptySet();
            final Set<Path> filesCopiedFrom = Collections.emptySet();
            final Set<Path> filesCopiedTo = Collections.emptySet();
            final Set<Path> filesCopiedModified = Collections.emptySet();
            final Set<Path> filesCopiedSource = Collections.emptySet();
            changeSet = ChangeSet.createFileChangeSet(
                    dirsAdded, dirsDeleted,
                    filesAdded, filesDeleted,
                    filesModified, filesModifiedSource, filesMovedFrom, filesMovedTo, filesMovedModified,
                    filesMovedSource, filesCopiedFrom, filesCopiedTo, filesCopiedModified, filesCopiedSource);
            context.addChangeSet(changeSet);
            meter.ok();
        } catch (final Exception e) {
            meter.fail(e);
            throw new RuntimeException(e);
        }
    }

    private static boolean compare(final InputStream input1, final InputStream input2) throws IOException {
        boolean error = false;
        try {
            final byte[] buffer1 = new byte[1_024];
            final byte[] buffer2 = new byte[1_024];
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
        } catch (IOException | RuntimeException e) {
            error = true; // this error should be thrown, even if there is an error closing stream 2
            throw e;
        } finally {
            try {
                input2.close();
            } catch (final IOException e) {
                if (!error) {
                    throw e;
                }
            }
        }
    }
}
