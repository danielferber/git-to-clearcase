/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.git;

import br.com.danielferber.gittocc2.config.ConfigException;
import java.io.PrintStream;

/**
 * Configuration of Git related tasks executed before synchronization.
 *
 * @author Daniel Felix Ferber
 */
public interface GitPrepareConfig {

    /**
     * @return if the local git repository shall be cleaned for untracked files or directories.
     */
    Boolean getCleanLocalGitRepository();

    /**
     * @return if the local git repository shall be fast forwarded after fetch.
     */
    Boolean getFastForwardLocalGitRepository();

    /**
     * @return if the commits are fetched from the remote repository.
     */
    Boolean getFetchRemoteGitRepository();

    /**
     * @return if a hard reset shall be applied to the local git repository. The hard reset reverts all changed files.
     */
    Boolean getResetLocalGitRepository();

    /**
     * @return if default properties shall be (over)written on the local repository.
     */
    Boolean getApplyDefaultGitConfig();

    /**
     * Writes a readable printout of the configuration.
     *
     * @param ps Printstream that receives the printout.
     * @param config Configuration to print.
     */
    static void printConfig(PrintStream ps, GitPrepareConfig config) {
        ps.println(" * Git prepare task configuration:");
        ps.println("    - apply default git configuration: " + config.getApplyDefaultGitConfig());
        ps.println("    - clean repository: " + config.getCleanLocalGitRepository());
        ps.println("    - reset repository: " + config.getResetLocalGitRepository());
        ps.println("    - fetch from remote repository: " + config.getFetchRemoteGitRepository());
        ps.println("    - fast forward repository: " + config.getFastForwardLocalGitRepository());
    }

    /**
     * Validates the configuration.
     *
     * @param config the configuration to validate.
     * @throws ConfigException thrown for some arbitrary missing or invalid configuration value.
     */
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
