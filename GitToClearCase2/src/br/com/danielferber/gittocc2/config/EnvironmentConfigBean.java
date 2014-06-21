/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.config;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

public class EnvironmentConfigBean implements EnvironmentConfig {

    private final EnvironmentConfig wrapped;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public static final String GIT_EXEC_PROPERTY = "git.exec";
    public static final String REPOSITORY_DIR_PROPERTY = "repository.dir";
    public static final String CLEARTOOL_EXEC_PROPERTY = "cleartool.exec";
    public static final String VOB_VIEW_DIR_PROPERTY = "vob.view.dir";

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    public EnvironmentConfigBean() {
        super();
        this.wrapped = new EnvironmentConfigPojo();
    }

    public EnvironmentConfigBean(EnvironmentConfig other) {
        this.wrapped = other;
    }

    @Override
    public File getGitExec() {
        return wrapped.getGitExec();
    }

    @Override
    public File getRepositoryDir() {
        return wrapped.getRepositoryDir();
    }

    @Override
    public EnvironmentConfig setGitExec(File file) {
        final File oldValue = wrapped.getGitExec();
        wrapped.setGitExec(file);
        this.pcs.firePropertyChange(GIT_EXEC_PROPERTY, oldValue, file);
        return this;
    }

    @Override
    public EnvironmentConfig setRepositoryDir(File dir) {
        final File oldValue = wrapped.getRepositoryDir();
        wrapped.setRepositoryDir(dir);
        this.pcs.firePropertyChange(REPOSITORY_DIR_PROPERTY, oldValue, dir);
        return this;
    }

    @Override
    public File getClearToolExec() {
        return wrapped.getClearToolExec();
    }

    @Override
    public File getVobViewDir() {
        return wrapped.getClearToolExec();
    }

    @Override
    public EnvironmentConfig setClearToolExec(File file) {
        final File oldValue = wrapped.getClearToolExec();
        wrapped.setClearToolExec(file);
        this.pcs.firePropertyChange(CLEARTOOL_EXEC_PROPERTY, oldValue, file);
        return this;
    }

    @Override
    public EnvironmentConfig setVobViewDir(File dir) {
        final File oldValue = wrapped.getVobViewDir();
        wrapped.setVobViewDir(dir);
        this.pcs.firePropertyChange(VOB_VIEW_DIR_PROPERTY, oldValue, dir);
        return this;
    }
}
