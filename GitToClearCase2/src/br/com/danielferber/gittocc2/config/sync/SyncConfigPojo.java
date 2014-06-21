package br.com.danielferber.gittocc2.config.sync;

public class SyncConfigPojo implements SyncConfig {

    private boolean updateVobRoot;
    private boolean fetchRemoteGitRepository;
    private boolean fastForwardLocalGitRepository;
    private boolean resetLocationGitRepository;
    private boolean cleanLocalGitRepository;
    private boolean createActivity;
    private String activityMessagePattern;

    @Override
    public SyncConfig setUpdateVobRoot(boolean value) {
        this.updateVobRoot = value;
        return this;
    }

    @Override
    public SyncConfig setFetchRemoteGitRepository(boolean value) {
        this.fetchRemoteGitRepository = value;
        return this;
    }

    @Override
    public SyncConfig setFastForwardLocalGitRepository(boolean value) {
        this.fastForwardLocalGitRepository = value;
        return this;
    }

    @Override
    public SyncConfig setResetLocalGitRepository(boolean value) {
        this.resetLocationGitRepository = value;
        return this;
    }

    @Override
    public SyncConfig setCleanLocalGitRepository(boolean value) {
        this.cleanLocalGitRepository = value;
        return this;
    }

    @Override
    public SyncConfig setCreateActivity(boolean value) {
        this.createActivity = value;
        return this;
    }

    @Override
    public SyncConfig setActivityMessagePattern(String value) {
        this.activityMessagePattern = value;
        return this;
    }

    @Override
    public boolean getUpdateVobRoot() {
        return this.updateVobRoot;
    }

    @Override
    public boolean getFetchRemoteGitRepository() {
        return this.fetchRemoteGitRepository;
    }

    @Override
    public boolean getFastForwardLocalGitRepository() {
        return this.fastForwardLocalGitRepository;
    }

    @Override
    public boolean getResetLocalGitRepository() {
        return this.resetLocationGitRepository;
    }

    @Override
    public boolean getCleanLocalGitRepository() {
        return this.cleanLocalGitRepository;
    }

    @Override
    public boolean getCreateActivity() {
        return this.createActivity;
    }

    @Override
    public String getActivityMessagePattern() {
        return this.activityMessagePattern;
    }
}
