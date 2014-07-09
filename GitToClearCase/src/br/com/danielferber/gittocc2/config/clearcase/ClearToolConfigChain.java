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
    public String getSyncActivityName() {
        for (ClearToolConfigSource config : sources) {
            String value = config.getSyncActivityName();
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public String getStampActivityName() {
        for (ClearToolConfigSource config : sources) {
            String value = config.getStampActivityName();
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
    public File getCommitStampFile() {
        for (ClearToolConfigSource config : sources) {
            File value = config.getCommitStampFile();
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public File getCounterStampFile() {
        for (ClearToolConfigSource config : sources) {
            File value = config.getCounterStampFile();
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public Boolean getUseSyncActivity() {
        for (ClearToolConfigSource config : sources) {
            Boolean value = config.getUseSyncActivity();
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public Boolean getUseStampActivity() {
        for (ClearToolConfigSource config : sources) {
            Boolean value = config.getUseStampActivity();
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
    public Boolean getVobViewDirUpdate() {
        for (ClearToolConfigSource config : sources) {
            Boolean value = config.getVobViewDirUpdate();
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

    @Override
    public Boolean getUseCommitStampFile() {
        for (ClearToolConfigSource config : sources) {
            Boolean value = config.getUseCommitStampFile();
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public Boolean getUseCounterStampFile() {
        for (ClearToolConfigSource config : sources) {
            Boolean value = config.getUseCounterStampFile();
            if (value != null) {
                return value;
            }
        }
        return null;
    }

}
