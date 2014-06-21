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

    public EnvironmentConfigPojo(EnvironmentConfigSource other) {
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
    public EnvironmentConfig setGitExec(File file) {
        this.gitExec = file;
        return this;
    }

    @Override
    public EnvironmentConfig setRepositoryDir(File dir) {
        this.repositoryDir = dir;
        return this;
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
    public EnvironmentConfig setClearToolExec(File file) {
        this.clearToolExec = file;
        return this;
    }

    @Override
    public EnvironmentConfig setVobViewDir(File dir) {
        this.vobViewDir = dir;
        return this;
    }
}
