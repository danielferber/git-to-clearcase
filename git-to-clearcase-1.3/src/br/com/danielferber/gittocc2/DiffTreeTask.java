/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.git.GitCommander;
import br.com.danielferber.gittocc2.git.GitConfig;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author x7ws
 */
public class DiffTreeTask implements Runnable {

    private final GitCommander commander;
    private final Context context;

    public DiffTreeTask(Context context, GitConfig gitConfig) {
        this.commander = new GitCommander(gitConfig);
        this.context = context;
    }

    @Override
    public void run() {
        String targetCommit = commander.readCommit();
        String sourceCommit = context.getCurrentCommitStamp();

        final List<File> dirsAdded = new ArrayList<>();
        final List<File> dirsDeleted = new ArrayList<>();

        final List<File> filesAdded = new ArrayList<>();
        final List<File> filesAddedSource = new ArrayList<>();
        final List<File> filesDeleted = new ArrayList<>();

        final List<File> filesModified = new ArrayList<>();
        final List<File> filesModifiedSource = new ArrayList<>();

        final List<File> filesMovedFrom = new ArrayList<>();
        final List<File> filesMovedTo = new ArrayList<>();
        final List<File> filesMovedModified = new ArrayList<>();
        final List<File> filesMovedSource = new ArrayList<>();

        final List<File> filesCopiedFrom = new ArrayList<>();
        final List<File> filesCopiedTo = new ArrayList<>();
        final List<File> filesCopiedModified = new ArrayList<>();
        final List<File> filesCopiedSource = new ArrayList<>();
        
        commander.diffTree(sourceCommit, targetCommit, dirsAdded, dirsDeleted, filesAdded, filesAddedSource, filesDeleted, filesModified, filesModifiedSource, filesMovedFrom, filesMovedTo, filesMovedModified, filesMovedSource, filesCopiedFrom, filesCopiedTo, filesCopiedModified, filesCopiedSource);
        ChangeSet changeset = new ChangeSet(targetCommit, dirsAdded, dirsDeleted, filesAdded, filesAddedSource, filesDeleted, filesModified, filesModifiedSource, filesMovedFrom, filesMovedTo, filesMovedModified, filesMovedSource, filesCopiedFrom, filesCopiedTo, filesCopiedModified, filesCopiedSource);
        context.setChangeSet(changeset);
    }
}
