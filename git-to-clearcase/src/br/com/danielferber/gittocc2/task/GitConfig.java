/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.task;

import br.com.danielferber.gittocc2.config.ConfigException;
import java.io.File;
import java.io.PrintStream;

/**
 *
 * @author Daniel Felix Ferber
 */
public interface GitConfig {

    /**
     * @return Path to the Git executable.
     */
    File getGitExec();

    /**
     * @return Calculated absolute path of the Git executable.
     */
//    File getGitAbsoluteExec();
    static void printConfig(PrintStream ps, GitConfig config) {
        ps.println(" * Executable file: " + config.getGitExec());
    }

    static void validate(final GitConfig config) throws ConfigException {
        final File exec = config.getGitExec();
        if (exec == null) {
            throw new ConfigException("Git executable: missing value.");
        }
        if (!exec.exists()) {
            throw new ConfigException("Git executable: does not exist.");
        }
        if (!exec.isFile()) {
            throw new ConfigException("Git executable: not a file.");
        }
        if (!exec.canExecute()) {
            throw new ConfigException("Git executable: not executable.");
        }
    }

}
