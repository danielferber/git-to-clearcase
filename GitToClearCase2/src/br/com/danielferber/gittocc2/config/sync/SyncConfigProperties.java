/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.config.sync;

import br.com.danielferber.gittocc2.config.ConfigProperties;
import java.util.Properties;

public class SyncConfigProperties implements SyncConfig {

    private final ConfigProperties properties;
    private final String prefix;

    public SyncConfigProperties(SyncConfigSource other, String prefix) {
        this.properties = new ConfigProperties();
        this.prefix = prefix;
        this.setActivityMessagePattern(other.getActivityMessagePattern());
        this.setCleanLocalGitRepository(other.getCleanLocalGitRepository());
        this.setCreateActivity(other.getCreateActivity());
        this.setFastForwardLocalGitRepository(other.getFastForwardLocalGitRepository());
        this.setFetchRemoteGitRepository(other.getFetchRemoteGitRepository());
        this.setResetLocalGitRepository(other.getResetLocalGitRepository());
        this.setUpdateVobRoot(other.getUpdateVobRoot());
    }

    public SyncConfigProperties(SyncConfigSource other) {
        this(other, "");
    }

    public SyncConfigProperties(Properties properties, String prefix) {
        this.properties = new ConfigProperties(properties);
        this.prefix = prefix;
    }

    public SyncConfigProperties(Properties properties) {
        this(properties, "");
    }

    @Override
    public SyncConfig setUpdateVobRoot(boolean value) {
        properties.setBoolean(prefix + "cc.updateVobRoot", value);
        return this;
    }

    @Override
    public SyncConfig setFetchRemoteGitRepository(boolean value) {
        properties.setBoolean(prefix + "git.fetchRemoteGitRepository", value);
        return this;
    }

    @Override
    public SyncConfig setFastForwardLocalGitRepository(boolean value) {
        properties.setBoolean(prefix + "git.fastForwardLocalGitRepository", value);
        return this;
    }

    @Override
    public SyncConfig setResetLocalGitRepository(boolean value) {
        properties.setBoolean(prefix + "git.resetLocalGitRepository", value);
        return this;
    }

    @Override
    public SyncConfig setCleanLocalGitRepository(boolean value) {
        properties.setBoolean(prefix + "git.cleanLocalGitRepository", value);
        return this;
    }

    @Override
    public SyncConfig setCreateActivity(boolean value) {
        properties.setBoolean(prefix + "git.createActivity", value);
        return this;
    }

    @Override
    public SyncConfig setActivityMessagePattern(String value) {
        properties.setString(prefix + "cc.activityMessagePattern", value);
        return this;
    }

    @Override
    public boolean getUpdateVobRoot() {
        return properties.getBoolean(prefix + "cc.updateVobRoot");
    }

    @Override
    public boolean getFetchRemoteGitRepository() {
        return properties.getBoolean(prefix + "git.fetchRemoteGitRepository");
    }

    @Override
    public boolean getFastForwardLocalGitRepository() {
        return properties.getBoolean(prefix + "git.fastForwardLocalGitRepository");
    }

    @Override
    public boolean getResetLocalGitRepository() {
        return properties.getBoolean(prefix + "git.resetLocalGitRepository");
    }

    @Override
    public boolean getCleanLocalGitRepository() {
        return properties.getBoolean(prefix + "git.cleanLocalGitRepository");
    }

    @Override
    public boolean getCreateActivity() {
        return properties.getBoolean(prefix + "git.createActivity");
    }

    @Override
    public String getActivityMessagePattern() {
        return properties.getString(prefix + "cc.activityMessagePattern");
    }

    @Override
    public String toString() {
        return properties.toString();
    }
}
