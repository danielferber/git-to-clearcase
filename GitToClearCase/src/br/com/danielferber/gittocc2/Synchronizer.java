/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.ConfigException;
import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigPojo;
import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigSource;
import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigValidated;
import br.com.danielferber.gittocc2.config.git.GitConfigPojo;
import br.com.danielferber.gittocc2.config.git.GitConfigSource;
import br.com.danielferber.gittocc2.config.git.GitConfigValidated;
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

    public static void main(String[] argv) {
        final ClearToolConfigSource nonValidatedClearToolConfig;
        final GitConfigSource nonValidateGitConfig;
        final boolean compareOnly;
        final File compareRoot;
        try {
            final SynchronizerCommandLine cl = new SynchronizerCommandLine(argv);
            nonValidatedClearToolConfig = cl.getClearToolConfig();
            nonValidateGitConfig = cl.getGitConfig();
            compareOnly = cl.isCompareOnly();
            compareRoot = cl.getCompareRoot();
        } catch (ValueConversionException e) {
            logger.error("Incorrect command line arguments: {} ", e.getMessage());
            return;
        } catch (OptionException e) {
            logger.error("Incorrect command line arguments: {} ", e.getMessage());
            try (PrintStream ps = LoggerFactory.getErrorPrintStream(logger)) {
                SynchronizerCommandLine.printHelp(ps);
            } catch (IOException ex) {
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
                
                ps.println("ClearTools properties:");
                ps.println(" - Executable file: " + nonValidatedClearToolConfig.getClearToolExec());
                ps.println(" - VOB view directory: " + nonValidatedClearToolConfig.getVobViewDir());
                ps.println(" - Create activity: " + nonValidatedClearToolConfig.getCreateActivity());
                if (nonValidatedClearToolConfig.getCreateActivity() != null && nonValidatedClearToolConfig.getCreateActivity()) {
                    ps.println("   Activity message pattern: " + nonValidatedClearToolConfig.getActivityMessagePattern());
                }
                ps.println(" - Update ClearCase VOB directory: " + nonValidatedClearToolConfig.getUpdateVobRoot());
                ps.println(" - Commit stamp file: " + nonValidatedClearToolConfig.getCommitStampFile());
                ps.println(" - Counter stamp file: " + nonValidatedClearToolConfig.getCounterStampFile());
                if (nonValidatedClearToolConfig.getOverriddenSyncFromCommit() != null) {
                    ps.println(" - Overridden sync commit: " + nonValidatedClearToolConfig.getOverriddenSyncFromCommit());
                }
                if (nonValidatedClearToolConfig.getOverriddenSyncCounter()!= null) {
                    ps.println(" - Overridden sync counter: " + nonValidatedClearToolConfig.getOverriddenSyncCounter());
                }
                
                ps.println("Git properties:");
                ps.println(" - git executable file: " + nonValidateGitConfig.getGitExec());
                ps.println(" - git repository directory: " + nonValidateGitConfig.getRepositoryDir());
                ps.println(" - clean git repository: " + nonValidateGitConfig.getCleanLocalGitRepository());
                ps.println(" - fast forward git repository: " + nonValidateGitConfig.getFastForwardLocalGitRepository());
                ps.println(" - fetch from remote repository: " + nonValidateGitConfig.getFetchRemoteGitRepository());
                ps.println(" - reset git repository: " + nonValidateGitConfig.getResetLocalGitRepository());
            }
        }

        final ClearToolConfigSource cleartoolConfig;
        final GitConfigSource gitConfig;
        
        try {
            cleartoolConfig = new ClearToolConfigPojo(new ClearToolConfigValidated(nonValidatedClearToolConfig));
            gitConfig = new GitConfigPojo(new GitConfigValidated(nonValidateGitConfig));
        } catch (ConfigException e) {
            logger.error("Incorrent environment configuration: {}", e.getMessage());
            return;
        }

        SynchronizeTask task = new SynchronizeTask(cleartoolConfig, gitConfig, compareOnly,compareRoot);
        try {
            task.call();
        } catch (Exception ex) {
            logger.error("Failed to execute Sync Task.", ex);
        }
    }
}


