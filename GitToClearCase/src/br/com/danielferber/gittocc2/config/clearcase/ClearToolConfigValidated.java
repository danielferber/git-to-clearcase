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
    public String getActivityName() {
        if (wrapped.getActivityName() == null) {
            throw new ConfigException("Sync activity name: missing value.");
        }
        return wrapped.getActivityName();
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
    public File getCommitStampFile() {
        if (wrapped.getCommitStampFile() == null) {
            throw new ConfigException("Commit stamp file name: missing value.");
        }
        return wrapped.getCommitStampFile();
    }

    @Override
    public File getCounterStampFile() {
        if (wrapped.getCounterStampFile() == null) {
            throw new ConfigException("counter stamp file name: missing value.");
        }
        return wrapped.getCounterStampFile();
    }

    @Override
    public Boolean getUseCommitStampFile() {
        if (wrapped.getCommitStampFile() == null) {
            throw new ConfigException("Use commit stamp file: missing value.");
        }
        return wrapped.getUseCommitStampFile();
    }

    @Override
    public Boolean getUseCounterStampFile() {
        if (wrapped.getCounterStampFile() == null) {
            throw new ConfigException("Use counter stamp file: missing value.");
        }
        return wrapped.getUseCounterStampFile();
    }

    @Override
    public Boolean getUseActivity() {
        if (wrapped.getUseActivity() == null) {
            throw new ConfigException("Use sync activity: missing value.");
        }
        return wrapped.getUseActivity();
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
    public Boolean getVobViewDirUpdate() {
        if (wrapped.getVobViewDirUpdate() == null) {
            throw new ConfigException("Update VOB view directory: missing value.");
        }
        return wrapped.getVobViewDirUpdate();
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
        if (getUseActivity()) {
            getActivityName();
        }
        getClearToolExec();
        getVobViewDir();
        getVobViewDirUpdate();
        getCommitStampAbsoluteFile();
        getCounterStampAbsoluteFile();
    }
}
