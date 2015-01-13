/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.cleartool;

import br.com.danielferber.gittocc2.config.ConfigException;
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

    /**
     * @return Path to the vob view directory.
     */
    File getVobViewDir();

    /**
     * @return Calculated absolute path of the vob view directory.
     */
    File getVobViewAbsoluteDir();

    public static void printConfig(PrintStream ps, ClearToolConfig config) {
        ps.println(" * ClearTool configuration:");
        ps.println("   - executable file: " + config.getClearToolExec());
        ps.println(" * ClearCase VOB configuration:");
        ps.println("   - directory: " + config.getVobViewDir());
    }

    static void validate(final ClearToolConfig wrapped) throws ConfigException {
        final File exec = wrapped.getClearToolExec();
        if (exec == null) {
            throw new ConfigException("ClearTool executable: missing property.");
        }
        final File absoluteExec = wrapped.getClearToolAbsoluteExec();
        if (!absoluteExec.exists()) {
            throw new ConfigException("ClearTool executable: does not exist.");
        }
        if (!absoluteExec.isFile()) {
            throw new ConfigException("ClearTool executable: not a file.");
        }
        if (!absoluteExec.canExecute()) {
            throw new ConfigException("ClearTool executable: not executable.");
        }

        final File dir = wrapped.getVobViewDir();
        if (dir == null) {
            throw new ConfigException("Vob view directory: missing property.");
        }
        if (!dir.exists()) {
            throw new ConfigException("Vob view directory: does not exist.");
        }
        if (!dir.isDirectory()) {
            throw new ConfigException("Vob view directory: not a directory.");
        }
    }
}
