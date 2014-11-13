/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.config;

import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigSource;
import br.com.danielferber.gittocc2.config.git.GitConfigSource;
import java.io.PrintStream;

/**
 *
 * @author Daniel Felix Ferber
 */
public class ReplayChangesConfiguration extends SynchronizerConfiguration {
    public ReplayChangesConfiguration(GitConfigSource gitConfig, ClearToolConfigSource clearToolConfig) {
        super(gitConfig, clearToolConfig);
    }

    @Override
    protected void writeSpecificPropertiesToLog(PrintStream ps) {
        ps.println("Infer changes from: GIT history");
    }
    
    
}
