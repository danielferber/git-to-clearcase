package br.com.danielferber.gittocc2.config.clearcase;

import java.io.File;
import java.io.PrintStream;

/**
 *
 * @author Daniel
 */
public interface ClearToolConfigSource {

    Boolean getUseSyncActivity();

    Boolean getUseStampActivity();

    String getSyncActivityName();

    String getStampActivityName();

    File getClearToolExec();

    File getCommitStampFileName();

    File getCounterStampFileName();

    Long getOverriddenSyncCounter();

    String getOverriddenSyncFromCommit();

    Boolean getUpdateVobViewDir();

    File getVobViewDir();

    File getCommitStampAbsoluteFile();

    File getCounterStampAbsoluteFile();

    final class Utils {

        public static void printConfig(PrintStream ps, ClearToolConfigSource config) {
            ps.println("ClearTools properties:");
            ps.println(" * Executable file: " + config.getClearToolExec());
            ps.println(" * VOB view directory: " + config.getVobViewDir());
            ps.println(" * Before synchronizing, do:");
            ps.println("    - update VOB view: " + config.getUpdateVobViewDir());
            ps.println("    - create new activity: " + config.getUseSyncActivity());
            if (config.getUseSyncActivity() != null && config.getUseSyncActivity()) {
                ps.println("      activity name: " + config.getSyncActivityName());
            }
            if (config.getUseStampActivity() != null && config.getUseStampActivity()) {
                ps.println("      activity name: " + config.getStampActivityName());
            }
            if (config.getOverriddenSyncFromCommit() != null) {
                ps.println("    - assume current vob view at commit (ignoring stamp file): " + config.getOverriddenSyncFromCommit());
            } else if (config.getCommitStampFileName() != null) {
                ps.println("    - read current vob view at commit from stamp file: " + config.getCommitStampAbsoluteFile());
            }
            if (config.getOverriddenSyncCounter() != null) {
                ps.println("    - assume counter (ignoring stamp file): " + config.getOverriddenSyncCounter());
            } else if (config.getCounterStampFileName() != null) {
                ps.println("    - read counter from stamp file: " + config.getCounterStampAbsoluteFile());
            }
            ps.println(" * After synchronizing, do:");
            if (config.getCommitStampFileName() != null) {
                ps.println("    - write resulting commit to stamp file: " + config.getCommitStampAbsoluteFile());
            }
            if (config.getCounterStampFileName() != null) {
                ps.println("    - write resulting counter to stamp file: " + config.getCounterStampAbsoluteFile());
            }
            if (config.getCommitStampFileName() == null && config.getCounterStampFileName() == null) {
                ps.println("    - nothing");
            }
        }
    }
}
