package br.com.danielferber.gittocc2.config.clearcase;

import java.io.File;

/**
 *
 * @author Daniel
 */
public interface ClearToolConfig extends ClearToolConfigSource {

    ClearToolConfig setActivityMessagePattern(String value);

    ClearToolConfig setClearToolExec(File file);

    ClearToolConfig setCommitStampFile(File file);

    ClearToolConfig setCounterStampFile(File file);

    ClearToolConfig setCreateActivity(Boolean value);

    ClearToolConfig setOverriddenSyncCounter(Long value);

    ClearToolConfig setOverriddenSyncFromCommit(String commit);

    ClearToolConfig setUpdateVobRoot(Boolean value);

    ClearToolConfig setVobViewDir(File dir);

}
