/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.git;

import br.com.danielferber.gittocc2.config.ConfigException;
import java.io.PrintStream;

/**
 * Configuration of Git related tasks executed after synchronization.
 *
 * @author Daniel Felix Ferber
 */
public interface GitFinishConfig {

    /**
     * Writes a readable printout of the configuration.
     *
     * @param ps Printstream that receives the printout.
     * @param config Configuration to print.
     */
    static void printConfig(PrintStream ps, GitFinishConfig config) {
        ps.println(" * Git finish task configuration: N/A.");
    }

    /**
     * Validates the configuration.
     *
     * @param config the configuration to validate.
     * @throws ConfigException thrown for some arbitrary missing or invalid configuration value.
     */
    static void validate(final GitFinishConfig config) throws ConfigException {
        // void
    }

}
