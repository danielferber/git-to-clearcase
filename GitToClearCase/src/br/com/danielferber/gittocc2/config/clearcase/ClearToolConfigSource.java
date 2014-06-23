package br.com.danielferber.gittocc2.config.clearcase;

import java.io.File;

/**
 *
 * @author Daniel
 */
public interface ClearToolConfigSource {

    String getActivityMessagePattern();
    
    File getClearToolExec();

    File getCommitStampFileName();

    File getCounterStampFileName();

    Boolean getCreateActivity();

    Long getOverriddenSyncCounter();

    String getOverriddenSyncFromCommit();

    Boolean getUpdateVobRoot();

    File getVobViewDir();
    
    File getCommitStampAbsoluteFile();

    File getCounterStampAbsoluteFile();
}
