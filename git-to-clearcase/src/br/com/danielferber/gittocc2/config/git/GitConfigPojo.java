package br.com.danielferber.gittocc2.config.git;

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
    private Boolean applyDefaultGitConfig;

    public GitConfigPojo() {
        super();
    }

    public GitConfigPojo(final File gitExec, final File repositoryDir) {
        this.gitExec = gitExec;
        this.repositoryDir = repositoryDir;
    }

    public GitConfigPojo(final GitConfigSource other) {
        this.gitExec = other.getGitExec();
        this.repositoryDir = other.getRepositoryDir();
        fetchRemoteGitRepository = other.getFetchRemoteGitRepository();
        fastForwardLocalGitRepository = other.getFastForwardLocalGitRepository();
        resetLocationGitRepository = other.getResetLocalGitRepository();
        cleanLocalGitRepository = other.getCleanLocalGitRepository();
        applyDefaultGitConfig = other.getApplyDefaultGitConfig();
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
    public Boolean getApplyDefaultGitConfig() {
        return applyDefaultGitConfig;
    }

    @Override
    public GitConfig setCleanLocalGitRepository(final Boolean value) {
        this.cleanLocalGitRepository = value;
        return this;
    }

    @Override
    public GitConfig setFastForwardLocalGitRepository(final Boolean value) {
        this.fastForwardLocalGitRepository = value;
        return this;
    }

    @Override
    public GitConfig setFetchRemoteGitRepository(final Boolean value) {
        this.fetchRemoteGitRepository = value;
        return this;
    }

    @Override
    public GitConfig setGitExec(final File file) {
        this.gitExec = file;
        return this;
    }

    @Override
    public GitConfig setRepositoryDir(final File dir) {
        this.repositoryDir = dir;
        return this;
    }

    @Override
    public GitConfig setResetLocalGitRepository(final Boolean value) {
        this.resetLocationGitRepository = value;
        return this;
    }

    @Override
    public GitConfig setApplyDefaultGitConfig(final Boolean value) {
        this.applyDefaultGitConfig = value;
        return this;
    }
}
