package br.com.danielferber.gittocc2.cc;

import java.io.File;
import java.io.PrintStream;

/**
 *
 * @author Daniel
 */
public interface CcConfig {

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

        public static void printConfig(PrintStream ps, CcConfig config) {
            ps.println("Cleartool properties:");
            ps.println(" * Executable file: " + config.getClearToolExec());
            ps.println("    - resolves to: " + config.getClearToolAbsoluteExec());
            ps.println(" * VOB view directory: " + config.getVobViewDir());
            ps.println("    - resolves to: " + config.getVobViewAbsoluteDir());
        }
    }

   
}
