package br.com.danielferber.gittocc2.config.git;

import java.io.File;
import java.util.Properties;

import br.com.danielferber.gittocc2.config.ConfigProperties;

/**
 *
 * @author Daniel Felix Ferber
 */
public class GitConfigProperties implements GitConfig {
    public static final String PROP_CLEAN_LOCAL_GIT_REPOSITORY = "cleanLocalGitRepository";
    public static final String PROP_FAST_FORWARD_LOCAL_GIT_REPOSITORY = "fastForwardLocalGitRepository";
    public static final String PROP_FETCH_REMOTE_GIT_REPOSITORY = "fetchRemoteGitRepository";
    public static final String PROP_GIT_EXEC = "git.exec";
    public static final String PROP_REPOSITORY_DIR = "repository.dir";
    public static final String PROP_RESET_LOCAL_GIT_REPOSITORY = "resetLocalGitRepository";
    public static final String PROP_APPLY_DEFAULT_GIT_CONFIG = "applyDefaultGitConfig";

    private final ConfigProperties properties;
    private final String prefix;

    public GitConfigProperties(final GitConfigSource other) {
        this(other, "");
    }

    public GitConfigProperties(final GitConfigSource other, final String prefix) {
        this.properties = new ConfigProperties();
        this.prefix = prefix;
        this.setGitExec(other.getGitExec());
        this.setRepositoryDir(other.getRepositoryDir());
        this.setCleanLocalGitRepository(other.getCleanLocalGitRepository());
        this.setFastForwardLocalGitRepository(other.getFastForwardLocalGitRepository());
        this.setFetchRemoteGitRepository(other.getFetchRemoteGitRepository());
        this.setResetLocalGitRepository(other.getResetLocalGitRepository());
        this.setApplyDefaultGitConfig(other.getApplyDefaultGitConfig());
    }

    public GitConfigProperties(final Properties properties) {
        this(properties, "");
    }

    public GitConfigProperties(final Properties properties, final String prefix) {
        this.properties = new ConfigProperties(properties);
        this.prefix = prefix;
    }

    @Override
    public Boolean getCleanLocalGitRepository() {
        return properties.getBoolean(prefix + PROP_CLEAN_LOCAL_GIT_REPOSITORY);
    }

    @Override
    public Boolean getFastForwardLocalGitRepository() {
        return properties.getBoolean(prefix + PROP_FAST_FORWARD_LOCAL_GIT_REPOSITORY);
    }

    @Override
    public Boolean getFetchRemoteGitRepository() {
        return properties.getBoolean(prefix + PROP_FETCH_REMOTE_GIT_REPOSITORY);
    }

    @Override
    public File getGitExec() {
        return properties.getFile(prefix + PROP_GIT_EXEC);
    }

    @Override
    public File getRepositoryDir() {
        return properties.getFile(prefix + PROP_REPOSITORY_DIR);
    }

    @Override
    public Boolean getResetLocalGitRepository() {
        return properties.getBoolean(prefix + PROP_RESET_LOCAL_GIT_REPOSITORY);
    }

    @Override
    public Boolean getApplyDefaultGitConfig() {
        return properties.getBoolean(prefix + PROP_APPLY_DEFAULT_GIT_CONFIG);
    }

    @Override
    public GitConfig setCleanLocalGitRepository(final Boolean value) {
        properties.setBoolean(prefix + PROP_CLEAN_LOCAL_GIT_REPOSITORY, value);
        return this;
    }

    @Override
    public GitConfig setFastForwardLocalGitRepository(final Boolean value) {
        properties.setBoolean(prefix + PROP_FAST_FORWARD_LOCAL_GIT_REPOSITORY, value);
        return this;
    }

    @Override
    public GitConfig setFetchRemoteGitRepository(final Boolean value) {
        properties.setBoolean(prefix + PROP_FETCH_REMOTE_GIT_REPOSITORY, value);
        return this;
    }

    @Override
    public GitConfig setGitExec(final File file) {
        properties.setFile(prefix + PROP_GIT_EXEC, file);
        return this;
    }

    @Override
    public GitConfig setRepositoryDir(final File dir) {
        properties.setFile(prefix + PROP_REPOSITORY_DIR, dir);
        return this;
    }

    @Override
    public GitConfig setResetLocalGitRepository(final Boolean value) {
        properties.setBoolean(prefix + PROP_RESET_LOCAL_GIT_REPOSITORY, value);
        return this;
    }

    @Override
    public GitConfig setApplyDefaultGitConfig(final Boolean value) {
        properties.setBoolean(prefix + PROP_APPLY_DEFAULT_GIT_CONFIG, value);
        return this;
    }

    @Override
    public String toString() {
        return properties.toString();
    }
}
