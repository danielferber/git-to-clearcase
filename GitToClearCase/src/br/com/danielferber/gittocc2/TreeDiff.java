/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author X7WS
 */
class TreeDiff {

    public final List<File> dirsAdded;//
    public final List<File> dirsDeleted;//
    public final List<File> filesAdded;//
    public final List<File> filesDeleted;//
    public final List<File> filesModified;
    public final List<File> filesMovedFrom;//
    public final List<File> filesMovedTo;//
    public final List<File> filesMovedModified;//
    public final List<File> filesCopiedFrom;//
    public final List<File> filesCopiedTo;//
    public final List<File> filesCopiedModified;//

    public TreeDiff(List<File> dirsAdded, List<File> dirsDeleted, List<File> filesAdded, List<File> filesDeleted, List<File> filesModified, List<File> filesMovedFrom, List<File> filesMovedTo, List<File> filesMovedModified, List<File> filesCopiedFrom, List<File> filesCopiedTo, List<File> filesCopiedModified) {
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
}
