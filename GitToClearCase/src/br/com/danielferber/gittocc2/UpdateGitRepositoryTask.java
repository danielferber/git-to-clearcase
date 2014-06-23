/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.git.GitConfigSource;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import java.util.concurrent.Callable;

/**
 *
 * @author daniel
 */
class UpdateGitRepositoryTask implements Callable<Void> {

    private final GitConfigSource gitConfig;
    private final GitCommander gitCommander;
    private final Meter globalMeter;

    UpdateGitRepositoryTask(GitConfigSource environmentConfig, GitCommander ctCommander, Meter outerMeter) {
        this.gitConfig = environmentConfig;
        this.gitCommander = ctCommander;
        this.globalMeter = outerMeter.sub("UpdateGitRepository");
    }

    @Override
    public Void call() throws Exception {
        globalMeter.start();
        try {
            if (gitConfig.getResetLocalGitRepository() != null && gitConfig.getResetLocalGitRepository()) {
                resetLocalGitRepository();
            }
            if (gitConfig.getCleanLocalGitRepository() != null && gitConfig.getCleanLocalGitRepository()) {
                cleanLocalGitRepository();
            }
            if (gitConfig.getFetchRemoteGitRepository() != null && gitConfig.getFetchRemoteGitRepository()) {
                fetchRemoteGitRepository();
            }
            if (gitConfig.getFastForwardLocalGitRepository() != null && gitConfig.getFastForwardLocalGitRepository()) {
                fastForwardLocalGitRepository();
            }
            globalMeter.ok();
        } catch (Exception e) {
            globalMeter.fail(e);
            throw e;
        }
        return null;
    }

    private void cleanLocalGitRepository() {
        Meter m = globalMeter.sub("clean").m("Clean local GIT repository.").start();
        gitCommander.cleanLocal();
        m.ok();
    }

    private void resetLocalGitRepository() {
        Meter m = globalMeter.sub("reset").m("Reset local GIT repository.").start();
        gitCommander.resetLocal();
        m.ok();
    }

    private void fastForwardLocalGitRepository() {
        Meter m = globalMeter.sub("fastForward").m("Fast forward commits on local GIT repository.").start();
        gitCommander.fastForward();
        m.ok();
    }

    private void fetchRemoteGitRepository() {
        Meter m = globalMeter.sub("fetch").m("Fetch new commits from remote GIT rempository.").start();
        gitCommander.fetchRemote();
        m.ok();
    }
}
