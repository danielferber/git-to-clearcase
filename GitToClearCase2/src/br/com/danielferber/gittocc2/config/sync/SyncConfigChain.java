package br.com.danielferber.gittocc2.config.sync;

/**
 *
 * @author Daniel Felix Ferber
 */
public class SyncConfigChain implements SyncConfigSource {

    private final SyncConfigChain wrapped1;
    private final SyncConfigChain wrapped2;

    public SyncConfigChain(SyncConfigChain wrapped1, SyncConfigChain wrapped2) {
        this.wrapped1 = wrapped1;
        this.wrapped2 = wrapped2;
    }

    @Override
    public Boolean getUpdateVobRoot() {
        if (wrapped2.getUpdateVobRoot() != null) {
            return wrapped2.getUpdateVobRoot();
        }
        return wrapped1.getUpdateVobRoot();
    }

    @Override
    public Boolean getFetchRemoteGitRepository() {
        if (wrapped2.getFetchRemoteGitRepository() != null) {
            return wrapped2.getFetchRemoteGitRepository();
        }
        return wrapped1.getFetchRemoteGitRepository();
    }

    @Override
    public Boolean getFastForwardLocalGitRepository() {
        if (wrapped2.getFastForwardLocalGitRepository() != null) {
            return wrapped2.getFastForwardLocalGitRepository();
        }
        return wrapped1.getFastForwardLocalGitRepository();
    }

    @Override
    public Boolean getResetLocalGitRepository() {
        if (wrapped2.getResetLocalGitRepository() != null) {
            return wrapped2.getResetLocalGitRepository();
        }
        return wrapped1.getResetLocalGitRepository();
    }

    @Override
    public Boolean getCleanLocalGitRepository() {
        if (wrapped2.getCleanLocalGitRepository() != null) {
            return wrapped2.getCleanLocalGitRepository();
        }
        return wrapped1.getCleanLocalGitRepository();
    }

    @Override
    public Boolean getCreateActivity() {
        if (wrapped2.getCreateActivity() != null) {
            return wrapped2.getCreateActivity();
        }
        return wrapped1.getCreateActivity();
    }

    @Override
    public String getActivityMessagePattern() {
        if (wrapped2.getActivityMessagePattern() != null) {
            return wrapped2.getActivityMessagePattern();
        }
        return wrapped1.getActivityMessagePattern();
    }

}
