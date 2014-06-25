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
    public String getActivityMessagePattern() {
        return wrapped.getActivityMessagePattern();
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
        return wrapped.getCommitStampFileName();
    }

    @Override
    public File getCounterStampFileName() {
        return wrapped.getCounterStampFileName();
    }

    @Override
    public Boolean getCreateActivity() {
        return wrapped.getCreateActivity();
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
    public Boolean getupdateVobViewDir() {
        return wrapped.getupdateVobViewDir();
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

}
