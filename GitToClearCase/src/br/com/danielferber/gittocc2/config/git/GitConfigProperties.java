package br.com.danielferber.gittocc2.config.git;

import java.io.File;
import java.util.Properties;

import br.com.danielferber.gittocc2.config.ConfigProperties;

/**
 *
 * @author Daniel Felix Ferber
 */
public class GitConfigProperties implements GitConfig {

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
        return properties.getBoolean(prefix + "git.cleanLocalGitRepository");
    }

    @Override
    public Boolean getFastForwardLocalGitRepository() {
        return properties.getBoolean(prefix + "git.fastForwardLocalGitRepository");
    }

    @Override
    public Boolean getFetchRemoteGitRepository() {
        return properties.getBoolean(prefix + "git.fetchRemoteGitRepository");
    }

    @Override
    public File getGitExec() {
        return properties.getFile(prefix + "git.exec");
    }

    @Override
    public File getRepositoryDir() {
        return properties.getFile(prefix + "repository.dir");
    }

    @Override
    public Boolean getResetLocalGitRepository() {
        return properties.getBoolean(prefix + "git.resetLocalGitRepository");
    }

    @Override
    public Boolean getApplyDefaultGitConfig() {
        return properties.getBoolean(prefix + "git.applyDefaultGitConfig");
    }

    @Override
    public GitConfig setCleanLocalGitRepository(final Boolean value) {
        properties.setBoolean(prefix + "git.cleanLocalGitRepository", value);
        return this;
    }

    @Override
    public GitConfig setFastForwardLocalGitRepository(final Boolean value) {
        properties.setBoolean(prefix + "git.fastForwardLocalGitRepository", value);
        return this;
    }

    @Override
    public GitConfig setFetchRemoteGitRepository(final Boolean value) {
        properties.setBoolean(prefix + "git.fetchRemoteGitRepository", value);
        return this;
    }

    @Override
    public GitConfig setGitExec(final File file) {
        properties.setFile(prefix + "git.exec", file);
        return this;
    }

    @Override
    public GitConfig setRepositoryDir(final File dir) {
        properties.setFile(prefix + "repository.dir", dir);
        return this;
    }

    @Override
    public GitConfig setResetLocalGitRepository(final Boolean value) {
        properties.setBoolean(prefix + "git.resetLocalGitRepository", value);
        return this;
    }

    @Override
    public GitConfig setApplyDefaultGitConfig(final Boolean value) {
        properties.setBoolean(prefix + "git.applyDefaultGitConfig", value);
        return this;
    }

    @Override
    public String toString() {
        return properties.toString();
    }
}
