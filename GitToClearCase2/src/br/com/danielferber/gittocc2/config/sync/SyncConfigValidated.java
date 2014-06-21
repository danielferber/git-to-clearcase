package br.com.danielferber.gittocc2.config.sync;

/**
 *
 * @author Daniel Felix Ferber
 */
public class SyncConfigValidated implements SyncConfigSource {

    private SyncConfigSource wrapped;

    public SyncConfigValidated(SyncConfigSource wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public Boolean getUpdateVobRoot() {
        return wrapped.getUpdateVobRoot();
    }

    @Override
    public Boolean getFetchRemoteGitRepository() {
        return wrapped.getFetchRemoteGitRepository();
    }

    @Override
    public Boolean getFastForwardLocalGitRepository() {
        return wrapped.getFastForwardLocalGitRepository();
    }

    @Override
    public Boolean getResetLocalGitRepository() {
        return wrapped.getResetLocalGitRepository();
    }

    @Override
    public Boolean getCleanLocalGitRepository() {
        return wrapped.getCleanLocalGitRepository();
    }

    @Override
    public Boolean getCreateActivity() {
        return wrapped.getCreateActivity();
    }

    @Override
    public String getActivityMessagePattern() {
        return wrapped.getActivityMessagePattern();
    }
}
