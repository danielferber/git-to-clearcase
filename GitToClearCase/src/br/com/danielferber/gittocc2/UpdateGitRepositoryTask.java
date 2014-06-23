package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.git.GitConfigSource;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import java.util.concurrent.Callable;

/**
 * Updates the GIT repository directory. The Git configuration may require
 * maintenance operations like clean, reset. The configuration may alsore
 * require fetching commits from the remote repository and a fast forward.
 *
 * @author Daniel Felix Ferber
 */
class UpdateGitRepositoryTask implements Callable<Void> {

    private final GitConfigSource gitConfig;
    private final GitCommander gitCommander;
    private final Meter meter;

    UpdateGitRepositoryTask(GitConfigSource environmentConfig, GitCommander ctCommander, Meter outerMeter) {
        this.gitConfig = environmentConfig;
        this.gitCommander = ctCommander;
        this.meter = outerMeter.sub("UpdateGit");
    }

    @Override
    public Void call() throws Exception {
        meter.start();
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
            meter.ok();
        } catch (Exception e) {
            meter.fail(e);
            throw e;
        }
        return null;
    }

    private void cleanLocalGitRepository() {
        Meter m = meter.sub("clean").m("Clean local GIT repository.").start();
        gitCommander.cleanLocal();
        m.ok();
    }

    private void resetLocalGitRepository() {
        Meter m = meter.sub("reset").m("Reset local GIT repository.").start();
        gitCommander.resetLocal();
        m.ok();
    }

    private void fastForwardLocalGitRepository() {
        Meter m = meter.sub("fastForward").m("Fast forward commits on local GIT repository.").start();
        gitCommander.fastForward();
        m.ok();
    }

    private void fetchRemoteGitRepository() {
        Meter m = meter.sub("fetch").m("Fetch new commits from remote GIT rempository.").start();
        gitCommander.fetchRemote();
        m.ok();
    }
}
