package br.com.danielferber.gittocc2.config.sync;

import br.com.danielferber.gittocc2.config.ConfigProperties;
import java.util.Properties;

/**
 *
 * @author Daniel Felix Ferber
 */
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
    public SyncConfig setUpdateVobRoot(Boolean value) {
        properties.setBoolean(prefix + "cc.updateVobRoot", value);
        return this;
    }

    @Override
    public SyncConfig setFetchRemoteGitRepository(Boolean value) {
        properties.setBoolean(prefix + "git.fetchRemoteGitRepository", value);
        return this;
    }

    @Override
    public SyncConfig setFastForwardLocalGitRepository(Boolean value) {
        properties.setBoolean(prefix + "git.fastForwardLocalGitRepository", value);
        return this;
    }

    @Override
    public SyncConfig setResetLocalGitRepository(Boolean value) {
        properties.setBoolean(prefix + "git.resetLocalGitRepository", value);
        return this;
    }

    @Override
    public SyncConfig setCleanLocalGitRepository(Boolean value) {
        properties.setBoolean(prefix + "git.cleanLocalGitRepository", value);
        return this;
    }

    @Override
    public SyncConfig setCreateActivity(Boolean value) {
        properties.setBoolean(prefix + "git.createActivity", value);
        return this;
    }

    @Override
    public SyncConfig setActivityMessagePattern(String value) {
        properties.setString(prefix + "cc.activityMessagePattern", value);
        return this;
    }

    @Override
    public Boolean getUpdateVobRoot() {
        return properties.getBoolean(prefix + "cc.updateVobRoot");
    }

    @Override
    public Boolean getFetchRemoteGitRepository() {
        return properties.getBoolean(prefix + "git.fetchRemoteGitRepository");
    }

    @Override
    public Boolean getFastForwardLocalGitRepository() {
        return properties.getBoolean(prefix + "git.fastForwardLocalGitRepository");
    }

    @Override
    public Boolean getResetLocalGitRepository() {
        return properties.getBoolean(prefix + "git.resetLocalGitRepository");
    }

    @Override
    public Boolean getCleanLocalGitRepository() {
        return properties.getBoolean(prefix + "git.cleanLocalGitRepository");
    }

    @Override
    public Boolean getCreateActivity() {
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
