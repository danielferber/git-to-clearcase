package br.com.danielferber.gittocc2.git;

import br.com.danielferber.gittocc2.config.ConfigException;
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
    
    public void validate() throws ConfigException {
        final File gitExec = getGitExec();
        if (gitExec == null) {
            throw new ConfigException("Git executable: missing value.");
        }
        if (!gitExec.exists()) {
            throw new ConfigException("Git executable: does not exist.");
        }
        if (!gitExec.isFile()) {
            throw new ConfigException("Git executable: not a file.");
        }
        if (!gitExec.canExecute()) {
            throw new ConfigException("Git executable: not executable.");
        }
        
        final File repositoryDir = getRepositoryDir();
        if (repositoryDir == null) {
            throw new ConfigException("Repository directory: missing value.");
        }
        if (!repositoryDir.exists()) {
            throw new ConfigException("Repository directory: does not exist.");
        }
        if (!repositoryDir.isDirectory()) {
            throw new ConfigException("Repository directory: not a directory.");
        }
        final File repositoryMetadataDir = new File(repositoryDir, ".git");
        if (!repositoryMetadataDir.isDirectory() || !repositoryMetadataDir.isDirectory()) {
            throw new ConfigException("Repository directory: not like a git repository.");
        }
    }
}
