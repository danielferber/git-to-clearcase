package br.com.danielferber.gittocc2.config.clearcase;

import java.io.File;

/**
 *
 * @author Daniel
 */
public interface ClearToolConfigSource {

    String getActivityMessagePattern();
    
    File getClearToolExec();

    File getCommitStampFile();

    File getCounterStampFile();

    Boolean getCreateActivity();

    Long getOverriddenSyncCounter();

    String getOverriddenSyncFromCommit();

    Boolean getUpdateVobRoot();

    File getVobViewDir();

}
