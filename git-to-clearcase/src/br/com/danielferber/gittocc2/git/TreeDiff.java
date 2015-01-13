/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.git;

import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import java.io.File;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;

/**
 * Container for file system changes that are supposed to synchronize to
 * ClearCase VOB directory.
 *
 * @author Daniel Felix Ferber
 */
public class TreeDiff {

    public final List<File> dirsAdded;
    public final List<File> dirsDeleted;
    public final List<File> filesAdded;
    public final List<File> filesDeleted;
    public final List<File> filesModified;
    public final List<File> filesMovedFrom;
    public final List<File> filesMovedTo;
    public final List<File> filesMovedModified;
    public final List<File> filesCopiedFrom;
    public final List<File> filesCopiedTo;
    public final List<File> filesCopiedModified;

    public TreeDiff(final List<File> dirsAdded, final List<File> dirsDeleted, final List<File> filesAdded, final List<File> filesDeleted, final List<File> filesModified, final List<File> filesMovedFrom, final List<File> filesMovedTo, final List<File> filesMovedModified, final List<File> filesCopiedFrom, final List<File> filesCopiedTo, final List<File> filesCopiedModified) {
        this.dirsAdded = Collections.unmodifiableList(dirsAdded);
        this.dirsDeleted = Collections.unmodifiableList(dirsDeleted);
        this.filesAdded = Collections.unmodifiableList(filesAdded);
        this.filesDeleted = Collections.unmodifiableList(filesDeleted);
        this.filesModified = Collections.unmodifiableList(filesModified);
        this.filesMovedFrom = Collections.unmodifiableList(filesMovedFrom);
        this.filesMovedTo = Collections.unmodifiableList(filesMovedTo);
        this.filesMovedModified = Collections.unmodifiableList(filesMovedModified);
        this.filesCopiedFrom = Collections.unmodifiableList(filesCopiedFrom);
        this.filesCopiedTo = Collections.unmodifiableList(filesCopiedTo);
        this.filesCopiedModified = Collections.unmodifiableList(filesCopiedModified);
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
        ps.println("Files to modified:");
        for (File f : filesModified) {
            ps.println(" - " + f.getPath());
        }
        ps.close();
        ps = LoggerFactory.getInfoPrintStream(logger);
        ps.println("Files moved:");
        for (int i = 0; i < filesMovedFrom.size(); i++) {
            ps.println(" - " + filesMovedFrom.get(i).getPath()+" -> "+filesMovedTo.get(i).getPath());
        }
        ps.println("Files moved and modified:");
        for (File f : filesMovedModified) {
            ps.println(" - " + f.getPath());
        }
        ps.close();
        ps = LoggerFactory.getInfoPrintStream(logger);
        ps.println("Files copied:");
        for (int i = 0; i < filesCopiedFrom.size(); i++) {
            ps.println(" - " + filesCopiedFrom.get(i).getPath()+" -> "+filesCopiedTo.get(i).getPath());
        }
        ps.println("Files copied and modified:");
        for (File f : filesCopiedModified) {
            ps.println(" - " + f.getPath());
        }
        ps.close();
    }
}
