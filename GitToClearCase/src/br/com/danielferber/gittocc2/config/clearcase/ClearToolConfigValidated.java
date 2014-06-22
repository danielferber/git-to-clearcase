package br.com.danielferber.gittocc2.config.clearcase;

import br.com.danielferber.gittocc2.config.ConfigException;
import java.io.File;

/**
 *
 * @author Daniel
 */
public class ClearToolConfigValidated implements ClearToolConfigSource {

    private final ClearToolConfigSource wrapped;

    public ClearToolConfigValidated(ClearToolConfigSource wrapped) {
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
            throw new ConfigException("Git executable: missing property.");
        }
        if (!exec.exists()) {
            throw new ConfigException("Git executable: does not exist.");
        }
        if (!exec.isFile()) {
            throw new ConfigException("Git executable: not a file.");
        }
        if (!exec.canExecute()) {
            throw new ConfigException("Git executable: not executable.");
        }
        return exec;
    }


    @Override
    public File getCommitStampFile() {
        return wrapped.getCommitStampFile();
    }

    @Override
    public File getCounterStampFile() {
        return wrapped.getCounterStampFile();
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
    public Boolean getUpdateVobRoot() {
        return wrapped.getUpdateVobRoot();
    }

    @Override
    public File getVobViewDir() {
        File dir = getVobViewDir();
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
