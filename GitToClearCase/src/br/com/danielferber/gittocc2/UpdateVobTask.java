/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigSource;
import br.com.danielferber.gittocc2.config.git.GitConfigSource;
import java.util.concurrent.Callable;

/**
 *
 * @author daniel
 */
public class UpdateVobTask implements Callable<Void> {

    private final GitConfigSource syncConfig;
    private final ClearToolConfigSource environmentConfig;
    private final ClearToolCommander ctCommander;

    public UpdateVobTask(GitConfigSource syncConfig, ClearToolConfigSource environmentConfig) {
        this.syncConfig = syncConfig;
        this.environmentConfig = environmentConfig;
    }

    @Override
    public Void call() throws Exception {
        if (syncConfig.getUpdateVobRoot()) {
            updateFullVob();
        } else {
            updateCommitStampFile();
            updateCounterStampFile();
        }

    }

}
