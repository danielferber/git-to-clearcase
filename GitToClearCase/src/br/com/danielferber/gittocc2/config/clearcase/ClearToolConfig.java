package br.com.danielferber.gittocc2.config.clearcase;

import java.io.File;

/**
 *
 * @author Daniel
 */
public interface ClearToolConfig extends ClearToolConfigSource {
    ClearToolConfig setClearToolExec(File file);
    ClearToolConfig setVobViewDir(File dir);
    ClearToolConfig setUpdateVobRoot(Boolean value);

    ClearToolConfig setUseSyncActivity(Boolean value);
    ClearToolConfig setUseStampActivity(Boolean value);
    ClearToolConfig setSyncActivityName(String value);
    ClearToolConfig setStampActivityName(String value);


    ClearToolConfig setUseCommitStampFile(Boolean value);
    ClearToolConfig setUseCounterStampFile(Boolean value);
    ClearToolConfig setCommitStampFile(File file);
    ClearToolConfig setCounterStampFile(File file);
    ClearToolConfig setOverriddenSyncCounter(Long value);
    ClearToolConfig setOverriddenSyncFromCommit(String commit);

}
