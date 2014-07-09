package br.com.danielferber.gittocc2.config.clearcase;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author Daniel
 */
public class ClearToolConfigPojo extends ClearToolConfigSourceImpl implements ClearToolConfig, Serializable {

    private static final long serialVersionUID = 1L;

    private File clearToolExec;
    private File vobViewDir;
    private Boolean updateVobRoot;
    private Boolean useSyncActivity;
    private Boolean useStampActivity;
    private String syncActivityName;
    private String stampActivityName;
    private Boolean useCommitStampFile;
    private Boolean useCounterStampFile;
    private File commitStampFileName;
    private File counterStampFileName;
    private Long overriddenSyncCounter;
    private String overriddenSyncFromCommit;

    public ClearToolConfigPojo() {
        super();
    }

    public ClearToolConfigPojo(final ClearToolConfigSource other) {
        this.clearToolExec = other.getClearToolExec();
        this.vobViewDir = other.getVobViewDir();
        this.updateVobRoot = other.getVobViewDirUpdate();
        this.useSyncActivity = other.getUseSyncActivity();
        this.useStampActivity = other.getUseStampActivity();
        this.syncActivityName = other.getSyncActivityName();
        this.stampActivityName = other.getStampActivityName();
        this.useCommitStampFile = other.getUseCommitStampFile();
        this.useCounterStampFile = other.getUseCounterStampFile();
        this.commitStampFileName = other.getCommitStampFile();
        this.counterStampFileName = other.getCounterStampFile();
        this.overriddenSyncCounter = other.getOverriddenSyncCounter();
        this.overriddenSyncFromCommit = other.getOverriddenSyncFromCommit();
    }

    public ClearToolConfigPojo(final File clearToolExec, final File vobViewDir) {
        this.clearToolExec = clearToolExec;
        this.vobViewDir = vobViewDir;
    }

    @Override
    public String getSyncActivityName() {
        return this.syncActivityName;
    }

    @Override
    public String getStampActivityName() {
        return this.stampActivityName;
    }

    @Override
    public File getClearToolExec() {
        return clearToolExec;
    }

    @Override
    public File getCommitStampFile() {
        return commitStampFileName;
    }

    @Override
    public File getCounterStampFile() {
        return counterStampFileName;
    }

    @Override
    public Boolean getUseSyncActivity() {
        return this.useSyncActivity;
    }

    @Override
    public Boolean getUseStampActivity() {
        return this.useStampActivity;
    }

    @Override
    public Long getOverriddenSyncCounter() {
        return overriddenSyncCounter;
    }

    @Override
    public String getOverriddenSyncFromCommit() {
        return overriddenSyncFromCommit;
    }

    @Override
    public Boolean getVobViewDirUpdate() {
        return this.updateVobRoot;
    }

    @Override
    public File getVobViewDir() {
        return vobViewDir;
    }

    @Override
    public Boolean getUseCommitStampFile() {
        return useCommitStampFile;
    }

    @Override
    public Boolean getUseCounterStampFile() {
        return useCounterStampFile;
    }

    @Override
    public ClearToolConfig setSyncActivityName(final String value) {
        this.syncActivityName = value;
        return this;
    }

    @Override
    public ClearToolConfig setStampActivityName(final String value) {
        this.stampActivityName = value;
        return this;
    }

    @Override
    public ClearToolConfig setClearToolExec(final File file) {
        this.clearToolExec = file;
        return this;
    }

    @Override
    public ClearToolConfig setCommitStampFile(final File file) {
        this.commitStampFileName = file;
        return this;
    }

    @Override
    public ClearToolConfig setCounterStampFile(final File file) {
        this.counterStampFileName = file;
        return this;
    }

    @Override
    public ClearToolConfig setUseSyncActivity(final Boolean value) {
        this.useSyncActivity = value;
        return this;
    }

    @Override
    public ClearToolConfig setUseStampActivity(final Boolean value) {
        this.useStampActivity = value;
        return this;
    }

    @Override
    public ClearToolConfig setOverriddenSyncCounter(final Long value) {
        this.overriddenSyncCounter = value;
        return this;
    }

    @Override
    public ClearToolConfig setOverriddenSyncFromCommit(final String value) {
        this.overriddenSyncFromCommit = value;
        return this;
    }

    @Override
    public ClearToolConfig setUpdateVobRoot(final Boolean value) {
        this.updateVobRoot = value;
        return this;
    }

    @Override
    public ClearToolConfig setVobViewDir(final File dir) {
        this.vobViewDir = dir;
        return this;
    }

    @Override
    public ClearToolConfigPojo setUseCommitStampFile(Boolean value) {
        this.useCommitStampFile = value;
        return this;
    }

    @Override
    public ClearToolConfigPojo setUseCounterStampFile(Boolean value) {
        this.useCounterStampFile = value;
        return this;
    }

}
