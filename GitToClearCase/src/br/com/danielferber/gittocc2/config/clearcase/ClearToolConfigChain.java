package br.com.danielferber.gittocc2.config.clearcase;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class ClearToolConfigChain extends ClearToolConfigSourceImpl implements ClearToolConfigSource {

    private final List<ClearToolConfigSource> sources;

    public ClearToolConfigChain(final ClearToolConfigSource... sources) {
        this.sources = new ArrayList<>(Arrays.asList(sources));
    }

    public void add(ClearToolConfigSource source) {
        this.add(source);
    }

    @Override
    public String getActivityMessagePattern() {
        for (ClearToolConfigSource config : sources) {
            String value = config.getActivityMessagePattern();
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public File getClearToolExec() {
        for (ClearToolConfigSource config : sources) {
            File value = config.getClearToolExec();
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public File getCommitStampFileName() {
        for (ClearToolConfigSource config : sources) {
            File value = config.getCommitStampFileName();
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public File getCounterStampFileName() {
        for (ClearToolConfigSource config : sources) {
            File value = config.getCounterStampFileName();
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public Boolean getCreateActivity() {
        for (ClearToolConfigSource config : sources) {
            Boolean value = config.getCreateActivity();
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public Long getOverriddenSyncCounter() {
        for (ClearToolConfigSource config : sources) {
            Long value = config.getOverriddenSyncCounter();
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public String getOverriddenSyncFromCommit() {
        for (ClearToolConfigSource config : sources) {
            String value = config.getOverriddenSyncFromCommit();
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public Boolean getUpdateVobViewDir() {
        for (ClearToolConfigSource config : sources) {
            Boolean value = config.getUpdateVobViewDir();
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public File getVobViewDir() {
        for (ClearToolConfigSource config : sources) {
            File value = config.getVobViewDir();
            if (value != null) {
                return value;
            }
        }
        return null;
    }
}
