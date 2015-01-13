/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.git;

import br.com.danielferber.gittocc2.config.ConfigException;
import java.io.PrintStream;

/**
 *
 * @author Daniel Felix Ferber
 */
public interface GitPrepareConfig {

    Boolean getCleanLocalGitRepository();

    Boolean getFastForwardLocalGitRepository();

    Boolean getFetchRemoteGitRepository();

    Boolean getResetLocalGitRepository();

    Boolean getApplyDefaultGitConfig();

    static void printConfig(PrintStream ps, GitPrepareConfig config) {
        ps.println(" * Git preparation configuration:");
        ps.println("    - apply default git configuration: " + config.getApplyDefaultGitConfig());
        ps.println("    - clean repository: " + config.getCleanLocalGitRepository());
        ps.println("    - reset repository: " + config.getResetLocalGitRepository());
        ps.println("    - fetch from remote repository: " + config.getFetchRemoteGitRepository());
        ps.println("    - fast forward repository: " + config.getFastForwardLocalGitRepository());
    }

    static void validate(final GitPrepareConfig config) throws ConfigException {
        if (config.getCleanLocalGitRepository() == null) {
            throw new ConfigException("Clean local git repository: missing value.");
        }
        if (config.getFastForwardLocalGitRepository() == null) {
            throw new ConfigException("Fast forward local git repository: missing value.");
        }
        if (config.getFetchRemoteGitRepository() == null) {
            throw new ConfigException("Fetch remote git repository: missing value.");
        }
        if (config.getResetLocalGitRepository() == null) {
            throw new ConfigException("Reset local git repository: missing value.");
        }
        if (config.getApplyDefaultGitConfig() == null) {
            throw new ConfigException("Apply default git config: missing value.");
        }
    }
}
