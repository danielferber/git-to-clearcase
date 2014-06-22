/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.ConfigException;
import br.com.danielferber.gittocc2.config.environment.EnvironmentConfigPojo;
import br.com.danielferber.gittocc2.config.environment.EnvironmentConfigSource;
import br.com.danielferber.gittocc2.config.environment.EnvironmentConfigValidated;
import br.com.danielferber.gittocc2.config.sync.SyncConfigPojo;
import br.com.danielferber.gittocc2.config.sync.SyncConfigSource;
import br.com.danielferber.gittocc2.config.sync.SyncConfigValidated;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
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
        final EnvironmentConfigSource nonValidatedEnvironmentConfig;
        final SyncConfigSource nonValidatedSyncConfig;
        try {
            final SynchronizerCommandLine cl = new SynchronizerCommandLine(argv);
            nonValidatedEnvironmentConfig = cl.getEnvironmentConfig();
            nonValidatedSyncConfig = cl.getSyncConfig();
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
                ps.println("Environment properties:");
                ps.println(" - git executable file: " + nonValidatedEnvironmentConfig.getGitExec());
                ps.println(" - git repository directory: " + nonValidatedEnvironmentConfig.getRepositoryDir());
                ps.println(" - cleartool executable file: " + nonValidatedEnvironmentConfig.getClearToolExec());
                ps.println(" - snapshot vob view directory: " + nonValidatedEnvironmentConfig.getVobViewDir());
                ps.println("Sync Task properties:");
                ps.println(" - ClearCase activity message pattern: " + nonValidatedSyncConfig.getActivityMessagePattern());
                ps.println(" - create ClearCase activity: " + nonValidatedSyncConfig.getCreateActivity());
                ps.println(" - update ClearCase vob: " + nonValidatedSyncConfig.getUpdateVobRoot());
                ps.println(" - clean git repository: " + nonValidatedSyncConfig.getCleanLocalGitRepository());
                ps.println(" - fast forward git repository: " + nonValidatedSyncConfig.getFastForwardLocalGitRepository());
                ps.println(" - fetch from remote repository: " + nonValidatedSyncConfig.getFetchRemoteGitRepository());
                ps.println(" - reset git repository: " + nonValidatedSyncConfig.getResetLocalGitRepository());
            }
        }

        final EnvironmentConfigSource environmentConfig;
        final SyncConfigSource syncConfig;
        
        try {
            environmentConfig = new EnvironmentConfigPojo(new EnvironmentConfigValidated(nonValidatedEnvironmentConfig));
            syncConfig = new SyncConfigPojo(new SyncConfigValidated(nonValidatedSyncConfig));
        } catch (ConfigException e) {
            logger.error("Incorrent environment configuration: {}", e.getMessage());
            return;
        }

        SynchronizeTask task = new SynchronizeTask(environmentConfig, syncConfig);
        try {
            task.call();
        } catch (Exception ex) {
            logger.error("Failed to execute Sync Task.", ex);
        }
    }
}


