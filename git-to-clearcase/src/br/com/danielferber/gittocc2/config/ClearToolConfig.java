/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.config;

import java.io.File;
import java.io.PrintStream;

/**
 *
 * @author Daniel Felix Ferber
 */
public interface ClearToolConfig {

    /**
     * @return Path to the cleartool executable.
     */
    File getClearToolExec();

    /**
     * @return Calculated absolute path of the cleartool executable.
     */
    File getClearToolAbsoluteExec();

    public static void printConfig(PrintStream ps, ClearToolConfig config) {
        ps.println(" * Executable file: " + config.getClearToolExec());
    }

    static void validate(final ClearToolConfig wrapped) {
        final File exec = wrapped.getClearToolExec();
        if (exec == null) {
            throw new ConfigException("ClearTool executable: missing property.");
        }
        if (!exec.exists()) {
            throw new ConfigException("ClearTool executable: does not exist.");
        }
        if (!exec.isFile()) {
            throw new ConfigException("ClearTool executable: not a file.");
        }
        if (!exec.canExecute()) {
            throw new ConfigException("ClearTool executable: not executable.");
        }

    }
}
