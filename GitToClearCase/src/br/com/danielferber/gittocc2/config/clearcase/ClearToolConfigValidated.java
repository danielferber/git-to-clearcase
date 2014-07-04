package br.com.danielferber.gittocc2.config.clearcase;

import java.io.File;

import br.com.danielferber.gittocc2.config.ConfigException;

/**
 *
 * @author Daniel
 */
public class ClearToolConfigValidated extends ClearToolConfigSourceImpl implements ClearToolConfigSource {

    private final ClearToolConfigSource wrapped;

    public ClearToolConfigValidated(final ClearToolConfigSource wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public String getSyncActivityName() {
        if (wrapped.getSyncActivityName() == null) {
            throw new ConfigException("Sync activity name: missing value.");
        }
        return wrapped.getSyncActivityName();
    }

    @Override
    public String getStampActivityName() {
        if (wrapped.getStampActivityName() == null) {
            throw new ConfigException("Stamp activity name: missing value.");
        }
        return wrapped.getStampActivityName();
    }

    @Override
    public File getClearToolExec() {
        final File exec = wrapped.getClearToolExec();
        if (exec == null) {
            throw new ConfigException("ClearTool executable: missing property.");
        }
        if (!exec.exists()) {
            throw new ConfigException("ClearTool executable: does not exist.");
        }
        if (!exec.isFile()) {
            throw new ConfigException("ClearTool executable: not a file.");
        }
        if (!exec.canExecute()) {
            throw new ConfigException("ClearTool executable: not executable.");
        }
        return exec;
    }

    @Override
    public File getCommitStampFileName() {
        if (wrapped.getCommitStampFileName() == null) {
            throw new ConfigException("Commit stamp file name: missing value.");
        }
        return wrapped.getCommitStampFileName();
    }

    @Override
    public File getCounterStampFileName() {
        if (wrapped.getCounterStampFileName() == null) {
            throw new ConfigException("counter stamp file name: missing value.");
        }
        return wrapped.getCounterStampFileName();
    }

    @Override
    public Boolean getUseSyncActivity() {
        if (wrapped.getUseSyncActivity() == null) {
            throw new ConfigException("Use sync activity: missing value.");
        }
        return wrapped.getUseSyncActivity();
    }

    @Override
    public Boolean getUseStampActivity() {
        if (wrapped.getUseStampActivity() == null) {
            throw new ConfigException("Use stamp activity: missing value.");
        }
        return wrapped.getUseStampActivity();
    }

    @Override
    public Long getOverriddenSyncCounter() {
        return wrapped.getOverriddenSyncCounter();
    }

    @Override
    public String getOverriddenSyncFromCommit() {
        return wrapped.getOverriddenSyncFromCommit();
    }

    @Override
    public Boolean getUpdateVobViewDir() {
        if (wrapped.getUpdateVobViewDir() == null) {
            throw new ConfigException("Update VOB view directory: missing value.");
        }
        return wrapped.getUpdateVobViewDir();
    }

    @Override
    public File getVobViewDir() {
        final File dir = wrapped.getVobViewDir();
        if (dir == null) {
            throw new ConfigException("Vob view directory: missing property.");
        }
        if (!dir.exists()) {
            throw new ConfigException("Vob view directory: does not exist.");
        }
        if (!dir.isDirectory()) {
            throw new ConfigException("Vob view directory: not a directory.");
        }
        return dir;
    }

    public void validateAll() {
        if (getUseSyncActivity()) {
            getSyncActivityName();
        }
        getClearToolExec();
        getVobViewDir();
        getUpdateVobViewDir();
        getCommitStampAbsoluteFile();
        getCounterStampAbsoluteFile();
    }
}
