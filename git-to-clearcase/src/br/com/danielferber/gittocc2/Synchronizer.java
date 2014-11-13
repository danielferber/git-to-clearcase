/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.CompareConfiguration;

import org.slf4j.Logger;

import br.com.danielferber.gittocc2.config.ConfigException;
import br.com.danielferber.gittocc2.config.ConfigurationReaderTask;
import br.com.danielferber.gittocc2.config.SynchronizerConfiguration;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;

/**
 *
 * @author Daniel Felix Ferber
 */
public class Synchronizer {

    private static final Logger logger = LoggerFactory.getLogger("GitToClearCase");

    public static void main(final String[] argv) {
        final SynchronizerConfiguration configuration;
        try {
            configuration = new ConfigurationReaderTask(argv).call();
        } catch (ConfigException e) {
            logger.error(e.getMessage());  
            return;
        } catch (Exception e) {
            logger.error("Failed to execute configuration reader task.", e);      
            return;
        }
        
        final SynchronizeTask task = new SynchronizeTask(
                configuration.getClearToolConfig(),
                configuration.getGitConfig(), 
                configuration instanceof CompareConfiguration,
                configuration instanceof CompareConfiguration ? ((CompareConfiguration) configuration).getCompareRoot() : null);
        try {
            task.call();
        } catch (final Exception ex) {
            logger.error("Failed to execute synchronization task.", ex);
        }
    }

 
}
