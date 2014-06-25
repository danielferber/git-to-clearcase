package br.com.danielferber.gittocc2;

import java.util.concurrent.Callable;

import br.com.danielferber.gittocc2.config.git.GitConfigSource;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;


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

    UpdateGitRepositoryTask(final GitConfigSource environmentConfig, final GitCommander ctCommander, final Meter outerMeter) {
        this.gitConfig = environmentConfig;
        this.gitCommander = ctCommander;
        this.meter = MeterFactory.getMeter("UpdateGitRepositoryTask");
    }

    @Override
    public Void call() throws Exception {
        meter.start();
        try {
            if (gitConfig.getApplyDefaultGitConfig() != null && gitConfig.getApplyDefaultGitConfig()) {
                applyDefaultGitConfig();
            }
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
        } catch (final Exception e) {
            meter.fail(e);
            throw e;
        }
        return null;
    }

    private void cleanLocalGitRepository() {
        final Meter m = meter.sub("cleanLocalGitRepository").m("Clean local GIT repository.").start();
        gitCommander.cleanLocal();
        m.ok();
    }

    private void resetLocalGitRepository() {
        final Meter m = meter.sub("resetLocalGitRepository").m("Reset local GIT repository.").start();
        gitCommander.resetLocal();
        m.ok();
    }

    private void fastForwardLocalGitRepository() {
        final Meter m = meter.sub("fastForwardLocalGitRepository").m("Fast forward commits on local GIT repository.").start();
        gitCommander.fastForward();
        m.ok();
    }

    private void fetchRemoteGitRepository() {
        final Meter m = meter.sub("fetchRemoteGitRepository").m("Fetch new commits from remote GIT rempository.").start();
        gitCommander.fetchRemote();
        m.ok();
    }

    private void applyDefaultGitConfig() {
        final Meter m = meter.sub("applyDefaultGitConfig").m("Fetch new commits from remote GIT rempository.").start();
        gitCommander.setConfig("merge.defaultToUpstream", "true");
        gitCommander.setConfig("diff.renameLimit", "10000");
        m.ok();
    }
}
