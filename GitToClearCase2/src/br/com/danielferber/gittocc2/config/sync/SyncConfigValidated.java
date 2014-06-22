package br.com.danielferber.gittocc2.config.sync;

import java.io.File;

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

    @Override
    public File getCommitStampFile() {
        return wrapped.getCommitStampFile();
    }

    @Override
    public File getCounterStampFile() {
        return wrapped.getCounterStampFile();
    }

    @Override
    public Long getOverriddenSyncCounter() {
        return wrapped.getOverriddenSyncCounter();
    }

    @Override
    public String getOverriddenSyncFromCommit() {
        return wrapped.getOverriddenSyncFromCommit();
    }
    
    
}
