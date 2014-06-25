package br.com.danielferber.gittocc2.config.clearcase;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

public class ClearToolConfigBean extends ClearToolConfigSourceImpl implements ClearToolConfig {

    private final ClearToolConfig wrapped;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public static final String CREATE_ACTIVITY_PROPERTY = "cc.createActivity";
    public static final String CLEARTOOL_EXEC_PROPERTY = "cc.cleartool.exec";
    public static final String VOB_VIEW_DIR_PROPERTY = "cc.view.dir";
    public static final String ACTIVITY_MESSAGE_PATTERN_PROPERTY = "cc.activityMessagePattern";
    public static final String UPDATE_VOB_ROOT_PROPERTY = "cc.updateVobRoot";
    public static final String COMMIT_STAMP_FILE_NAME_PROPERTY = "cc.commitStampFileName";
    public static final String COUNTER_STAMP_FILE_NAME_PROPERTY = "cc.counterStampFileName";
    public static final String OVERRIDDEN_SYNC_COUNTER = "cc.overriddenSyncCounter";
    public static final String OVERRIDDEN_SYNC_FROM_COMMIT = "cc.overridenSyncFromCommit";

    public ClearToolConfigBean() {
        super();
        this.wrapped = new ClearToolConfigPojo();
    }

    public ClearToolConfigBean(final ClearToolConfig other) {
        this.wrapped = other;
    }

    public void addPropertyChangeListener(final PropertyChangeListener listener) {
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
        return wrapped.getClearToolExec();
    }

    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    @Override
    public ClearToolConfig setActivityMessagePattern(final String value) {
        final String oldValue = wrapped.getActivityMessagePattern();
        wrapped.setActivityMessagePattern(value);
        this.pcs.firePropertyChange(ACTIVITY_MESSAGE_PATTERN_PROPERTY, oldValue, value);
        return this;
    }

    @Override
    public ClearToolConfig setClearToolExec(final File file) {
        final File oldValue = wrapped.getClearToolExec();
        wrapped.setClearToolExec(file);
        this.pcs.firePropertyChange(CLEARTOOL_EXEC_PROPERTY, oldValue, file);
        return this;
    }

    @Override
    public ClearToolConfig setCommitStampFileName(final File file) {
        final File oldValue = wrapped.getCommitStampFileName();
        wrapped.setCommitStampFileName(file);
        this.pcs.firePropertyChange(COMMIT_STAMP_FILE_NAME_PROPERTY, oldValue, file);
        return this;
    }

        @Override
		public ClearToolConfig setCounterStampFileName(final File file) {
		    final File oldValue = wrapped.getCounterStampFileName();
		    wrapped.setCounterStampFileName(file);
		    this.pcs.firePropertyChange(COUNTER_STAMP_FILE_NAME_PROPERTY, oldValue, file);
		    return this;
		}

    @Override
   public ClearToolConfig setCreateActivity(final Boolean value) {
	final Boolean oldValue = wrapped.getCreateActivity();
	wrapped.setCreateActivity(value);
	this.pcs.firePropertyChange(CREATE_ACTIVITY_PROPERTY, oldValue, value);
	return this;
   }

    @Override
    public ClearToolConfig setOverriddenSyncCounter(final Long value) {
        final Long oldValue = wrapped.getOverriddenSyncCounter();
        wrapped.setOverriddenSyncCounter(value);
        this.pcs.firePropertyChange(OVERRIDDEN_SYNC_COUNTER, oldValue, value);
        return this;
    }

    @Override
    public ClearToolConfig setOverriddenSyncFromCommit(final String value) {
        final String oldValue = wrapped.getOverriddenSyncFromCommit();
        wrapped.setOverriddenSyncFromCommit(value);
        this.pcs.firePropertyChange(OVERRIDDEN_SYNC_FROM_COMMIT, oldValue, value);
        return this;
    }

    @Override
    public ClearToolConfig setUpdateVobRoot(final Boolean value) {
        final Boolean oldValue = wrapped.getupdateVobViewDir();
        wrapped.setUpdateVobRoot(value);
        this.pcs.firePropertyChange(UPDATE_VOB_ROOT_PROPERTY, oldValue, value);
        return this;
    }

    @Override
    public ClearToolConfig setVobViewDir(final File dir) {
        final File oldValue = wrapped.getVobViewDir();
        wrapped.setVobViewDir(dir);
        this.pcs.firePropertyChange(VOB_VIEW_DIR_PROPERTY, oldValue, dir);
        return this;
    }

}
