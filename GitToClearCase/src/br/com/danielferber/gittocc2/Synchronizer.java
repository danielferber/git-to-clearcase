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
        try {
            final SynchronizerCommandLine cl = new SynchronizerCommandLine(argv);
            nonValidatedClearToolConfig = cl.getClearToolConfig();
            nonValidateGitConfig = cl.getGitConfig();
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
                ps.println("ClearTools properties:");
                ps.println(" - cleartool executable file: " + nonValidatedClearToolConfig.getClearToolExec());
                ps.println(" - snapshot vob view directory: " + nonValidatedClearToolConfig.getVobViewDir());
                ps.println(" - ClearCase activity message pattern: " + nonValidatedClearToolConfig.getActivityMessagePattern());
                ps.println(" - create ClearCase activity: " + nonValidatedClearToolConfig.getCreateActivity());
                ps.println(" - update ClearCase vob: " + nonValidatedClearToolConfig.getUpdateVobRoot());
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

        SynchronizeTask task = new SynchronizeTask(cleartoolConfig, gitConfig);
        try {
            task.call();
        } catch (Exception ex) {
            logger.error("Failed to execute Sync Task.", ex);
        }
    }
}


