package br.com.danielferber.gittocc2.config.git;

import java.io.File;

/**
 *
 * @author Daniel Felix Ferber
 */
public class GitConfigChain implements GitConfigSource {

    private final GitConfigSource wrapped1;
    private final GitConfigSource wrapped2;

    public GitConfigChain(GitConfigSource wrapped1, GitConfigSource wrapped2) {
        this.wrapped1 = wrapped1;
        this.wrapped2 = wrapped2;
    }

    @Override
    public Boolean getCleanLocalGitRepository() {
        if (wrapped2.getCleanLocalGitRepository() != null) {
            return wrapped2.getCleanLocalGitRepository();
        }
        return wrapped1.getCleanLocalGitRepository();
    }

    @Override
    public Boolean getFastForwardLocalGitRepository() {
        if (wrapped2.getFastForwardLocalGitRepository() != null) {
            return wrapped2.getFastForwardLocalGitRepository();
        }
        return wrapped1.getFastForwardLocalGitRepository();
    }

    @Override
    public Boolean getFetchRemoteGitRepository() {
        if (wrapped2.getFetchRemoteGitRepository() != null) {
            return wrapped2.getFetchRemoteGitRepository();
        }
        return wrapped1.getFetchRemoteGitRepository();
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
    public Boolean getResetLocalGitRepository() {
        if (wrapped2.getResetLocalGitRepository() != null) {
            return wrapped2.getResetLocalGitRepository();
        }
        return wrapped1.getResetLocalGitRepository();
    }

    @Override
    public Boolean getApplyDefaultGitConfig() {
        if (wrapped2.getApplyDefaultGitConfig() != null) {
            return wrapped2.getApplyDefaultGitConfig();
        }
        return wrapped1.getApplyDefaultGitConfig();
    }
}
