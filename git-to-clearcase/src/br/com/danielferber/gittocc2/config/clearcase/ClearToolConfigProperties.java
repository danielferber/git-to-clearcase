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

    public static final String PROP_VOB_VIEW_DIR = "vobview.dir";
    public static final String PROP_VOB_VIEW_UPDATE = "vobview.update";
    public static final String PROP_CLEAR_TOOL_EXEC = "cleartool.exec";
    public static final String PROP_USE_ACTIVITY = "activity.use";
    public static final String PROP_ACTIVITY_NAME = "activity.name";
    public static final String PROP_USE_COMMIT_STAMP_FILE = "stamp.commit.use";
    public static final String PROP_USE_COUNTER_STAMP_FILE = "stamp.counter.use";
    public static final String PROP_CHECK_CHECKOUT_FORGOTTEN = "check.checkout.forgotten";
    public static final String PROP_COMMIT_STAMP_FILE = "stamp.commit.file";
    public static final String PROP_COUNTER_STAMP_FILE = "stamp.counter.file";
    public static final String PROP_OVERRIDDEN_SYNC_COUNTER = "stamp.commit.override";
    public static final String PROP_OVERRIDDEN_SYNC_COMMIT = "stamp.counter.override";
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
        return properties.getString(PROP_ACTIVITY_NAME);
    }

    @Override
    public File getClearToolExec() {
        return properties.getFile(PROP_CLEAR_TOOL_EXEC);
    }

    @Override
    public File getCommitStampFile() {
        return properties.getFile(PROP_COMMIT_STAMP_FILE);
    }

    @Override
    public File getCounterStampFile() {
        return properties.getFile(PROP_COUNTER_STAMP_FILE);
    }

    @Override
    public Boolean getUseCommitStampFile() {
        return properties.getBoolean(PROP_USE_COMMIT_STAMP_FILE);
    }

    @Override
    public Boolean getUseCounterStampFile() {
        return properties.getBoolean(PROP_USE_COUNTER_STAMP_FILE);
    }

    @Override
    public Boolean getUseActivity() {
        return properties.getBoolean(PROP_USE_ACTIVITY);
    }

    @Override
    public Long getOverriddenSyncCounter() {
        return properties.getLong(PROP_OVERRIDDEN_SYNC_COUNTER);
    }

    @Override
    public String getOverriddenSyncFromCommit() {
        return properties.getString(PROP_OVERRIDDEN_SYNC_COMMIT);
    }

    public Properties getProperties() {
        return new Properties(properties);
    }

    @Override
    public Boolean getVobViewDirUpdate() {
        return properties.getBoolean(PROP_VOB_VIEW_UPDATE);
    }

    @Override
    public File getVobViewDir() {
        return properties.getFile(PROP_VOB_VIEW_DIR);
    }

    @Override
    public ClearToolConfig setActivityName(final String value) {
        properties.setString(PROP_ACTIVITY_NAME, value);
        return this;
    }

    @Override
    public ClearToolConfig setClearToolExec(final File file) {
        properties.setFile(PROP_CLEAR_TOOL_EXEC, file);
        return this;
    }

    @Override
    public ClearToolConfig setCommitStampFile(final File file) {
        properties.setFile(PROP_COMMIT_STAMP_FILE, file);
        return this;
    }

    @Override
    public ClearToolConfig setCounterStampFile(final File file) {
        properties.setFile(PROP_COUNTER_STAMP_FILE, file);
        return this;
    }

    @Override
    public ClearToolConfig setUseCommitStampFile(final Boolean value) {
        properties.setBoolean(PROP_COMMIT_STAMP_FILE, value);
        return this;
    }

    @Override
    public ClearToolConfig setUseCounterStampFile(final Boolean value) {
        properties.setBoolean(PROP_COUNTER_STAMP_FILE, value);
        return this;
    }

    @Override
    public ClearToolConfig setUseActivity(final Boolean value) {
        properties.setBoolean(PROP_USE_ACTIVITY, value);
        return this;
    }

    @Override
    public ClearToolConfig setOverriddenSyncCounter(final Long value) {
        properties.setLong(PROP_OVERRIDDEN_SYNC_COUNTER, value);
        return this;
    }

    @Override
    public ClearToolConfig setOverriddenSyncFromCommit(final String value) {
        properties.setString(PROP_OVERRIDDEN_SYNC_COMMIT, value);
        return this;
    }

    @Override
    public ClearToolConfig setUpdateVobRoot(final Boolean value) {
        properties.setBoolean(PROP_VOB_VIEW_UPDATE, value);
        return this;
    }

    @Override
    public ClearToolConfig setVobViewDir(final File dir) {
        properties.setFile(PROP_VOB_VIEW_DIR, dir);
        return this;
    }

    @Override
    public Boolean getCheckForgottenCheckout() {
        return properties.getBoolean(PROP_CHECK_CHECKOUT_FORGOTTEN);
    }

    @Override
    public ClearToolConfig setCheckForgottenCheckout(Boolean value) {
        properties.setBoolean(PROP_CHECK_CHECKOUT_FORGOTTEN, value);
        return this;
    }

    @Override
    public String toString() {
        return properties.toString();
    }
}
