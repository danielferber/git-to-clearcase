package br.com.danielferber.gittocc2.config.sync;

import java.io.File;

/**
 *
 * @author Daniel Felix Ferber
 */
public class SyncConfigPojo implements SyncConfig {

    private Boolean updateVobRoot;
    private Boolean fetchRemoteGitRepository;
    private Boolean fastForwardLocalGitRepository;
    private Boolean resetLocationGitRepository;
    private Boolean cleanLocalGitRepository;
    private Boolean createActivity;
    private String activityMessagePattern;
    private File commitStampFile;
    private File counterStampFile;
    private Long overriddenSyncCounter;
    private String overriddenSyncFromCommit;

    public SyncConfigPojo() {
        super();
    }

    public SyncConfigPojo(SyncConfigSource other) {
        updateVobRoot = other.getUpdateVobRoot();
        fetchRemoteGitRepository = other.getFetchRemoteGitRepository();
        fastForwardLocalGitRepository = other.getFastForwardLocalGitRepository();
        resetLocationGitRepository = other.getResetLocalGitRepository();
        cleanLocalGitRepository = other.getCleanLocalGitRepository();
        createActivity = other.getCreateActivity();
        activityMessagePattern = other.getActivityMessagePattern();
        commitStampFile = other.getCommitStampFile();
        counterStampFile = other.getCounterStampFile();
        overriddenSyncCounter = other.getOverriddenSyncCounter();
        overriddenSyncFromCommit = other.getOverriddenSyncFromCommit();
    }

    @Override
    public SyncConfig setUpdateVobRoot(Boolean value) {
        this.updateVobRoot = value;
        return this;
    }

    @Override
    public SyncConfig setFetchRemoteGitRepository(Boolean value) {
        this.fetchRemoteGitRepository = value;
        return this;
    }

    @Override
    public SyncConfig setFastForwardLocalGitRepository(Boolean value) {
        this.fastForwardLocalGitRepository = value;
        return this;
    }

    @Override
    public SyncConfig setResetLocalGitRepository(Boolean value) {
        this.resetLocationGitRepository = value;
        return this;
    }

    @Override
    public SyncConfig setCleanLocalGitRepository(Boolean value) {
        this.cleanLocalGitRepository = value;
        return this;
    }

    @Override
    public SyncConfig setCreateActivity(Boolean value) {
        this.createActivity = value;
        return this;
    }

    @Override
    public SyncConfig setActivityMessagePattern(String value) {
        this.activityMessagePattern = value;
        return this;
    }

    @Override
    public SyncConfig setCommitStampFile(File file) {
        this.commitStampFile = file;
        return this;
    }

    @Override
    public SyncConfig setCounterStampFile(File file) {
        this.counterStampFile = file;
        return this;
    }

    @Override
    public SyncConfig setOverriddenSyncCounter(Long value) {
        this.overriddenSyncCounter = value;
        return this;
    }

    @Override
    public SyncConfig setOverriddenSyncFromCommit(String value) {
        this.overriddenSyncFromCommit = value;
        return this;
    }

    @Override
    public Boolean getUpdateVobRoot() {
        return this.updateVobRoot;
    }

    @Override
    public Boolean getFetchRemoteGitRepository() {
        return this.fetchRemoteGitRepository;
    }

    @Override
    public Boolean getFastForwardLocalGitRepository() {
        return this.fastForwardLocalGitRepository;
    }

    @Override
    public Boolean getResetLocalGitRepository() {
        return this.resetLocationGitRepository;
    }

    @Override
    public Boolean getCleanLocalGitRepository() {
        return this.cleanLocalGitRepository;
    }

    @Override
    public Boolean getCreateActivity() {
        return this.createActivity;
    }

    @Override
    public String getActivityMessagePattern() {
        return this.activityMessagePattern;
    }

    @Override
    public File getCommitStampFile() {
        return commitStampFile;
    }

    @Override
    public File getCounterStampFile() {
        return counterStampFile;
    }

    @Override
    public Long getOverriddenSyncCounter() {
        return overriddenSyncCounter;
    }

    @Override
    public String getOverriddenSyncFromCommit() {
        return overriddenSyncFromCommit;
    }
}
