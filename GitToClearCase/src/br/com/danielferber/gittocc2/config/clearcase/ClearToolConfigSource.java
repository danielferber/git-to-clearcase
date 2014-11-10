package br.com.danielferber.gittocc2.config.clearcase;

import java.io.File;
import java.io.PrintStream;

/**
 *
 * @author Daniel
 */
public interface ClearToolConfigSource {
    /* Clear case environment. */

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

    /* Activity. */
    /**
     * @return If true, reuse or create an activity for file synchronization .
     * If false, reuse the current activity.
     */
    Boolean getUseActivity();

    /**
     * @return If {@link #getUseActivity()} is true, the expected name for
     * the file synchronization activity.
     */
    String getActivityName();

    /* Synchronization tracking. */
    /**
     * @return If true, read and write the last commit hash from/to a file.
     */
    Boolean getUseCommitStampFile();

    /**
     * @return If true, read and write the last sync counter from/to a file.
     */
    Boolean getUseCounterStampFile();

    /**
     * @return Path of the commit stamp file, relative to the vob view
     * directory.
     */
    File getCommitStampFile();

    /**
     * @return Path of the counter stamp file, relative to the vob view
     * directory.
     */
    File getCounterStampFile();

    /**
     * @return Calculated absolute path of the commit stamp file.
     */
    File getCommitStampAbsoluteFile();

    /**
     * @return Calculated absolute path of the counter stamp file.
     */
    File getCounterStampAbsoluteFile();

    /**
     * @return If set, the counter to use instead of value read from counter
     * stamp file.
     */
    Long getOverriddenSyncCounter();

    /**
     * @return If set, the commit to use instead of value read from commit stamp
     * file.
     */
    String getOverriddenSyncFromCommit();

    /* Behavior. */
    /**
     * @return If true, update the entire vob before synchronization.
     */
    Boolean getVobViewDirUpdate();
    
    /**
     * @return if true, check for pending, forgotten checkouts
     */
    Boolean getCheckForgottenCheckout();

    final class Utils {

        public static void printConfig(PrintStream ps, ClearToolConfigSource config) {
            ps.println("Clear case properties:");
            ps.println(" * Executable file: " + config.getClearToolExec());
            ps.println(" * VOB view directory: " + config.getVobViewDir());
            ps.println(" * Error prevention:");
            ps.println("  - Update vob view directory: " + config.getVobViewDirUpdate());
            ps.println("  - Find forgotten checkouts: " + config.getCheckForgottenCheckout());
            ps.println(" * Use activity: " + config.getUseActivity());
            if (config.getUseActivity() != null && config.getUseActivity()) {
                ps.println("   activity name: " + config.getActivityName());
            }
            ps.println("* Synchronization tracking (current state of vob view directory):");
            ps.println("  - Commit hash of last synchronization:");
            boolean nothing = true;
            if (config.getOverriddenSyncFromCommit() != null) {
                ps.println("      initial commit stamp: " + config.getOverriddenSyncFromCommit());
                nothing = false;
            }
            if (config.getUseCommitStampFile() != null && config.getUseCommitStampFile()) {
                ps.println("      commit stamp: file " + config.getCommitStampFile());
                nothing = false;
            }
            if (nothing) {
                ps.println("      do not track commit");
            }
            ps.println("  - Last synchronization number:");
            nothing = true;
            if (config.getOverriddenSyncCounter() != null) {
                ps.println("      initial counter stamp: " + config.getOverriddenSyncCounter());
                nothing = false;
            }
            if (config.getUseCounterStampFile() != null && config.getUseCounterStampFile()) {
                ps.println("      counter stamp: file " + config.getCounterStampFile());
                nothing = false;
            }
            if (nothing) {
                ps.println("      do not track counter");
            }
        }
    }
}
