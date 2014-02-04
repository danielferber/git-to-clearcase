/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.git.config;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.Serializable;

public class GitConfigBean implements GitConfig {

    private final GitConfig wrappedGitConfig;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    public static final String REPOSITORY_DIR_PROPERTY = "repositoryDir";
    public static final String GIT_EXEC_PROPERTY = "gitExec";

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    public GitConfigBean() {
        super();
        this.wrappedGitConfig = new GitConfigPojo();
    }

    public GitConfigBean(GitConfig other) {
        this.wrappedGitConfig = other;
    }

    @Override
    public File getGitExec() {
        return wrappedGitConfig.getGitExec();
    }

    @Override
    public File getRepositoryDir() {
        return wrappedGitConfig.getRepositoryDir();
    }

    @Override
    public void setGitExec(File gitExec) {
        final File oldValue = wrappedGitConfig.getGitExec();
        wrappedGitConfig.setGitExec(gitExec);
        this.pcs.firePropertyChange(GIT_EXEC_PROPERTY, oldValue, gitExec);
    }

    @Override
    public void setRepositoryDir(File repositoryDir) {
        final File oldValue = wrappedGitConfig.getRepositoryDir();
        wrappedGitConfig.setRepositoryDir(repositoryDir);
        this.pcs.firePropertyChange(REPOSITORY_DIR_PROPERTY, oldValue, repositoryDir);
    }
}
