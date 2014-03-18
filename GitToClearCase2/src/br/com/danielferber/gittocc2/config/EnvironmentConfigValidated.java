/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.config;

import java.io.File;

/**
 *
 * @author Daniel
 */
public class EnvironmentConfigValidated implements EnvironmentConfig {

    private final EnvironmentConfig wrapped;

    public EnvironmentConfigValidated(EnvironmentConfig wrappedGitConfig) {
        this.wrapped = wrappedGitConfig;
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
    public void setGitExec(File file) {
        wrapped.setGitExec(file);
    }

    @Override
    public void setRepositoryDir(File dir) {
        wrapped.setGitExec(dir);
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

    @Override
    public void setClearToolExec(File file) {
        wrapped.setClearToolExec(file);
    }

    @Override
    public void setVobViewDir(File dir) {
        wrapped.setVobViewDir(dir);
    }
}
