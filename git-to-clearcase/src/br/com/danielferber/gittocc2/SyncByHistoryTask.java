/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.task.config.SyncByHistoryConfiguration;

/**
 *
 * @author Daniel Felix Ferber
 */
class SyncByHistoryTask extends SyncStrategyTask {

    public SyncByHistoryTask(SyncByHistoryConfiguration configuration) {
        super("SyncByHistory", "Synchronize using Git history", configuration);
    }

    @Override
    protected Void meteredCall() throws Exception {         
                = (new GitTreeDiffTask(gitCommander, syncFromCommit, syncToCommit)).call();
    }
}
