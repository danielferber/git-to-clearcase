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

    private final ConfigProperties properties;
    private final String prefix;

    public ClearToolConfigProperties(final ClearToolConfigSource other) {
        this(other, "");
    }

    public ClearToolConfigProperties(final ClearToolConfigSource other, final String prefix) {
        this.properties = new ConfigProperties();
        this.prefix = prefix;
        this.setClearToolExec(other.getClearToolExec());
        this.setVobViewDir(other.getVobViewDir());
        this.setActivityMessagePattern(other.getActivityMessagePattern());
        this.setCreateActivity(other.getCreateActivity());
        this.setUpdateVobRoot(other.getUpdateVobRoot());
        this.setCommitStampFileName(other.getCommitStampFileName());
        this.setCounterStampFileName(other.getCounterStampFileName());
        this.setOverriddenSyncCounter(other.getOverriddenSyncCounter());
        this.setOverriddenSyncFromCommit(other.getOverriddenSyncFromCommit());
    }

    public ClearToolConfigProperties(final Map<String, String> map) {
        this(map, "");
    }

    public ClearToolConfigProperties(final Map<String, String> map, final String prefix) {
        this.properties = new ConfigProperties();
        this.properties.putAll(map);
        this.prefix = prefix;

    }

    public ClearToolConfigProperties(final Properties properties) {
        this(properties, "");
    }

    public ClearToolConfigProperties(final Properties properties, final String prefix) {
        this.properties = new ConfigProperties(properties);
        this.prefix = prefix;
    }

    @Override
    public String getActivityMessagePattern() {
        return properties.getString(prefix + "cc.activityMessagePattern");
    }

    @Override
    public File getClearToolExec() {
        return properties.getFile(prefix + "cleartool.exec");
    }

    @Override
    public File getCommitStampFileName() {
        return properties.getFile(prefix + "cc.commitStampFileName");
    }

    @Override
    public File getCounterStampFileName() {
        return properties.getFile(prefix + "cc.counterStampFileName");
    }

    @Override
    public Boolean getCreateActivity() {
        return properties.getBoolean(prefix + "git.createActivity");
    }

    @Override
    public Long getOverriddenSyncCounter() {
        return properties.getLong(prefix + "cc.overriddenSyncCounter");
    }

    @Override
    public String getOverriddenSyncFromCommit() {
        return properties.getString(prefix + "cc.overriddenSyncFromCommit");
    }

    public Properties getProperties() {
        return new Properties(properties);
    }

    @Override
    public Boolean getUpdateVobRoot() {
        return properties.getBoolean(prefix + "cc.updateVobRoot");
    }

    @Override
    public File getVobViewDir() {
        return properties.getFile(prefix + "vobview.dir");
    }

    @Override
    public ClearToolConfig setActivityMessagePattern(final String value) {
        properties.setString(prefix + "cc.activityMessagePattern", value);
        return this;
    }

    @Override
    public ClearToolConfig setClearToolExec(final File file) {
        properties.setFile(prefix + "cleartool.exec", file);
        return this;
    }

    @Override
    public ClearToolConfig setCommitStampFileName(final File file) {
        properties.setFile(prefix + "cc.commitStampFileName", file);
        return this;
    }

    @Override
    public ClearToolConfig setCounterStampFileName(final File file) {
        properties.setFile(prefix + "cc.counterStampFileName", file);
        return this;
    }

    @Override
    public ClearToolConfig setCreateActivity(final Boolean value) {
        properties.setBoolean(prefix + "git.createActivity", value);
        return this;
    }

    @Override
    public ClearToolConfig setOverriddenSyncCounter(final Long value) {
        properties.setLong(prefix + "cc.overriddenSyncCounter", value);
        return this;
    }

    @Override
    public ClearToolConfig setOverriddenSyncFromCommit(final String value) {
        properties.setString(prefix + "cc.overriddenSyncFromCommit", value);
        return this;
    }

    @Override
    public ClearToolConfig setUpdateVobRoot(final Boolean value) {
        properties.setBoolean(prefix + "cc.updateVobRoot", value);
        return this;
    }

    @Override
    public ClearToolConfig setVobViewDir(final File dir) {
        properties.setFile(prefix + "vobview.dir", dir);
        return this;
    }

    @Override
    public String toString() {
        return properties.toString();
    }
}
