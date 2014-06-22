package br.com.danielferber.gittocc2.config.sync;

import java.io.File;

/**
 *
 * @author Daniel Felix Ferber
 */
public interface SyncConfig extends SyncConfigSource {
    SyncConfig setUpdateVobRoot(Boolean value);
    SyncConfig setFetchRemoteGitRepository(Boolean value);
    SyncConfig setFastForwardLocalGitRepository(Boolean value);
    SyncConfig setResetLocalGitRepository(Boolean value);
    SyncConfig setCleanLocalGitRepository(Boolean value);
    SyncConfig setCreateActivity(Boolean value);
    SyncConfig setActivityMessagePattern(String value);
    SyncConfig setCommitStampFile(File file);
    SyncConfig setCounterStampFile(File file);
    SyncConfig setOverriddenSyncCounter(Long value);
    SyncConfig setOverriddenSyncFromCommit(String commit);
}
