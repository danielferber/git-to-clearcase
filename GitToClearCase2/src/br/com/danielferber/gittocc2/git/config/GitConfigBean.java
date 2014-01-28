/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.danielferber.gittocc2.git.config;

import java.io.File;


public class GitConfigBean implements GitConfig {
    private File gitExec;
    private File repositoryDir;

    public GitConfigBean(File gitExec, File repositoryDir) {
        this.gitExec = gitExec;
        this.repositoryDir = repositoryDir;
    }

    public GitConfigBean(GitConfig other) {
        this.gitExec = other.getGitExec();
        this.repositoryDir = other.getRepositoryDir();
    }

    @Override
    public File getGitExec() {
        return gitExec;
    }

    @Override
    public File getRepositoryDir() {
        return repositoryDir;
    }

    @Override
    public void setGitExec(File gitExec) {
        this.gitExec = gitExec;
    }

    @Override
    public void setRepositoryDir(File repositoryDir) {
        this.repositoryDir = repositoryDir;
    }
}
