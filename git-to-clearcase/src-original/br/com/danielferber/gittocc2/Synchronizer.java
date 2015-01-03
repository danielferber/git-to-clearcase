/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.PropertiesConfigFactory;
import br.com.danielferber.gittocc2.task.ClearCaseActivityConfig;
import br.com.danielferber.gittocc2.task.ClearCaseFinalizeConfig;
import br.com.danielferber.gittocc2.task.ClearCasePrepareConfig;
import br.com.danielferber.gittocc2.task.ClearCaseVobConfig;
import br.com.danielferber.gittocc2.task.ClearToolConfig;
import br.com.danielferber.gittocc2.config.ConfigException;
import br.com.danielferber.gittocc2.task.GitConfig;
import br.com.danielferber.gittocc2.task.GitFinishConfig;
import br.com.danielferber.gittocc2.task.GitPrepareConfig;
import br.com.danielferber.gittocc2.task.GitRepositoryConfig;
import br.com.danielferber.gittocc2.config.SynchronizationConfigContainer;
import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigPojo;
import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigSource;
import br.com.danielferber.gittocc2.config.git.GitConfigPojo;
import br.com.danielferber.gittocc2.config.git.GitConfigSource;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import joptsimple.OptionException;
import joptsimple.ValueConversionException;
import org.slf4j.Logger;

/**
 *
 * @author Daniel Felix Ferber
 */
public class Synchronizer {

    private static final Logger logger = LoggerFactory.getLogger("GitToClearCase");

    public static void main(final String[] argv) {
        final ClearToolConfigSource cleartoolConfig;
        final GitConfigSource gitConfig;
        final boolean compareOnly;
        final File compareRoot;

        try {
            GitConfigPojo gitConfigDefault = createDefaultGitConfig();
            ClearToolConfigPojo clearToolConfigDefault = createDefaultClearToolConfig();

            final PropertiesConfigFactory cl = new PropertiesConfigFactory(argv, gitConfigDefault, clearToolConfigDefault);
            cleartoolConfig = cl.getClearToolConfig();
            gitConfig = cl.getGitConfig();
            compareOnly = cl.isCompareOnly();
            compareRoot = cl.getCompareRoot();
        } catch (final ValueConversionException | OptionException e) {
            logger.error("Incorrect command line arguments: {} ", e.getMessage());
            return;
        }

        if (gitConfig == null || cleartoolConfig == null) {
            try (PrintStream ps = LoggerFactory.getInfoPrintStream(logger)) {
                PropertiesConfigFactory.printHelp(ps);
            } catch (final IOException ex) {
                logger.error("Failed to print command line help.", ex);
            }
            return;
        }

        if (logger.isInfoEnabled()) {
            try (PrintStream ps = LoggerFactory.getInfoPrintStream(logger)) {
                ps.println("Infer changes from: " + (compareOnly ? "file by file comparison" : "GIT history"));
                if (compareOnly) {
                    ps.println(" - Compare root: " + compareRoot);
                }

                ClearToolConfig.printConfig(ps, (ClearToolConfig) cleartoolConfig);
                ClearCaseVobConfig.printConfig(ps, (ClearCaseVobConfig) cleartoolConfig);
                ClearCasePrepareConfig.printConfig(ps, (ClearCasePrepareConfig) cleartoolConfig);
                ClearCaseFinalizeConfig.printConfig(ps, (ClearCaseFinalizeConfig) cleartoolConfig);
                ClearCaseActivityConfig.printConfig(ps, (ClearCaseActivityConfig) cleartoolConfig);
                SynchronizationConfigContainer.printConfig(ps, (SynchronizationConfigContainer) cleartoolConfig);
                GitConfig.printConfig(ps, (GitConfig) gitConfig);
                GitRepositoryConfig.printConfig(ps, (GitRepositoryConfig) gitConfig);
                GitPrepareConfig.printConfig(ps, (GitPrepareConfig) gitConfig);
                GitFinishConfig.printConfig(ps, (GitFinishConfig) gitConfig);
                ps.close();
            }
        }

        try {
            ClearCaseActivityConfig.validate(cleartoolConfig);
            ClearCaseFinalizeConfig.validate(cleartoolConfig);
            ClearCaseFinalizeConfig.validate(cleartoolConfig);
            ClearCaseVobConfig.validate(cleartoolConfig);
            ClearToolConfig.validate(cleartoolConfig);
            SynchronizationConfigContainer.validate(cleartoolConfig);
            GitConfig.validate(gitConfig);
            GitFinishConfig.validate(gitConfig);
            GitPrepareConfig.validate(gitConfig);
            GitRepositoryConfig.validate(gitConfig);
        } catch (final ConfigException e) {
            logger.error("Incorrent environment configuration: {}", e.getMessage());
            return;
        }

        final SynchronizeTask task = new SynchronizeTask(cleartoolConfig, gitConfig, compareOnly, compareRoot);
        try {
            task.call();
        } catch (final Exception ex) {
            logger.error("Failed to execute Sync Task.", ex);
        }
    }

    private static GitConfigPojo createDefaultGitConfig() {
        final GitConfigPojo gitConfigDefault = new GitConfigPojo();
        gitConfigDefault.setApplyDefaultGitConfig(false);
        gitConfigDefault.setCleanLocalGitRepository(false);
        gitConfigDefault.setFastForwardLocalGitRepository(false);
        gitConfigDefault.setFetchRemoteGitRepository(false);
        gitConfigDefault.setResetLocalGitRepository(false);
        return gitConfigDefault;
    }

    private static ClearToolConfigPojo createDefaultClearToolConfig() {
        final ClearToolConfigPojo clearToolConfigDefault = new ClearToolConfigPojo();
        clearToolConfigDefault.setUseCommitStampFile(false);
        clearToolConfigDefault.setUseCounterStampFile(false);
        clearToolConfigDefault.setCommitStampFile(new File("sync-commit-stamp.txt"));
        clearToolConfigDefault.setCounterStampFile(new File("sync-counter-stamp.txt"));
        clearToolConfigDefault.setUseActivity(false);
        clearToolConfigDefault.setUpdateVobRoot(false);
        clearToolConfigDefault.setCheckForgottenCheckout(false);
        return clearToolConfigDefault;
    }
}
