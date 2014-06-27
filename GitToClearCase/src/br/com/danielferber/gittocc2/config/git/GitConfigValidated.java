package br.com.danielferber.gittocc2.config.git;

import java.io.File;

import br.com.danielferber.gittocc2.config.ConfigException;

/**
 *
 * @author Daniel Felix Ferber
 */
public class GitConfigValidated implements GitConfigSource {

    private final GitConfigSource wrapped;

    public GitConfigValidated(final GitConfigSource wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public Boolean getCleanLocalGitRepository() {
         if (wrapped.getCleanLocalGitRepository() == null) {
            throw new ConfigException("Clean local git repository: missing value.");
        }
        return wrapped.getCleanLocalGitRepository();
    }

    @Override
    public Boolean getFastForwardLocalGitRepository() {
         if (wrapped.getFastForwardLocalGitRepository() == null) {
            throw new ConfigException("Fast forward local git repository: missing value.");
        }
        return wrapped.getFastForwardLocalGitRepository();
    }

    @Override
    public Boolean getFetchRemoteGitRepository() {
         if (wrapped.getFetchRemoteGitRepository() == null) {
            throw new ConfigException("Fetch remote git repository: missing value.");
        }
        return wrapped.getFetchRemoteGitRepository();
    }

    @Override
    public File getGitExec() {
        final File exec = wrapped.getGitExec();
        if (exec == null) {
            throw new ConfigException("Git executable: missing value.");
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
        final File dir = wrapped.getRepositoryDir();
        if (dir == null) {
            throw new ConfigException("Repository directory: missing value.");
        }
        if (!dir.exists()) {
            throw new ConfigException("Repository directory: does not exist.");
        }
        if (!dir.isDirectory()) {
            throw new ConfigException("Repository directory: not a directory.");
        }
        final File repositoryMetadataDir = new File(dir, ".git");
        if (!repositoryMetadataDir.isDirectory() || !repositoryMetadataDir.isDirectory()) {
            throw new ConfigException("Repository directory: not like a git repository.");
        }
        return dir;
    }

    @Override
    public Boolean getResetLocalGitRepository() {
        if (wrapped.getResetLocalGitRepository() == null) {
            throw new ConfigException("Reset local git repository: missing value.");
        }
        return wrapped.getResetLocalGitRepository();
    }

    @Override
    public Boolean getApplyDefaultGitConfig() {
        if (wrapped.getApplyDefaultGitConfig() == null) {
            throw new ConfigException("Apply default git config: missing value.");
        }
        return wrapped.getApplyDefaultGitConfig();
    }

	public void validateAll() {
		getApplyDefaultGitConfig();
		getCleanLocalGitRepository();
		getFastForwardLocalGitRepository();
		getFetchRemoteGitRepository();
		getGitExec();
		getRepositoryDir();
		getResetLocalGitRepository();

	}
}
