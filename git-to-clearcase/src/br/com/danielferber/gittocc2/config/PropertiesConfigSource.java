/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.config;

import java.io.File;
import java.util.Properties;

public class PropertiesConfigSource {

    public static final String PROP_VOB_VIEW_DIR = "vobview.dir";
    public static final String PROP_VOB_VIEW_UPDATE = "vobview.update";
    public static final String PROP_CLEAR_TOOL_EXEC = "cleartool.exec";
    public static final String PROP_USE_ACTIVITY = "activity.use";
    public static final String PROP_ACTIVITY_NAME = "activity.name";
    public static final String PROP_USE_COMMIT_STAMP_FILE = "stamp.commit.use";
    public static final String PROP_USE_COUNTER_STAMP_FILE = "stamp.counter.use";
    public static final String PROP_CHECK_CHECKOUT_FORGOTTEN = "check.checkout.forgotten";
    public static final String PROP_COMMIT_STAMP_FILE = "stamp.commit.file";
    public static final String PROP_COUNTER_STAMP_FILE = "stamp.counter.file";
    public static final String PROP_OVERRIDDEN_SYNC_COUNTER = "stamp.commit.override";
    public static final String PROP_OVERRIDDEN_SYNC_COMMIT = "stamp.counter.override";

    public static final String PROP_CLEAN_LOCAL_GIT_REPOSITORY = "cleanLocalGitRepository";
    public static final String PROP_FAST_FORWARD_LOCAL_GIT_REPOSITORY = "fastForwardLocalGitRepository";
    public static final String PROP_FETCH_REMOTE_GIT_REPOSITORY = "fetchRemoteGitRepository";
    public static final String PROP_GIT_EXEC = "git.exec";
    public static final String PROP_REPOSITORY_DIR = "repository.dir";
    public static final String PROP_RESET_LOCAL_GIT_REPOSITORY = "resetLocalGitRepository";
    public static final String PROP_APPLY_DEFAULT_GIT_CONFIG = "applyDefaultGitConfig";

    private final ConfigProperties properties;

    public PropertiesConfigSource(final Properties properties) {
        this.properties = new ConfigProperties(properties);
    }

    public class ClearCaseActivityBean implements ClearCaseActivityConfig {

        @Override
        public String getActivityName() {
            return properties.getString(PROP_ACTIVITY_NAME);
        }

        public ClearCaseActivityBean setActivityName(final String value) {
            properties.setString(PROP_ACTIVITY_NAME, value);
            return this;
        }

        @Override
        public Boolean getUseActivity() {
            return properties.getBoolean(PROP_USE_ACTIVITY);
        }

        public ClearCaseActivityBean setUseActivity(final Boolean value) {
            properties.setBoolean(PROP_USE_ACTIVITY, value);
            return this;
        }
    };

    public class ClearToolBean implements ClearToolConfig {

        @Override
        public File getClearToolExec() {
            return properties.getFile(PROP_CLEAR_TOOL_EXEC);
        }

        public ClearToolBean setClearToolExec(final File file) {
            properties.setFile(PROP_CLEAR_TOOL_EXEC, file);
            return this;
        }

        @Override
        public File getClearToolAbsoluteExec() {
            return getClearToolExec().getAbsoluteFile();
        }
    }

    public class SynchronizationBean implements SynchronizationConfig {

        @Override
        public Boolean getUseCommitStampFile() {
            return properties.getBoolean(PROP_USE_COMMIT_STAMP_FILE);
        }

        public SynchronizationBean setUseCommitStampFile(final Boolean value) {
            properties.setBoolean(PROP_COMMIT_STAMP_FILE, value);
            return this;
        }

        @Override
        public Boolean getUseCounterStampFile() {
            return properties.getBoolean(PROP_USE_COUNTER_STAMP_FILE);
        }

        public SynchronizationBean setUseCounterStampFile(final Boolean value) {
            properties.setBoolean(PROP_COUNTER_STAMP_FILE, value);
            return this;
        }

        @Override
        public Long getOverriddenSyncCounter() {
            return properties.getLong(PROP_OVERRIDDEN_SYNC_COUNTER);
        }

        public SynchronizationBean setOverriddenSyncCounter(final Long value) {
            properties.setLong(PROP_OVERRIDDEN_SYNC_COUNTER, value);
            return this;
        }

        @Override
        public String getOverriddenSyncFromCommit() {
            return properties.getString(PROP_OVERRIDDEN_SYNC_COMMIT);
        }

        public SynchronizationBean setOverriddenSyncFromCommit(final String value) {
            properties.setString(PROP_OVERRIDDEN_SYNC_COMMIT, value);
            return this;
        }
    }

    public class ClearCaseVobBean implements ClearCaseVobConfig {

        @Override
        public File getCounterStampFile() {
            return properties.getFile(PROP_COUNTER_STAMP_FILE);
        }

        public ClearCaseVobBean setCounterStampFile(final File file) {
            properties.setFile(PROP_COUNTER_STAMP_FILE, file);
            return this;
        }

        @Override
        public File getCommitStampFile() {
            return properties.getFile(PROP_COMMIT_STAMP_FILE);
        }

        public ClearCaseVobBean setCommitStampFile(final File file) {
            properties.setFile(PROP_COMMIT_STAMP_FILE, file);
            return this;
        }

        @Override
        public File getVobViewDir() {
            return properties.getFile(PROP_VOB_VIEW_DIR);
        }

        public ClearCaseVobBean setVobViewDir(final File dir) {
            properties.setFile(PROP_VOB_VIEW_DIR, dir);
            return this;
        }

        @Override
        public File getCommitStampAbsoluteFile() {
            return new File(getVobViewDir(), getCommitStampFile().getPath());
        }

        @Override
        public File getCounterStampAbsoluteFile() {
            return new File(getVobViewDir(), getCounterStampFile().getPath());
        }

        @Override
        public File getVobViewAbsoluteDir() {
            return getVobViewDir().getAbsoluteFile();
        }
    }

    public class ClearCasePrepareBean implements ClearCasePrepareConfig {

        @Override
        public Boolean getVobViewDirUpdate() {
            return properties.getBoolean(PROP_VOB_VIEW_UPDATE);
        }

        public ClearCasePrepareBean setUpdateVobRoot(final Boolean value) {
            properties.setBoolean(PROP_VOB_VIEW_UPDATE, value);
            return this;
        }
    }

    public class ClearCaseFinalizeBean implements ClearCaseFinalizeConfig {

        @Override
        public Boolean getValidateExistingCheckout() {
            return properties.getBoolean(PROP_CHECK_CHECKOUT_FORGOTTEN);
        }

        public ClearCaseFinalizeBean setValidateExistingCheckout(Boolean value) {
            properties.setBoolean(PROP_CHECK_CHECKOUT_FORGOTTEN, value);
            return this;
        }
    }

    public class GitBean implements GitConfig {

        @Override
        public File getGitExec() {
            return properties.getFile(PROP_GIT_EXEC);
        }

        public GitBean setGitExec(final File file) {
            properties.setFile(PROP_GIT_EXEC, file);
            return this;
        }
    }

    public class GitFinishBean implements GitFinishConfig {

    }

    public class GitPrepareBean implements GitPrepareConfig {

        @Override
        public Boolean getCleanLocalGitRepository() {
            return properties.getBoolean(PROP_CLEAN_LOCAL_GIT_REPOSITORY);
        }

        public GitPrepareBean setCleanLocalGitRepository(final Boolean value) {
            properties.setBoolean(PROP_CLEAN_LOCAL_GIT_REPOSITORY, value);
            return this;
        }

        @Override
        public Boolean getFastForwardLocalGitRepository() {
            return properties.getBoolean(PROP_FAST_FORWARD_LOCAL_GIT_REPOSITORY);
        }

        public GitPrepareBean setFastForwardLocalGitRepository(final Boolean value) {
            properties.setBoolean(PROP_FAST_FORWARD_LOCAL_GIT_REPOSITORY, value);
            return this;
        }

        @Override
        public Boolean getFetchRemoteGitRepository() {
            return properties.getBoolean(PROP_FETCH_REMOTE_GIT_REPOSITORY);
        }

        public GitPrepareBean setFetchRemoteGitRepository(final Boolean value) {
            properties.setBoolean(PROP_FETCH_REMOTE_GIT_REPOSITORY, value);
            return this;
        }

        @Override
        public Boolean getResetLocalGitRepository() {
            return properties.getBoolean(PROP_RESET_LOCAL_GIT_REPOSITORY);
        }

        public GitPrepareBean setResetLocalGitRepository(final Boolean value) {
            properties.setBoolean(PROP_RESET_LOCAL_GIT_REPOSITORY, value);
            return this;
        }

        @Override
        public Boolean getApplyDefaultGitConfig() {
            return properties.getBoolean(PROP_APPLY_DEFAULT_GIT_CONFIG);
        }

        public GitPrepareBean setApplyDefaultGitConfig(final Boolean value) {
            properties.setBoolean(PROP_APPLY_DEFAULT_GIT_CONFIG, value);
            return this;
        }
    }

    public class GitRepositoryBean implements GitRepositoryConfig {

        @Override
        public File getRepositoryDir() {
            return properties.getFile(PROP_REPOSITORY_DIR);
        }

        public GitRepositoryBean setRepositoryDir(final File dir) {
            properties.setFile(PROP_REPOSITORY_DIR, dir);
            return this;
        }
    }
}
