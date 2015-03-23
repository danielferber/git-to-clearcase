package br.com.danielferber.gittocc2.cc;

import br.com.danielferber.gittocc2.config.ConfigException;
import java.io.File;
import java.io.PrintStream;

/**
 *
 * @author Daniel
 */
public interface ClearToolConfig {

    /**
     * @return Path to the cleartool executable.
     */
    File getClearToolExec();

    /**
     * @return Path to the vob view directory.
     */
    File getVobViewDir();

    /**
     * @return Calculated absolute path of the cleartool executable.
     */
    File getClearToolAbsoluteExec();

    /**
     * @return Calculated absolute path of the vob view directory.
     */
    File getVobViewAbsoluteDir();

    final class Utils {

        public static void printConfig(PrintStream ps, ClearToolConfig config) {
            ps.println("Cleartool properties:");
            ps.println(" * Executable file: " + config.getClearToolExec());
            ps.println("    - resolves to: " + config.getClearToolAbsoluteExec());
            ps.println(" * VOB view directory: " + config.getVobViewDir());
            ps.println("    - resolves to: " + config.getVobViewAbsoluteDir());
        }
    }

    static void validate(ClearToolConfig config) throws ConfigException {
        final File clearToolExec = config.getClearToolExec();
        if (clearToolExec == null) {
            throw new ConfigException("ClearTool executable: missing property.");
        }
        final File clearToolAbsoluteExec = config.getClearToolAbsoluteExec();
        if (clearToolAbsoluteExec == null) {
            throw new ConfigException("ClearTool absolute executable: missing property.");
        }
        if (!clearToolAbsoluteExec.exists()) {
            throw new ConfigException("ClearTool executable: does not exist.");
        }
        if (!clearToolAbsoluteExec.isFile()) {
            throw new ConfigException("ClearTool executable: not a file.");
        }
        if (!clearToolAbsoluteExec.canExecute()) {
            throw new ConfigException("ClearTool executable: not executable.");
        }

        final File vobViewDir = config.getVobViewDir();
        if (vobViewDir == null) {
            throw new ConfigException("Vob view directory: missing property.");
        }
        final File vobViewAbsoluteDir = config.getVobViewDir();
        if (vobViewAbsoluteDir == null) {
            throw new ConfigException("Vob view absolute directory: missing property.");
        }
        if (!vobViewAbsoluteDir.exists()) {
            throw new ConfigException("Vob view directory: does not exist.");
        }
        if (!vobViewAbsoluteDir.isDirectory()) {
            throw new ConfigException("Vob view directory: not a directory.");
        }
    }
}
