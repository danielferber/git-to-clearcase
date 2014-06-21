package br.com.danielferber.gittocc2.config.sync;

/**
 *
 * @author Daniel Felix Ferber
 */
public interface SyncConfigSource {
    boolean getUpdateVobRoot();
    boolean getFetchRemoteGitRepository();
    boolean getFastForwardLocalGitRepository();
    boolean getResetLocalGitRepository();
    boolean getCleanLocalGitRepository();
    boolean getCreateActivity();
    String getActivityMessagePattern();
}
