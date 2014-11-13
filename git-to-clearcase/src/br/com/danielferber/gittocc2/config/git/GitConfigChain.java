package br.com.danielferber.gittocc2.config.git;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Daniel Felix Ferber
 */
public class GitConfigChain implements GitConfigSource {

    private final List<GitConfigSource> sources;

    public GitConfigChain(List<GitConfigSource> sources) {
        this.sources = new ArrayList<>(sources);
    }
    
    public GitConfigChain(final GitConfigSource... sources) {
        this.sources = new ArrayList<>(Arrays.asList(sources));
    }

    public void addHighPriority(GitConfigSource source) {
        this.sources.add(0, source);
    }

    public void addLowPriority(GitConfigSource source) {
        this.sources.add(source);
    }

    @Override
    public Boolean getCleanLocalGitRepository() {
        for (GitConfigSource config : sources) {
            Boolean value = config.getCleanLocalGitRepository();
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public Boolean getFastForwardLocalGitRepository() {
        for (GitConfigSource config : sources) {
            Boolean value = config.getFastForwardLocalGitRepository();
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public Boolean getFetchRemoteGitRepository() {
        for (GitConfigSource config : sources) {
            Boolean value = config.getFetchRemoteGitRepository();
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public File getGitExec() {
        for (GitConfigSource config : sources) {
            File value = config.getGitExec();
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public File getRepositoryDir() {
        for (GitConfigSource config : sources) {
            File value = config.getRepositoryDir();
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public Boolean getResetLocalGitRepository() {
        for (GitConfigSource config : sources) {
            Boolean value = config.getResetLocalGitRepository();
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public Boolean getApplyDefaultGitConfig() {
        for (GitConfigSource config : sources) {
            Boolean value = config.getApplyDefaultGitConfig();
            if (value != null) {
                return value;
            }
        }
        return null;
    }
}
