package br.com.danielferber.gittocc2.config.clearcase;

import java.io.File;

/**
 *
 * @author Daniel
 */
public interface ClearToolConfig extends ClearToolConfigSource {

    ClearToolConfig setSyncActivityName(String value);

    ClearToolConfig setStampActivityName(String value);

    ClearToolConfig setClearToolExec(File file);

    ClearToolConfig setCommitStampFileName(File file);

    ClearToolConfig setCounterStampFileName(File file);

    ClearToolConfig setUseSyncActivity(Boolean value);

    ClearToolConfig setUseStampActivity(Boolean value);

    ClearToolConfig setOverriddenSyncCounter(Long value);

    ClearToolConfig setOverriddenSyncFromCommit(String commit);

    ClearToolConfig setUpdateVobRoot(Boolean value);

    ClearToolConfig setVobViewDir(File dir);
}
