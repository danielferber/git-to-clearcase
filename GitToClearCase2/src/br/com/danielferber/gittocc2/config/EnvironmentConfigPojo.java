/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.config;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author Daniel
 */
public class EnvironmentConfigPojo implements EnvironmentConfig, Serializable {

    private File gitExec;
    private File repositoryDir;
    private File clearToolExec;
    private File vobViewDir;

    public EnvironmentConfigPojo() {
        super();
    }

    public EnvironmentConfigPojo(File gitExec, File repositoryDir, File clearToolExec, File vobViewDir) {
        this.gitExec = gitExec;
        this.repositoryDir = repositoryDir;
        this.clearToolExec = clearToolExec;
        this.vobViewDir = vobViewDir;
    }

    public EnvironmentConfigPojo(EnvironmentConfig other) {
        this.gitExec = other.getGitExec();
        this.repositoryDir = other.getRepositoryDir();
        this.clearToolExec = other.getClearToolExec();
        this.vobViewDir = other.getVobViewDir();
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
    public void setGitExec(File file) {
        this.gitExec = file;
    }

    @Override
    public void setRepositoryDir(File dir) {
        this.repositoryDir = dir;
    }

    @Override
    public File getClearToolExec() {
        return clearToolExec;
    }

    @Override
    public File getVobViewDir() {
        return vobViewDir;
    }

    @Override
    public void setClearToolExec(File file) {
        this.clearToolExec = file;
    }

    @Override
    public void setVobViewDir(File dir) {
        this.vobViewDir = dir;
    }
}
