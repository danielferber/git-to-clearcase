/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.git;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author X7WS
 */
public class GitHistory {

    final String fromCommit;
    final String toCommit;
    final String report;
    final List<File> filesAdded = new ArrayList<File>();
    final List<File> filesDeleted = new ArrayList<File>();
    final List<File> filesModified = new ArrayList<File>();
    final List<File> filesMovedFrom = new ArrayList<File>();
    final List<File> filesMovedTo = new ArrayList<File>();
    final List<File> filesCopiedFrom = new ArrayList<File>();
    final List<File> filesCopiedTo = new ArrayList<File>();

    public GitHistory(String fromCommit, String toCommit, String report) {
        this.fromCommit = fromCommit;
        this.toCommit = toCommit;
        this.report = report;
    }
    
    public String getReport() {
        return report;
    }

    public List<File> getFilesAdded() {
        return Collections.unmodifiableList(filesAdded);
    }

    public List<File> getFilesCopiedFrom() {
        return Collections.unmodifiableList(filesCopiedFrom);
    }

    public List<File> getFilesCopiedTo() {
        return Collections.unmodifiableList(filesCopiedTo);
    }

    public List<File> getFilesDeleted() {
        return Collections.unmodifiableList(filesDeleted);
    }

    public List<File> getFilesModified() {
        return Collections.unmodifiableList(filesModified);
    }

    public List<File> getFilesMovedFrom() {
        return Collections.unmodifiableList(filesMovedFrom);
    }

    public List<File> getFilesMovedTo() {
        return Collections.unmodifiableList(filesMovedTo);
    }
}
