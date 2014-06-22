package br.com.danielferber.gittocc2.config.clearcase;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

public class ClearToolConfigBean implements ClearToolConfig {

    private final ClearToolConfig wrapped;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public static final String CREATE_ACTIVITY_PROPERTY = "cc.createActivity";
    public static final String CLEARTOOL_EXEC_PROPERTY = "cc.cleartool.exec";
    public static final String VOB_VIEW_DIR_PROPERTY = "cc.view.dir";
    public static final String ACTIVITY_MESSAGE_PATTERN_PROPERTY = "cc.activityMessagePattern";
    public static final String UPDATE_VOB_ROOT_PROPERTY = "cc.updateVobRoot";
    public static final String COMMIT_STAMP_FILE_PROPERTY = "cc.commitStampFile";
    public static final String COUNTER_STAMP_FILE_PROPERTY = "cc.counterStampFile";
    public static final String OVERRIDDEN_SYNC_COUNTER = "cc.overriddenSyncCounter";
    public static final String OVERRIDDEN_SYNC_FROM_COMMIT = "cc.overridenSyncFromCommit";

    public ClearToolConfigBean() {
        super();
        this.wrapped = new ClearToolConfigPojo();
    }

    public ClearToolConfigBean(ClearToolConfig other) {
        this.wrapped = other;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    @Override
    public String getActivityMessagePattern() {
        return wrapped.getActivityMessagePattern();
    }

    @Override
    public File getClearToolExec() {
        return wrapped.getClearToolExec();
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
        return wrapped.getClearToolExec();
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    @Override
    public ClearToolConfig setActivityMessagePattern(String value) {
        final String oldValue = wrapped.getActivityMessagePattern();
        wrapped.setActivityMessagePattern(value);
        this.pcs.firePropertyChange(ACTIVITY_MESSAGE_PATTERN_PROPERTY, oldValue, value);
        return this;
    }

    @Override
    public ClearToolConfig setClearToolExec(File file) {
        final File oldValue = wrapped.getClearToolExec();
        wrapped.setClearToolExec(file);
        this.pcs.firePropertyChange(CLEARTOOL_EXEC_PROPERTY, oldValue, file);
        return this;
    }

    @Override
    public ClearToolConfig setCommitStampFile(File file) {
        final File oldValue = wrapped.getCommitStampFile();
        wrapped.setCommitStampFile(file);
        this.pcs.firePropertyChange(COMMIT_STAMP_FILE_PROPERTY, oldValue, file);
        return this;
    }
    
        @Override
		public ClearToolConfig setCounterStampFile(File file) {
		    final File oldValue = wrapped.getCounterStampFile();
		    wrapped.setCounterStampFile(file);
		    this.pcs.firePropertyChange(COUNTER_STAMP_FILE_PROPERTY, oldValue, file);
		    return this;
		}

    @Override
   public ClearToolConfig setCreateActivity(Boolean value) {
	final Boolean oldValue = wrapped.getCreateActivity();
	wrapped.setCreateActivity(value);
	this.pcs.firePropertyChange(CREATE_ACTIVITY_PROPERTY, oldValue, value);
	return this;
   }

    @Override
    public ClearToolConfig setOverriddenSyncCounter(Long value) {
        Long oldValue = wrapped.getOverriddenSyncCounter();
        wrapped.setOverriddenSyncCounter(value);
        this.pcs.firePropertyChange(OVERRIDDEN_SYNC_COUNTER, oldValue, value);
        return this;
    }

    @Override
    public ClearToolConfig setOverriddenSyncFromCommit(String value) {
        String oldValue = wrapped.getOverriddenSyncFromCommit();
        wrapped.setOverriddenSyncFromCommit(value);
        this.pcs.firePropertyChange(OVERRIDDEN_SYNC_FROM_COMMIT, oldValue, value);
        return this;
    }

    @Override
    public ClearToolConfig setUpdateVobRoot(Boolean value) {
        final Boolean oldValue = wrapped.getUpdateVobRoot();
        wrapped.setUpdateVobRoot(value);
        this.pcs.firePropertyChange(UPDATE_VOB_ROOT_PROPERTY, oldValue, value);
        return this;
    }

    @Override
    public ClearToolConfig setVobViewDir(File dir) {
        final File oldValue = wrapped.getVobViewDir();
        wrapped.setVobViewDir(dir);
        this.pcs.firePropertyChange(VOB_VIEW_DIR_PROPERTY, oldValue, dir);
        return this;
    }

}
