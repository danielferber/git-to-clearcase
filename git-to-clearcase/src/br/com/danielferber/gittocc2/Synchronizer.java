/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.ConfigException;
import br.com.danielferber.gittocc2.task.config.ConfigurationReaderTask;
import br.com.danielferber.gittocc2.task.config.SyncByCompareConfiguration;
import br.com.danielferber.gittocc2.task.config.SyncByHistoryConfiguration;
import br.com.danielferber.gittocc2.task.config.SyncStrategyConfiguration;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import org.slf4j.Logger;

/**
 *
 * @author Daniel Felix Ferber
 */
public class Synchronizer {

    private static final Logger logger = LoggerFactory.getLogger("GitToClearCase");

    public static void main(final String[] argv) {
        final SyncStrategyConfiguration configuration;
        try {
            configuration = new ConfigurationReaderTask(argv).call();
        } catch (ConfigException e) {
            logger.error(e.getMessage());  
            return;
        } catch (Exception e) {
            logger.error("Failed to execute configuration reader task.", e);      
            return;
        }
        
        try {
            final SyncStrategyTask synchronizerTask;
            synchronizerTask = synchronizerTaskFactory(configuration);
            synchronizerTask.call();
            return;
        } catch (Exception e) {
            logger.error("Failed to execute synchronization task.", e);
            return;
        }        
    }

    static SyncStrategyTask synchronizerTaskFactory(SyncStrategyConfiguration config) {
        if (config instanceof SyncByCompareConfiguration) {
            return new SyncByCompareTask((SyncByCompareConfiguration) config);
        } else if (config instanceof SyncByHistoryConfiguration) {
            return new SyncByHistoryTask((SyncByHistoryConfiguration) config);
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
