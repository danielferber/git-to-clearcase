package br.com.danielferber.gittocc2.task;

import br.com.danielferber.gittocc2.GitCommander;
import br.com.danielferber.gittocc2.MeterCallable;
import br.com.danielferber.gittocc2.config.git.GitConfigSource;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;

/**
 * Updates the GIT repository directory. The Git configuration may require
 * maintenance operations like clean, reset. The configuration may alsore
 * require fetching commits from the remote repository and a fast forward.
 *
 * @author Daniel Felix Ferber
 */
public class UpdateGitRepositoryTask extends MeterCallable<Void> {

    private final GitConfigSource gitConfig;
    private final GitCommander gitCommander;

    public UpdateGitRepositoryTask(final GitConfigSource environmentConfig, final GitCommander ctCommander) {
        super("UpdateGitRepositoryTask", "Update Git Repository.");
        this.gitConfig = environmentConfig;
        this.gitCommander = ctCommander;
    }

    @Override
    protected Void meteredCall() throws Exception {
        if (gitConfig.getApplyDefaultGitConfig()) {
            applyDefaultGitConfig();
        }
        if (gitConfig.getResetLocalGitRepository()) {
            resetLocalGitRepository();
        }
        if (gitConfig.getCleanLocalGitRepository()) {
            cleanLocalGitRepository();
        }
        if (gitConfig.getFetchRemoteGitRepository()) {
            fetchRemoteGitRepository();
        }
        if (gitConfig.getFastForwardLocalGitRepository()) {
            fastForwardLocalGitRepository();
        }
        return null;
    }

    private void cleanLocalGitRepository() {
        final Meter m = getMeter().sub("cleanLocalGitRepository").m("Clean local GIT repository.").start();
        gitCommander.cleanLocal();
        m.ok();
    }

    private void resetLocalGitRepository() {
        final Meter m = getMeter().sub("resetLocalGitRepository").m("Reset local GIT repository.").start();
        gitCommander.resetLocal();
        m.ok();
    }

    private void fastForwardLocalGitRepository() {
        final Meter m = getMeter().sub("fastForwardLocalGitRepository").m("Fast forward commits on local GIT repository.").start();
        gitCommander.fastForward();
        m.ok();
    }

    private void fetchRemoteGitRepository() {
        final Meter m = getMeter().sub("fetchRemoteGitRepository").m("Fetch new commits from remote GIT rempository.").start();
        gitCommander.fetchRemote();
        m.ok();
    }

    private void applyDefaultGitConfig() {
        final Meter m = getMeter().sub("applyDefaultGitConfig").m("Fetch new commits from remote GIT rempository.").start();
        gitCommander.setConfig("merge.defaultToUpstream", "true");
        gitCommander.setConfig("diff.renameLimit", "10000");
        m.ok();
    }
}
