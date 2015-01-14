/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.git;

import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;

/**
 * Batch of Git related tasks executed before synchronization.
 *
 * @author Daniel Felix Ferber
 */
public class GitPrepareTask implements Runnable {

    /**
     * Config that controlls the task.
     */
    private final GitPrepareConfig config;
    /**
     * Commander that executes git commands.
     */
    private final GitCommander gitCommander;

    public GitPrepareTask(GitPrepareConfig config, GitCommander gitCommander) {
        this.config = config;
        this.gitCommander = gitCommander;
    }

    @Override
    public void run() {
        final Meter m = MeterFactory.getMeter("GitPrepare");
        m.run(() -> {;
            if (config.getApplyDefaultGitConfig()) {
                m.sub("applyDefaultGitConfig").m("Apply default configuration to GIT rempository.").run(() -> {
                    gitCommander.setConfig("merge.defaultToUpstream", "true");
                    gitCommander.setConfig("diff.renameLimit", "10000");
                });
            }
            if (config.getResetLocalGitRepository()) {
                m.sub("resetLocalGitRepository").m("Reset local GIT repository.").run(() -> {
                    gitCommander.resetLocal();
                });
            }
            if (config.getCleanLocalGitRepository()) {
                m.sub("cleanLocalGitRepository").m("Clean local GIT repository.").run(() -> {
                    gitCommander.cleanLocal();
                });
            }
            if (config.getFetchRemoteGitRepository()) {
                m.sub("fetchRemoteGitRepository").m("Fetch new commits from remote GIT rempository.").run(() -> {
                    gitCommander.fetchRemote();
                });

            }
            if (config.getFastForwardLocalGitRepository()) {
                m.sub("fastForwardLocalGitRepository").m("Fast forward commits on local GIT repository.").run(() -> {
                    gitCommander.fastForward();
                });
            }
        });
    }
}
