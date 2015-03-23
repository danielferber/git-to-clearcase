/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.change;

import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;

/**
 *
 * @author x7ws
 */
public class ChangeSet {

    /**
     * List of view private directories to add to clearcase.
     */
    public final List<File> dirsAdded;
    /**
     * List of view version directories to remove from clearcase.
     */
    public final List<File> dirsDeleted;
    /**
     * List of view private files to add to clearcase.
     */
    public final List<File> filesAdded;
    /**
     * List of view version files to remove from clearcase.
     */
    public final List<File> filesDeleted;
    public final List<File> filesModified;
    public final List<File> filesModifiedSource;
    public final List<File> filesMovedFrom;
    public final List<File> filesMovedTo;
    public final List<File> filesMovedModified;
    public final List<File> filesMovedSource;
    public final List<File> filesCopiedFrom;
    public final List<File> filesCopiedTo;
    public final List<File> filesCopiedModified;
    public final List<File> filesCopiedSource;

    public static ChangeSet createFileChangeSet(final Collection<Path> dirsAdded, final Collection<Path> dirsDeleted, final Collection<Path> filesAdded, final Collection<Path> filesDeleted, final Collection<Path> filesModified, final Collection<Path> filesModifiedSource, final Collection<Path> filesMovedFrom, final Collection<Path> filesMovedTo, final Collection<Path> filesMovedModified, final Collection<Path> filesMovedSource, final Collection<Path> filesCopiedFrom, final Collection<Path> filesCopiedTo, final Collection<Path> filesCopiedModified, final Collection<Path> filesCopiedSource) {
        return new ChangeSet(pathToFile(dirsAdded), pathToFile(dirsDeleted),
                pathToFile(filesAdded), pathToFile(filesDeleted),
                pathToFile(filesModified), pathToFile(filesModifiedSource),
                pathToFile(filesMovedFrom), pathToFile(filesMovedTo), pathToFile(filesMovedModified), pathToFile(filesMovedSource),
                pathToFile(filesCopiedFrom), pathToFile(filesCopiedTo), pathToFile(filesCopiedModified), pathToFile(filesCopiedSource));
    }

    private static List<File> pathToFile(Collection<Path> paths) {
        List<File> result = new ArrayList<>(paths.size());
        for (Path path : paths) {
            result.add(path.toFile());
        }
        return result;
    }

    public ChangeSet(final List<File> dirsAdded, final List<File> dirsDeleted, final List<File> filesAdded, final List<File> filesDeleted, final List<File> filesModified, final List<File> filesModifiedSource, final List<File> filesMovedFrom, final List<File> filesMovedTo, final List<File> filesMovedModified, final List<File> filesMovedSource, final List<File> filesCopiedFrom, final List<File> filesCopiedTo, final List<File> filesCopiedModified, final List<File> filesCopiedSource) {
        this.dirsAdded = Collections.unmodifiableList(dirsAdded);
        this.dirsDeleted = Collections.unmodifiableList(dirsDeleted);
        this.filesAdded = Collections.unmodifiableList(filesAdded);
        this.filesDeleted = Collections.unmodifiableList(filesDeleted);
        this.filesModified = Collections.unmodifiableList(filesModified);
        this.filesModifiedSource = Collections.unmodifiableList(filesModifiedSource);
        this.filesMovedFrom = Collections.unmodifiableList(filesMovedFrom);
        this.filesMovedTo = Collections.unmodifiableList(filesMovedTo);
        this.filesMovedModified = Collections.unmodifiableList(filesMovedModified);
        this.filesMovedSource = Collections.unmodifiableList(filesMovedSource);
        this.filesCopiedFrom = Collections.unmodifiableList(filesCopiedFrom);
        this.filesCopiedTo = Collections.unmodifiableList(filesCopiedTo);
        this.filesCopiedModified = Collections.unmodifiableList(filesCopiedModified);
        this.filesCopiedSource = Collections.unmodifiableList(filesCopiedSource);
        if (this.filesCopiedModified.size() != this.filesCopiedSource.size()) {
            throw new IllegalArgumentException("this.filesCopiedModified.size() != this.filesCopiedSource.size()");
        }
        if (this.filesMovedModified.size() != this.filesMovedSource.size()) {
            throw new IllegalArgumentException("this.filesMovedModified.size() != this.filesMovedSource.size()");
        }
        if (this.filesModified.size() != this.filesModifiedSource.size()) {
            throw new IllegalArgumentException("this.filesModified.size() != this.filesModifiedSource.size()");
        }
    }

    public boolean hasStuff() {
        return !(dirsAdded.isEmpty()
                && dirsDeleted.isEmpty()
                && filesAdded.isEmpty()
                && filesDeleted.isEmpty()
                && filesModified.isEmpty()
                && filesMovedFrom.isEmpty()
                && filesMovedTo.isEmpty()
                && filesMovedModified.isEmpty()
                && filesCopiedFrom.isEmpty()
                && filesCopiedTo.isEmpty()
                && filesCopiedModified.isEmpty());
    }

    @Override
    public String toString() {
        return "ChangeSet{" + "dir.add=#" + dirsAdded.size() + ", dir.del=#" + dirsDeleted.size() + ", file.add=#" + filesAdded.size() + ", file.del=#" + filesDeleted.size() + ", file.mod=#" + filesModified.size() + ", file.mov=#" + filesMovedFrom.size() + ", file.cop=#" + filesCopiedFrom.size() + '}';
    }
    
    public void log(Logger logger) {
        PrintStream ps = LoggerFactory.getInfoPrintStream(logger);
        ps.println("Dirs to add:");
        for (File f : dirsAdded) {
            ps.println(" - " + f.getPath());
        }
        ps.close();
        ps = LoggerFactory.getInfoPrintStream(logger);
        ps.println("Files to add:");
        for (File f : filesAdded) {
            ps.println(" - " + f.getPath());
        }
        ps.close();
        ps = LoggerFactory.getInfoPrintStream(logger);
        ps.println("Dirs to delete:");
        for (File f : dirsDeleted) {
            ps.println(" - " + f.getPath());
        }
        ps.close();
        ps = LoggerFactory.getInfoPrintStream(logger);
        ps.println("Files to delete:");
        for (File f : filesDeleted) {
            ps.println(" - " + f.getPath());
        }
        ps.close();
        ps = LoggerFactory.getInfoPrintStream(logger);
        ps.println("Files to modify:");
        for (File f : filesModified) {
            ps.println(" - " + f.getPath());
        }
        ps.close();
        ps = LoggerFactory.getInfoPrintStream(logger);
        ps.println("Files to move:");
        for (int i = 0; i < filesMovedFrom.size(); i++) {
            ps.println(" - " + filesMovedFrom.get(i).getPath() + " -> " + filesMovedTo.get(i).getPath());
        }
        ps.println("Files to modify after move:");
        for (File f : filesMovedModified) {
            ps.println(" - " + f.getPath());
        }
        ps.close();
        ps = LoggerFactory.getInfoPrintStream(logger);
        ps.println("Files to copy:");
        for (int i = 0; i < filesCopiedFrom.size(); i++) {
            ps.println(" - " + filesCopiedFrom.get(i).getPath() + " -> " + filesCopiedTo.get(i).getPath());
        }
        ps.println("Files o modify after copy:");
        for (File f : filesCopiedModified) {
            ps.println(" - " + f.getPath());
        }
        ps.close();
    }
}
