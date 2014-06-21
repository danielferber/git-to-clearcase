package br.com.danielferber.gittocc2.config.environment;

import java.io.File;

/**
 *
 * @author Daniel
 */
public class EnvironmentConfigChain implements EnvironmentConfigSource {
    final EnvironmentConfigSource wrapped1;
    final EnvironmentConfigSource wrapped2;

    public EnvironmentConfigChain(EnvironmentConfigSource wrapped1, EnvironmentConfigSource wrapped2) {
        this.wrapped1 = wrapped1;
        this.wrapped2 = wrapped2;
    }
    
    @Override
    public File getGitExec() {
        if (wrapped2.getGitExec() != null) {
            return wrapped2.getGitExec();
        }
        return wrapped1.getGitExec();
    }

    @Override
    public File getRepositoryDir() {
        if (wrapped2.getRepositoryDir() != null) {
            return wrapped2.getRepositoryDir();
        }
        return wrapped1.getRepositoryDir();
    }

    @Override
    public File getClearToolExec() {
        if (wrapped2.getClearToolExec() != null) {
            return wrapped2.getClearToolExec();
        }
        return wrapped1.getClearToolExec();
    }

    @Override
    public File getVobViewDir() {
        if (wrapped2.getVobViewDir() != null) {
            return wrapped2.getVobViewDir();
        }
        return wrapped1.getVobViewDir();
    }
}
