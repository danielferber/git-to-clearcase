package br.com.danielferber.gittocc2.config.git;

import br.com.danielferber.gittocc2.config.ConfigProperties;
import java.io.File;
import java.util.Properties;

/**
 *
 * @author Daniel Felix Ferber
 */
public class GitConfigProperties implements GitConfig {

    private final ConfigProperties properties;
    private final String prefix;

    public GitConfigProperties(GitConfigSource other) {
        this(other, "");
    }

    public GitConfigProperties(GitConfigSource other, String prefix) {
        this.properties = new ConfigProperties();
        this.prefix = prefix;
        this.setGitExec(other.getGitExec());
        this.setRepositoryDir(other.getRepositoryDir());
        this.setCleanLocalGitRepository(other.getCleanLocalGitRepository());
        this.setFastForwardLocalGitRepository(other.getFastForwardLocalGitRepository());
        this.setFetchRemoteGitRepository(other.getFetchRemoteGitRepository());
        this.setResetLocalGitRepository(other.getResetLocalGitRepository());
    }

    public GitConfigProperties(Properties properties) {
        this(properties, "");
    }

    public GitConfigProperties(Properties properties, String prefix) {
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
    public GitConfig setCleanLocalGitRepository(Boolean value) {
        properties.setBoolean(prefix + "git.cleanLocalGitRepository", value);
        return this;
    }

    @Override
    public GitConfig setFastForwardLocalGitRepository(Boolean value) {
        properties.setBoolean(prefix + "git.fastForwardLocalGitRepository", value);
        return this;
    }


    @Override
    public GitConfig setFetchRemoteGitRepository(Boolean value) {
        properties.setBoolean(prefix + "git.fetchRemoteGitRepository", value);
        return this;
    }

    @Override
    public GitConfig setGitExec(File file) {
        properties.setFile(prefix + "git.exec", file);
        return this;
    }

    @Override
    public GitConfig setRepositoryDir(File dir) {
        properties.setFile(prefix + "repository.dir", dir);
        return this;
    }

    @Override
    public GitConfig setResetLocalGitRepository(Boolean value) {
        properties.setBoolean(prefix + "git.resetLocalGitRepository", value);
        return this;
    }


    @Override
    public String toString() {
        return properties.toString();
    }
}
