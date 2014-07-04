/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import joptsimple.OptionException;
import joptsimple.ValueConversionException;

import org.slf4j.Logger;

import br.com.danielferber.gittocc2.config.ConfigException;
import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigPojo;
import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigSource;
import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigValidated;
import br.com.danielferber.gittocc2.config.git.GitConfigPojo;
import br.com.danielferber.gittocc2.config.git.GitConfigSource;
import br.com.danielferber.gittocc2.config.git.GitConfigValidated;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;

/**
 *
 * @author Daniel Felix Ferber
 */
public class Synchronizer {

    private static final Logger logger = LoggerFactory.getLogger("GitToClearCase");

    public static void main(final String[] argv) {
        final ClearToolConfigSource nonValidatedClearToolConfig;
        final GitConfigSource nonValidateGitConfig;
        final boolean compareOnly;
        final File compareRoot;
        try {
        	final GitConfigPojo gitConfigDefault = new GitConfigPojo();
        	gitConfigDefault.setApplyDefaultGitConfig(false);
        	gitConfigDefault.setCleanLocalGitRepository(false);
        	gitConfigDefault.setFastForwardLocalGitRepository(false);
        	gitConfigDefault.setFetchRemoteGitRepository(false);
        	gitConfigDefault.setResetLocalGitRepository(false);
        	
        	final ClearToolConfigPojo clearToolConfigDefault = new ClearToolConfigPojo();
        	clearToolConfigDefault.setCommitStampFileName(new File("sync-commit-stamp.txt"));
        	clearToolConfigDefault.setCounterStampFileName(new File("sync-counter-stamp.txt"));
        	clearToolConfigDefault.setUseSyncActivity(false);
        	clearToolConfigDefault.setUpdateVobRoot(false);
        	
            final SynchronizerCommandLine cl = new SynchronizerCommandLine(argv, gitConfigDefault, clearToolConfigDefault);
            nonValidatedClearToolConfig = cl.getClearToolConfig();
            nonValidateGitConfig = cl.getGitConfig();
            compareOnly = cl.isCompareOnly();
            compareRoot = cl.getCompareRoot();
        } catch (final ValueConversionException e) {
            logger.error("Incorrect command line arguments: {} ", e.getMessage());
            return;
        } catch (final OptionException e) {
            logger.error("Incorrect command line arguments: {} ", e.getMessage());
            try (PrintStream ps = LoggerFactory.getErrorPrintStream(logger)) {
                SynchronizerCommandLine.printHelp(ps);
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

                ClearToolConfigSource.Utils.printConfig(ps, nonValidatedClearToolConfig);
                GitConfigSource.Utils.printConfig(ps, nonValidateGitConfig);
                ps.close();
            }
        }

        final ClearToolConfigValidated cleartoolConfig;
        final GitConfigValidated gitConfig;

        try {
            cleartoolConfig = new ClearToolConfigValidated(nonValidatedClearToolConfig);
            cleartoolConfig.validateAll();
            gitConfig = new GitConfigValidated(nonValidateGitConfig);
            gitConfig.validateAll();
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
}


