/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.change;

import br.com.danielferber.gittocc2.git.GitCommander;
import br.com.danielferber.gittocc2.git.GitConfig;

/**
 *
 * @author x7ws
 */
public class DiffTreeTask implements Runnable {

    private final GitCommander commander;
    private final ChangeContext context;
    private final ChangeConfig changeConfig;

    public DiffTreeTask(ChangeContext context, GitConfig gitConfig, ChangeConfig changeConfig) {
        this.commander = new GitCommander(gitConfig);
        this.context = context;
        this.changeConfig = changeConfig;
        
        // fails on inconsistent config
        if (changeConfig.getCounterStampOverride() == null) {
            changeConfig.getCounterStampAbsoluteFile();
        }
        if (changeConfig.getCommitStampOverride() == null) {
            changeConfig.getCommitStampAbsoluteFile();
        }
    }

    @Override
    public void run() {
        String targetCommit = commander.readCommit();
        String sourceCommit = changeConfig.getCommitStampOverride() != null ? changeConfig.getCommitStampOverride() : changeConfig.readCommitStampFromFile();
        ChangeSet changeset = commander.diffTree(sourceCommit, targetCommit);
        context.addChangeSet(changeset);
    }
}
