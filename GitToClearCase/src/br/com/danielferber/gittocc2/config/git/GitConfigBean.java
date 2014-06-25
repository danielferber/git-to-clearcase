package br.com.danielferber.gittocc2.config.git;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

/**
 *
 * @author Daniel Felix Ferber
 */
public class GitConfigBean implements GitConfig {

    private final GitConfig wrapped;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    public static final String GIT_EXEC_PROPERTY = "git.exec";
    public static final String REPOSITORY_DIR_PROPERTY = "repository.dir";
    public static final String CLEAN_LOCAL_GIT_REPOSITORY_PROPERTY = "git.cleanLocalGitRepository";
    public static final String RESET_LOCAL_GIT_REPOSITORY_PROPERTY = "git.resetLocalGitRepository";
    public static final String FAST_FORWARD_LOCAL_GIT_REPOSITORY_PROPERTY = "git.fastForwardLocalGitRepository";
    public static final String FETCH_REMOTE_GIT_REPOSITORY_PROPERTY = "git.fetchRemoteGitRepository";
    public static final String APPLY_DEFAULT_GIT_CONFIG_PROPERTY = "git.applyDefaultGitconfig";

    public GitConfigBean() {
        super();
        this.wrapped = new GitConfigPojo();
    }

    public GitConfigBean(final GitConfig other) {
        this.wrapped = other;
    }

    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    @Override
    public Boolean getCleanLocalGitRepository() {
        return wrapped.getCleanLocalGitRepository();
    }

    @Override
    public Boolean getFastForwardLocalGitRepository() {
        return wrapped.getFastForwardLocalGitRepository();
    }

    @Override
    public Boolean getFetchRemoteGitRepository() {
        return wrapped.getFetchRemoteGitRepository();
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
    public Boolean getResetLocalGitRepository() {
        return wrapped.getResetLocalGitRepository();
    }

    @Override
    public Boolean getApplyDefaultGitConfig() {
        return wrapped.getApplyDefaultGitConfig();
    }

    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    @Override
    public GitConfig setCleanLocalGitRepository(final Boolean value) {
        final Boolean oldValue = wrapped.getCleanLocalGitRepository();
        wrapped.setCleanLocalGitRepository(value);
        this.pcs.firePropertyChange(CLEAN_LOCAL_GIT_REPOSITORY_PROPERTY, oldValue, value);
        return this;
    }

    @Override
    public GitConfig setFastForwardLocalGitRepository(final Boolean value) {
        final Boolean oldValue = wrapped.getFastForwardLocalGitRepository();
        wrapped.setFastForwardLocalGitRepository(value);
        this.pcs.firePropertyChange(FAST_FORWARD_LOCAL_GIT_REPOSITORY_PROPERTY, oldValue, value);
        return this;
    }

    @Override
    public GitConfig setFetchRemoteGitRepository(final Boolean value) {
        final Boolean oldValue = wrapped.getFetchRemoteGitRepository();
        wrapped.setFetchRemoteGitRepository(value);
        this.pcs.firePropertyChange(FETCH_REMOTE_GIT_REPOSITORY_PROPERTY, oldValue, value);
        return this;
    }

    @Override
    public GitConfig setGitExec(final File file) {
        final File oldValue = wrapped.getGitExec();
        wrapped.setGitExec(file);
        this.pcs.firePropertyChange(GIT_EXEC_PROPERTY, oldValue, file);
        return this;
    }

    @Override
    public GitConfig setRepositoryDir(final File dir) {
        final File oldValue = wrapped.getRepositoryDir();
        wrapped.setRepositoryDir(dir);
        this.pcs.firePropertyChange(REPOSITORY_DIR_PROPERTY, oldValue, dir);
        return this;
    }

    @Override
    public GitConfig setResetLocalGitRepository(final Boolean value) {
        final Boolean oldValue = wrapped.getResetLocalGitRepository();
        wrapped.setResetLocalGitRepository(value);
        this.pcs.firePropertyChange(RESET_LOCAL_GIT_REPOSITORY_PROPERTY, oldValue, value);
        return this;
    }

    @Override
    public GitConfig setApplyDefaultGitConfig(final Boolean value) {
        final Boolean oldValue = wrapped.getApplyDefaultGitConfig();
        wrapped.setApplyDefaultGitConfig(value);
        this.pcs.firePropertyChange(APPLY_DEFAULT_GIT_CONFIG_PROPERTY, oldValue, value);
        return this;
    }
}
