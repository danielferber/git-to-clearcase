package br.com.danielferber.gittocc2.config.sync;

import java.io.File;

/**
 *
 * @author Daniel Felix Ferber
 */
public class SyncConfigChain implements SyncConfigSource {

    private final SyncConfigSource wrapped1;
    private final SyncConfigSource wrapped2;

    public SyncConfigChain(SyncConfigSource wrapped1, SyncConfigSource wrapped2) {
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

    @Override
    public File getCommitStampFile() {
        if (wrapped2.getCommitStampFile() != null) {
            return wrapped2.getCommitStampFile();
        }
        return wrapped1.getCommitStampFile();
    }

    @Override
    public File getCounterStampFile() {
        if (wrapped2.getCounterStampFile() != null) {
            return wrapped2.getCounterStampFile();
        }
        return wrapped1.getCounterStampFile();
    }

    @Override
    public Long getOverriddenSyncCounter() {
        if (wrapped2.getOverriddenSyncCounter() != null) {
            return wrapped2.getOverriddenSyncCounter();
        }
        return wrapped1.getOverriddenSyncCounter();
    }

    @Override
    public String getOverriddenSyncFromCommit() {
        if (wrapped2.getOverriddenSyncFromCommit() != null) {
            return wrapped2.getOverriddenSyncFromCommit();
        }
        return wrapped1.getOverriddenSyncFromCommit();
    }
    
    

}
