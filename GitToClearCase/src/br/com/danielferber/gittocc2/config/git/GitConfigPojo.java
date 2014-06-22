package br.com.danielferber.gittocc2.config.git;

import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfig;
import java.io.File;

/**
 *
 * @author Daniel Felix Ferber
 */
public class GitConfigPojo implements GitConfig {

    private File gitExec;
    private File repositoryDir;
    private Boolean fetchRemoteGitRepository;
    private Boolean fastForwardLocalGitRepository;
    private Boolean resetLocationGitRepository;
    private Boolean cleanLocalGitRepository;

    public GitConfigPojo() {
        super();
    }

    public GitConfigPojo(File gitExec, File repositoryDir) {
        this.gitExec = gitExec;
        this.repositoryDir = repositoryDir;
    }

    public GitConfigPojo(GitConfigSource other) {
        this.gitExec = other.getGitExec();
        this.repositoryDir = other.getRepositoryDir();
        fetchRemoteGitRepository = other.getFetchRemoteGitRepository();
        fastForwardLocalGitRepository = other.getFastForwardLocalGitRepository();
        resetLocationGitRepository = other.getResetLocalGitRepository();
        cleanLocalGitRepository = other.getCleanLocalGitRepository();
    }

    @Override
    public Boolean getCleanLocalGitRepository() {
        return this.cleanLocalGitRepository;
    }

    @Override
    public Boolean getFastForwardLocalGitRepository() {
        return this.fastForwardLocalGitRepository;
    }

    @Override
    public Boolean getFetchRemoteGitRepository() {
        return this.fetchRemoteGitRepository;
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
    public Boolean getResetLocalGitRepository() {
        return this.resetLocationGitRepository;
    }

    @Override
    public GitConfig setCleanLocalGitRepository(Boolean value) {
        this.cleanLocalGitRepository = value;
        return this;
    }

    @Override
    public GitConfig setFastForwardLocalGitRepository(Boolean value) {
        this.fastForwardLocalGitRepository = value;
        return this;
    }

    @Override
    public GitConfig setFetchRemoteGitRepository(Boolean value) {
        this.fetchRemoteGitRepository = value;
        return this;
    }

    @Override
    public GitConfig setGitExec(File file) {
        this.gitExec = file;
        return this;
    }

    @Override
    public GitConfig setRepositoryDir(File dir) {
        this.repositoryDir = dir;
        return this;
    }

    @Override
    public GitConfig setResetLocalGitRepository(Boolean value) {
        this.resetLocationGitRepository = value;
        return this;
    }

}
