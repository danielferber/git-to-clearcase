package br.com.danielferber.gittocc2.config.sync;

/**
 *
 * @author Daniel Felix Ferber
 */
public interface SyncConfig extends SyncConfigSource {
    SyncConfig setUpdateVobRoot(boolean value);
    SyncConfig setFetchRemoteGitRepository(boolean value);
    SyncConfig setFastForwardLocalGitRepository(boolean value);
    SyncConfig setResetLocalGitRepository(boolean value);
    SyncConfig setCleanLocalGitRepository(boolean value);
    SyncConfig setCreateActivity(boolean value);
    SyncConfig setActivityMessagePattern(String value);
}
