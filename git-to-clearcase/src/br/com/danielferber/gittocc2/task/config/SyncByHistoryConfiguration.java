/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.task.config;

import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigSource;
import br.com.danielferber.gittocc2.config.git.GitConfigSource;
import java.io.PrintStream;

/**
 *
 * @author Daniel Felix Ferber
 */
public class SyncByHistoryConfiguration extends SyncStrategyConfiguration {
    public SyncByHistoryConfiguration(GitConfigSource gitConfig, ClearToolConfigSource clearToolConfig) {
        super(gitConfig, clearToolConfig);
    }

    @Override
    protected void writeSpecificPropertiesToLog(PrintStream ps) {
        ps.println("Synchronization strategy: GIT history");
    }
    
    
}
