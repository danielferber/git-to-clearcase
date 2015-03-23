package br.com.danielferber.gittocc2.git;

import br.com.danielferber.gittocc2.config.ConfigProperties;
import java.io.File;
import java.util.Properties;

/**
 *
 * @author Daniel Felix Ferber
 */
public final class GitConfigProperties extends GitConfigImpl {
    public static final String PROP_GIT_EXEC = "git.exec";
    public static final String PROP_REPOSITORY_DIR = "repository.dir";

    private final ConfigProperties properties;
    private final String prefix;

    public GitConfigProperties(final Properties properties) {
        this(properties, "");
    }

    public GitConfigProperties(final Properties properties, final String prefix) {
        this.properties = new ConfigProperties(properties);
        this.prefix = prefix;
    }

    @Override
    public File getGitExec() {
        return properties.getFile(prefix + PROP_GIT_EXEC);
    }

    @Override
    public File getRepositoryDir() {
        return properties.getFile(prefix + PROP_REPOSITORY_DIR);
    }

    public GitConfig setGitExec(final File file) {
        properties.setFile(prefix + PROP_GIT_EXEC, file);
        return this;
    }

    public GitConfig setRepositoryDir(final File dir) {
        properties.setFile(prefix + PROP_REPOSITORY_DIR, dir);
        return this;
    }

    @Override
    public String toString() {
        return properties.toString();
    }
}
