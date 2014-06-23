package br.com.danielferber.gittocc2.config.git;

import br.com.danielferber.gittocc2.config.ConfigException;
import java.io.File;

/**
 *
 * @author Daniel Felix Ferber
 */
public class GitConfigValidated implements GitConfigSource {

    private GitConfigSource wrapped;

    public GitConfigValidated(GitConfigSource wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public Boolean getCleanLocalGitRepository() {
        return wrapped.getCleanLocalGitRepository();
    }

    @Override
    public Boolean getFastForwardLocalGitRepository() {
        return wrapped.getFastForwardLocalGitRepository();
    }

    @Override
    public Boolean getFetchRemoteGitRepository() {
        return wrapped.getFetchRemoteGitRepository();
    }

    @Override
    public File getGitExec() {
        final File exec = wrapped.getGitExec();
        if (exec == null) {
            throw new ConfigException("Git executable: missing property.");
        }
        if (!exec.exists()) {
            throw new ConfigException("Git executable: does not exist.");
        }
        if (!exec.isFile()) {
            throw new ConfigException("Git executable: not a file.");
        }
        if (!exec.canExecute()) {
            throw new ConfigException("Git executable: not executable.");
        }
        return exec;
    }

    @Override
    public File getRepositoryDir() {
        File dir = wrapped.getRepositoryDir();
        if (dir == null) {
            throw new ConfigException("Repository directory: missing property.");
        }
        if (!dir.exists()) {
            throw new ConfigException("Repository directory: does not exist.");
        }
        if (!dir.isDirectory()) {
            throw new ConfigException("Repository directory: not a directory.");
        }
        File repositoryMetadataDir = new File(dir, ".git");
        if (!repositoryMetadataDir.isDirectory() || !repositoryMetadataDir.isDirectory()) {
            throw new ConfigException("Repository directory: not like a git repository.");
        }
        return dir;
    }

    @Override
    public Boolean getResetLocalGitRepository() {
        return wrapped.getResetLocalGitRepository();
    }
}
