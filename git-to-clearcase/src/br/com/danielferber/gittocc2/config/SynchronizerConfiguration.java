/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.config;

import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigSource;
import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigValidated;
import br.com.danielferber.gittocc2.config.git.GitConfigSource;
import br.com.danielferber.gittocc2.config.git.GitConfigValidated;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import java.io.PrintStream;
import org.slf4j.Logger;

/**
 *
 * @author Daniel Felix Ferber
 */
public abstract class SynchronizerConfiguration {

    final GitConfigSource gitConfig;
    final ClearToolConfigSource clearToolConfig;

    public SynchronizerConfiguration(GitConfigSource gitConfig, ClearToolConfigSource clearToolConfig) {
        this.gitConfig = gitConfig;
        this.clearToolConfig = clearToolConfig;
    }

    public final void validate() {
        new GitConfigValidated(gitConfig).validateAll();
        new ClearToolConfigValidated(clearToolConfig).validateAll();
    }

    public final GitConfigSource getGitConfig() {
        return new GitConfigValidated(gitConfig);
    }

    public final ClearToolConfigSource getClearToolConfig() {
        return new ClearToolConfigValidated(clearToolConfig);
    }

    public final void writeToLog(Logger logger) {
        if (logger.isInfoEnabled()) {
            try (PrintStream ps = LoggerFactory.getInfoPrintStream(logger)) {
                writeSpecificPropertiesToLog(ps);
                ClearToolConfigSource.Utils.printConfig(ps, clearToolConfig);
                GitConfigSource.Utils.printConfig(ps, gitConfig);
                ps.close();
            }
        }
    }

    protected void writeSpecificPropertiesToLog(PrintStream ps) {
        // nothing by default
    }
}
