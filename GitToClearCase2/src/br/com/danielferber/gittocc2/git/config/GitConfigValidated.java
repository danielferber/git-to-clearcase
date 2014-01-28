/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.git.config;

import java.io.File;

/**
 *
 * @author Daniel
 */
public class GitConfigValidated implements GitConfig {

    private final GitConfig wrappedGitConfig;

    public GitConfigValidated(GitConfig wrappedGitConfig) {
        this.wrappedGitConfig = wrappedGitConfig;
    }

    @Override
    public File getGitExec() {
        final File gitExec = wrappedGitConfig.getGitExec();
        if (gitExec == null) {
            throw new GitConfigException("Git executable: missing property.");
        }
        if (!gitExec.exists()) {
            throw new GitConfigException("Git executable: does not exist.");
        }
        if (!gitExec.isFile()) {
            throw new GitConfigException("Git executable: not a file.");
        }
        if (!gitExec.canExecute()) {
            throw new GitConfigException("Git executable: not executable.");
        }
        return wrappedGitConfig.getGitExec();
    }

    @Override
    public File getRepositoryDir() {
        File repositoryDir = getRepositoryDir();
        if (repositoryDir == null) {
            throw new GitConfigException("Repository directory: missing property.");
        }
        if (!repositoryDir.exists()) {
            throw new GitConfigException("Repository directory: does not exist.");
        }
        if (!repositoryDir.isDirectory()) {
            throw new GitConfigException("Repository directory: not a directory.");
        }
        File repositoryMetadataDir = new File(repositoryDir, ".git");
        if (!repositoryMetadataDir.isDirectory() || ! repositoryMetadataDir.isDirectory()) {
            throw new GitConfigException("Repository directory: not like a git repository.");
        }
        return repositoryDir;
    }

    @Override
    public void setGitExec(File gitExec) {
        wrappedGitConfig.setGitExec(gitExec);
    }

    @Override
    public void setRepositoryDir(File repositoryDir) {
        wrappedGitConfig.setGitExec(repositoryDir);
    }    
}
