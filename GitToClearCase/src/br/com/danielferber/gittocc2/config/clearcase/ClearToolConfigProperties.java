/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.config.clearcase;

import java.io.File;
import java.util.Map;
import java.util.Properties;

import br.com.danielferber.gittocc2.config.ConfigProperties;

public class ClearToolConfigProperties extends ClearToolConfigSourceImpl implements ClearToolConfig {

    public static final String VOB_VIEW_DIR_PROPERTY = "cc.vobview.dir";
    public static final String VOB_VIEW_UPDATE_PROPERTY = "cc.vobview.update";
    public static final String CLEAR_TOOL_EXEC_PROPERTY = "cc.cleartool.exec";
    public static final String USE_SYNC_ACTIVITY_PROPERTY = "cc.syncActivity.use";
    public static final String USE_STAMP_ACTIVITY_PROPERTY = "cc.stampActivity.use";
    public static final String SYNC_ACTIVITY_NAME_PROPERTY = "cc.syncActivity.name";
    public static final String STAMP_ACTIVITY_NAME_PROPERTY = "cc.stampActivity.name";
    public static final String USE_COMMIT_STAMP_FILE_PROPERTY = "cc.commitStamp.use";
    public static final String USE_COUNTER_STAMP_FILE_PROPERTY = "cc.counterStamp.use";
    public static final String COMMIT_STAMP_FILE_PROPERTY = "cc.commitStamp.file";
    public static final String COUNTER_STAMP_FILE_PROPERTY = "cc.counterStamp.file";
    public static final String OVERRIDDEN_SYNC_COUNTER_PROPERTY = "cc.commitStamp.override";
    public static final String OVERRIDDEN_SYNC_COMMIT_PROPERTY = "cc.counterStamp.override";

    private final ConfigProperties properties;

    public ClearToolConfigProperties(final ClearToolConfigSource other) {
        this.properties = new ConfigProperties();
        this.setClearToolExec(other.getClearToolExec());
        this.setVobViewDir(other.getVobViewDir());
        this.setUpdateVobRoot(other.getVobViewDirUpdate());
        
        this.setUseActivity(other.getUseActivity());
        this.setActivityName(other.getActivityName());
        
        this.setUseCommitStampFile(other.getUseCommitStampFile());
        this.setUseCounterStampFile(other.getUseCounterStampFile());
        this.setCommitStampFile(other.getCommitStampFile());
        this.setCounterStampFile(other.getCounterStampFile());
        this.setOverriddenSyncCounter(other.getOverriddenSyncCounter());
        this.setOverriddenSyncFromCommit(other.getOverriddenSyncFromCommit());
    }

    public ClearToolConfigProperties(final Map<String, String> map) {
        this(map, "");
    }

    public ClearToolConfigProperties(final Map<String, String> map, final String prefix) {
        this.properties = new ConfigProperties();
        this.properties.putAll(map);

    }

    public ClearToolConfigProperties(final Properties properties) {
        this(properties, "");
    }

    public ClearToolConfigProperties(final Properties properties, final String prefix) {
        this.properties = new ConfigProperties(properties);
    }

    @Override
    public String getActivityName() {
        return properties.getString(SYNC_ACTIVITY_NAME_PROPERTY);
    }

    @Override
    public File getClearToolExec() {
        return properties.getFile(CLEAR_TOOL_EXEC_PROPERTY);
    }

    @Override
    public File getCommitStampFile() {
        return properties.getFile(COMMIT_STAMP_FILE_PROPERTY);
    }

    @Override
    public File getCounterStampFile() {
        return properties.getFile(COUNTER_STAMP_FILE_PROPERTY);
    }

    @Override
    public Boolean getUseCommitStampFile() {
        return properties.getBoolean(USE_COMMIT_STAMP_FILE_PROPERTY);
    }

    @Override
    public Boolean getUseCounterStampFile() {
        return properties.getBoolean(USE_COUNTER_STAMP_FILE_PROPERTY);
    }

    @Override
    public Boolean getUseActivity() {
        return properties.getBoolean(USE_SYNC_ACTIVITY_PROPERTY);
    }

    @Override
    public Long getOverriddenSyncCounter() {
        return properties.getLong(OVERRIDDEN_SYNC_COUNTER_PROPERTY);
    }

    @Override
    public String getOverriddenSyncFromCommit() {
        return properties.getString(OVERRIDDEN_SYNC_COMMIT_PROPERTY);
    }

    public Properties getProperties() {
        return new Properties(properties);
    }

    @Override
    public Boolean getVobViewDirUpdate() {
        return properties.getBoolean(VOB_VIEW_UPDATE_PROPERTY);
    }

    @Override
    public File getVobViewDir() {
        return properties.getFile(VOB_VIEW_DIR_PROPERTY);
    }

    @Override
    public ClearToolConfig setActivityName(final String value) {
        properties.setString(SYNC_ACTIVITY_NAME_PROPERTY, value);
        return this;
    }

    @Override
    public ClearToolConfig setClearToolExec(final File file) {
        properties.setFile(CLEAR_TOOL_EXEC_PROPERTY, file);
        return this;
    }

    @Override
    public ClearToolConfig setCommitStampFile(final File file) {
        properties.setFile(COMMIT_STAMP_FILE_PROPERTY, file);
        return this;
    }

    @Override
    public ClearToolConfig setCounterStampFile(final File file) {
        properties.setFile(COUNTER_STAMP_FILE_PROPERTY, file);
        return this;
    }

    @Override
    public ClearToolConfig setUseCommitStampFile(final Boolean value) {
        properties.setBoolean(COMMIT_STAMP_FILE_PROPERTY, value);
        return this;
    }

    @Override
    public ClearToolConfig setUseCounterStampFile(final Boolean value) {
        properties.setBoolean(COUNTER_STAMP_FILE_PROPERTY, value);
        return this;
    }

    @Override
    public ClearToolConfig setUseActivity(final Boolean value) {
        properties.setBoolean("git.useSyncActivity", value);
        return this;
    }

    @Override
    public ClearToolConfig setOverriddenSyncCounter(final Long value) {
        properties.setLong("cc.overriddenSyncCounter", value);
        return this;
    }

    @Override
    public ClearToolConfig setOverriddenSyncFromCommit(final String value) {
        properties.setString("cc.overriddenSyncFromCommit", value);
        return this;
    }

    @Override
    public ClearToolConfig setUpdateVobRoot(final Boolean value) {
        properties.setBoolean("cc.updateVobRoot", value);
        return this;
    }

    @Override
    public ClearToolConfig setVobViewDir(final File dir) {
        properties.setFile("vobview.dir", dir);
        return this;
    }

    @Override
    public String toString() {
        return properties.toString();
    }
}
