package br.com.danielferber.gittocc2.config.sync;

/**
 *
 * @author Daniel Felix Ferber
 */
public interface SyncConfigSource {
    Boolean getUpdateVobRoot();
    Boolean getFetchRemoteGitRepository();
    Boolean getFastForwardLocalGitRepository();
    Boolean getResetLocalGitRepository();
    Boolean getCleanLocalGitRepository();
    Boolean getCreateActivity();
    String getActivityMessagePattern();
}
