package br.com.danielferber.gittocc2.config;

import java.io.File;

/**
 *
 * @author Daniel
 */
public class EnvironmentConfigValidated implements EnvironmentConfigSource {

    private final EnvironmentConfigSource wrapped;

    public EnvironmentConfigValidated(EnvironmentConfigSource wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public File getGitExec() {
        final File exec = wrapped.getGitExec();
        if (exec == null) {
            throw new EnvironmentConfigException("Git executable: missing property.");
        }
        if (!exec.exists()) {
            throw new EnvironmentConfigException("Git executable: does not exist.");
        }
        if (!exec.isFile()) {
            throw new EnvironmentConfigException("Git executable: not a file.");
        }
        if (!exec.canExecute()) {
            throw new EnvironmentConfigException("Git executable: not executable.");
        }
        return wrapped.getGitExec();
    }

    @Override
    public File getRepositoryDir() {
        File dir = getRepositoryDir();
        if (dir == null) {
            throw new EnvironmentConfigException("Repository directory: missing property.");
        }
        if (!dir.exists()) {
            throw new EnvironmentConfigException("Repository directory: does not exist.");
        }
        if (!dir.isDirectory()) {
            throw new EnvironmentConfigException("Repository directory: not a directory.");
        }
        File repositoryMetadataDir = new File(dir, ".git");
        if (!repositoryMetadataDir.isDirectory() || !repositoryMetadataDir.isDirectory()) {
            throw new EnvironmentConfigException("Repository directory: not like a git repository.");
        }
        return dir;
    }

    @Override
    public File getClearToolExec() {
        final File exec = wrapped.getClearToolExec();
        if (exec == null) {
            throw new EnvironmentConfigException("Git executable: missing property.");
        }
        if (!exec.exists()) {
            throw new EnvironmentConfigException("Git executable: does not exist.");
        }
        if (!exec.isFile()) {
            throw new EnvironmentConfigException("Git executable: not a file.");
        }
        if (!exec.canExecute()) {
            throw new EnvironmentConfigException("Git executable: not executable.");
        }
        return wrapped.getGitExec();
    }

    @Override
    public File getVobViewDir() {
        File dir = getVobViewDir();
        if (dir == null) {
            throw new EnvironmentConfigException("Vob view directory: missing property.");
        }
        if (!dir.exists()) {
            throw new EnvironmentConfigException("Vob view directory: does not exist.");
        }
        if (!dir.isDirectory()) {
            throw new EnvironmentConfigException("Vob view directory: not a directory.");
        }
        return dir;
    }
}
