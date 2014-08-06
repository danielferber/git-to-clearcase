package br.com.danielferber.gittocc2.config.clearcase;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

public class ClearToolConfigBean extends ClearToolConfigSourceImpl implements ClearToolConfig {

    private final ClearToolConfig wrapped;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    public static final String USE_SYNC_ACTIVITY_PROPERTY = "cc.useSyncActivity";
    public static final String USE_STAMP_ACTIVITY_PROPERTY = "cc.useStampActivity";
    public static final String CLEARTOOL_EXEC_PROPERTY = "cc.cleartool.exec";
    public static final String VOB_VIEW_DIR_PROPERTY = "cc.view.dir";
    public static final String SYNC_ACTIVITY_NAME_PROPERTY = "cc.syncActivityName";
    public static final String STAMP_ACTIVITY_NAME_PROPERTY = "cc.stampActivityName";
    public static final String UPDATE_VOB_ROOT_PROPERTY = "cc.updateVobRoot";
    public static final String COMMIT_STAMP_FILE_NAME_PROPERTY = "cc.commitStampFileName";
    public static final String COUNTER_STAMP_FILE_NAME_PROPERTY = "cc.counterStampFileName";
    public static final String USE_COMMIT_STAMP_FILE_PROPERTY = "cc.useCommitStampFile";
    public static final String USE_COUNTER_STAMP_FILE_PROPERTY = "cc.useCounterStampFile";
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
    public String getActivityName() {
        return wrapped.getActivityName();
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
    public Boolean getUseCommitStampFile() {
        return wrapped.getUseCommitStampFile();
    }

    @Override
    public Boolean getUseCounterStampFile() {
        return wrapped.getUseCounterStampFile();
    }

    @Override
    public Boolean getUseActivity() {
        return wrapped.getUseActivity();
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
    public Boolean getVobViewDirUpdate() {
        return wrapped.getVobViewDirUpdate();
    }

    @Override
    public File getVobViewDir() {
        return wrapped.getClearToolExec();
    }

    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    @Override
    public ClearToolConfig setActivityName(final String value) {
        final String oldValue = wrapped.getActivityName();
        wrapped.setActivityName(value);
        this.pcs.firePropertyChange(SYNC_ACTIVITY_NAME_PROPERTY, oldValue, value);
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
    public ClearToolConfig setCommitStampFile(final File file) {
        final File oldValue = wrapped.getCommitStampFile();
        wrapped.setCommitStampFile(file);
        this.pcs.firePropertyChange(COMMIT_STAMP_FILE_NAME_PROPERTY, oldValue, file);
        return this;
    }

    @Override
    public ClearToolConfig setCounterStampFile(final File file) {
        final File oldValue = wrapped.getCounterStampFile();
        wrapped.setCounterStampFile(file);
        this.pcs.firePropertyChange(COUNTER_STAMP_FILE_NAME_PROPERTY, oldValue, file);
        return this;
    }

    @Override
    public ClearToolConfig setUseCommitStampFile(final Boolean value) {
        final Boolean oldValue = wrapped.getUseCommitStampFile();
        wrapped.setUseCommitStampFile(value);
        this.pcs.firePropertyChange(USE_COMMIT_STAMP_FILE_PROPERTY, oldValue, value);
        return this;
    }

    @Override
    public ClearToolConfig setUseCounterStampFile(final Boolean value) {
        final Boolean oldValue = wrapped.getUseCounterStampFile();
        wrapped.setUseCounterStampFile(value);
        this.pcs.firePropertyChange(USE_COUNTER_STAMP_FILE_PROPERTY, oldValue, value);
        return this;
    }

    @Override
    public ClearToolConfig setUseActivity(final Boolean value) {
        final Boolean oldValue = wrapped.getUseActivity();
        wrapped.setUseActivity(value);
        this.pcs.firePropertyChange(USE_SYNC_ACTIVITY_PROPERTY, oldValue, value);
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
        final Boolean oldValue = wrapped.getVobViewDirUpdate();
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
