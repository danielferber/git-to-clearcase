package br.com.danielferber.gittocc2.config.clearcase;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author Daniel
 */
public class ClearToolConfigPojo extends ClearToolConfigSourceImpl implements ClearToolConfig, Serializable {

    private File clearToolExec;
    private File vobViewDir;
    private Boolean updateVobRoot;
    private Boolean createActivity;
    private String activityMessagePattern;
    private File commitStampFileName;
    private File counterStampFileName;
    private Long overriddenSyncCounter;
    private String overriddenSyncFromCommit;

    public ClearToolConfigPojo() {
        super();
    }

    public ClearToolConfigPojo(ClearToolConfigSource other) {
        this.clearToolExec = other.getClearToolExec();
        this.vobViewDir = other.getVobViewDir();
        this.updateVobRoot = other.getUpdateVobRoot();
        this.createActivity = other.getCreateActivity();
        this.activityMessagePattern = other.getActivityMessagePattern();
        this.commitStampFileName = other.getCommitStampFileName();
        this.counterStampFileName = other.getCounterStampFileName();
        this.overriddenSyncCounter = other.getOverriddenSyncCounter();
        this.overriddenSyncFromCommit = other.getOverriddenSyncFromCommit();
    }

    public ClearToolConfigPojo(File clearToolExec, File vobViewDir) {
        this.clearToolExec = clearToolExec;
        this.vobViewDir = vobViewDir;
    }

    @Override
    public String getActivityMessagePattern() {
        return this.activityMessagePattern;
    }

    @Override
    public File getClearToolExec() {
        return clearToolExec;
    }

    @Override
    public File getCommitStampFileName() {
        return commitStampFileName;
    }

    @Override
    public File getCounterStampFileName() {
        return counterStampFileName;
    }

    @Override
    public Boolean getCreateActivity() {
        return this.createActivity;
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
    public Boolean getUpdateVobRoot() {
        return this.updateVobRoot;
    }

    @Override
    public File getVobViewDir() {
        return vobViewDir;
    }

    @Override
    public ClearToolConfig setActivityMessagePattern(String value) {
        this.activityMessagePattern = value;
        return this;
    }

    @Override
    public ClearToolConfig setClearToolExec(File file) {
        this.clearToolExec = file;
        return this;
    }

    @Override
    public ClearToolConfig setCommitStampFileName(File file) {
        this.commitStampFileName = file;
        return this;
    }

    @Override
    public ClearToolConfig setCounterStampFileName(File file) {
        this.counterStampFileName = file;
        return this;
    }

    @Override
    public ClearToolConfig setCreateActivity(Boolean value) {
        this.createActivity = value;
        return this;
    }

    @Override
    public ClearToolConfig setOverriddenSyncCounter(Long value) {
        this.overriddenSyncCounter = value;
        return this;
    }

    @Override
    public ClearToolConfig setOverriddenSyncFromCommit(String value) {
        this.overriddenSyncFromCommit = value;
        return this;
    }

    @Override
    public ClearToolConfig setUpdateVobRoot(Boolean value) {
        this.updateVobRoot = value;
        return this;
    }

    @Override
    public ClearToolConfig setVobViewDir(File dir) {
        this.vobViewDir = dir;
        return this;
    }

}
