/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.change.ChangeSet;
import br.com.danielferber.gittocc2.cc.CcCommander;
import br.com.danielferber.gittocc2.cc.CcConfig;
import br.com.danielferber.gittocc2.change.ChangeConfig;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 *
 * @author x7ws
 */
public class ApplyTask implements Runnable {

    private final Context changeContext;
    private final ChangeConfig changeConfig;
    private final CcCommander ctCommander;

    public ApplyTask(Context changeContext, CcConfig ccConfig, ChangeConfig changeConfig) {
        this.changeContext = changeContext;
        this.changeConfig = changeConfig;
        this.ctCommander = new CcCommander(ccConfig);
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

    private void copyFilesFromSource(final List<File> targets, final List<File> sources) {
        Iterator<File> targetIt = targets.iterator();
        Iterator<File> sourceIt = sources.iterator();

        while (targetIt.hasNext()) {
            final File sourceFile = sourceIt.next();
            final File targetFile = targetIt.next();
            if (sourceFile == null) {
                continue;
            }
            try {
                Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (final IOException e) {
//                .
//                error("Failed to copy file.", e);
            }
        }
    }

    @Override
    public void run() {
        final Meter m = Meter.getCurrentInstance();
        ChangeSet diff = changeContext.getChangeSet();
        if (diff == null) {
            return;
        }
        
        if (!diff.dirsAdded.isEmpty()) {
            m.sub("dir.add.checkout.parent").m("Add new directories: checkout parent directories.").run(() -> {
                final Collection<File> dirs = parentDirs(roots(diff.dirsAdded));
                ctCommander.checkoutDirs(dirs);
            });

            m.sub("dir.add.make").m("Add new directories: make directory elements.").run(() -> {
                ctCommander.makeDirs(diff.dirsAdded);
            });
        }

        if (!diff.filesAdded.isEmpty()) {
            m.sub("file.add.checkout.parent").m("Add new files: checkout parent directories.").run(() -> {
                ctCommander.checkoutDirs(parentDirs(diff.filesAdded));
            });

            m.sub("file.add.make").m("Add new files: make file elements.").run(() -> {
                ctCommander.makeFiles(diff.filesAdded);
            });

            m.sub("file.add.write").m("Add new files: write file content.").iterations(diff.filesAdded.size()).run(() -> {
                copyFilesFromSource(diff.filesAdded, diff.filesAddedSource);
            });
        }

        if (!diff.filesCopiedTo.isEmpty()) {
            m.sub("file.copy.checkout.parent").m("Copy files: checkout target parent directories.").run(() -> {
                ctCommander.checkoutDirs(parentDirs(diff.filesCopiedTo));
            });

            m.sub("file.copy.make").m("Copy files: make file elements.").run(() -> {
                ctCommander.makeFiles(diff.filesCopiedTo);
            });

            if (!diff.filesCopiedTo.isEmpty()) {
                m.sub("file.copy.write").m("Copy files: write file content.").iterations(diff.filesCopiedTo.size()).run(() -> {
                    copyFilesFromSource(diff.filesCopiedTo, diff.filesCopiedSource);
                });
            }
        }

        if (!diff.filesMovedFrom.isEmpty()) {
            m.sub("file.move.checkout.parent").m("Move files: checkout source parent directories.").run(() -> {
                ctCommander.checkoutDirs(parentDirs(diff.filesMovedFrom));
            });

            m.sub("file.move.checkout.parent").m("Move files: checkout target parent directories.").run(() -> {
                ctCommander.checkoutDirs(parentDirs(diff.filesMovedTo));
            });

            m.sub("file.move.make").m("Move files: move elements.").iterations(diff.filesMovedTo.size()).run(() -> {
                final Iterator<File> sourceIterator = diff.filesMovedFrom.iterator();
                final Iterator<File> targetIterator = diff.filesMovedTo.iterator();
                while (sourceIterator.hasNext()) {
                    final File source = sourceIterator.next();
                    final File target = targetIterator.next();
                    ctCommander.moveFile(source, target);
                }
            });

            if (!diff.filesMovedModified.isEmpty()) {
                m.sub("file.move.checkout.parent").m("Move files: checkout parent directoties.").run(() -> {
                    ctCommander.checkoutFiles(diff.filesMovedModified);
                });

                m.sub("file.move.write").m("Move files: write file content.").iterations(diff.filesMovedModified.size()).run(() -> {
                    copyFilesFromSource(diff.filesMovedModified, diff.filesMovedSource);
                });
            }
        }

        if (!diff.filesModified.isEmpty()) {
            m.sub("file.modify.checkout").m("Modify files: checkout files.").run(() -> {
                ctCommander.checkoutFiles(diff.filesModified);
            });

            m.sub("file.modify.write").m("Modify files: write file content.").run(() -> {
                copyFilesFromSource(diff.filesModified, diff.filesModifiedSource);
            });
        }

        if (!diff.dirsDeleted.isEmpty()) {
            final Collection<File> dirsToDelete = roots(diff.dirsDeleted);

            if (!dirsToDelete.isEmpty()) {
                m.sub("dir.delete.checkout.parent").m("Delete directoties: checkout parent directories.").run(() -> {
                    ctCommander.checkoutDirs(parentDirs(dirsToDelete));
                });

                m.sub("dir.delete.remove").m("Delete directoties: delete element names.").run(() -> {
                    ctCommander.removeDirs(dirsToDelete);
                });
            }
        }

        if (!diff.filesDeleted.isEmpty()) {
            final TreeSet<File> filesToDelete = leafes(diff.dirsDeleted, diff.filesDeleted);

            if (!filesToDelete.isEmpty()) {
                m.sub("file.delete.checkout.parent").m("Delete files: checkout parent directories.").run(() -> {
                    ctCommander.checkoutDirs(parentDirs(filesToDelete));
                });

                m.sub("file.delete.remove").m("Delete files: delete element names.").run(() -> {
                    ctCommander.removeFiles(filesToDelete);
                });
            }
        }
    }
}
