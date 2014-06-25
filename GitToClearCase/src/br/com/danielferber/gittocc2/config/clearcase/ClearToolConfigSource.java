package br.com.danielferber.gittocc2.config.clearcase;

import br.com.danielferber.gittocc2.config.git.GitConfigSource;
import java.io.File;
import java.io.PrintStream;

/**
 *
 * @author Daniel
 */
public interface ClearToolConfigSource {

    String getActivityMessagePattern();

    File getClearToolExec();

    File getCommitStampFileName();

    File getCounterStampFileName();

    Boolean getCreateActivity();

    Long getOverriddenSyncCounter();

    String getOverriddenSyncFromCommit();

    Boolean getupdateVobViewDir();

    File getVobViewDir();

    File getCommitStampAbsoluteFile();

    File getCounterStampAbsoluteFile();

    final class Utils {

        public static void printConfig(PrintStream ps, ClearToolConfigSource config) {
            ps.println("ClearTools properties:");
            ps.println(" * Executable file: " + config.getClearToolExec());
            ps.println(" * VOB view directory: " + config.getVobViewDir());
            ps.println(" * Before synchronizing, do:");
            ps.println("    - update VOB view: " + config.getupdateVobViewDir());
            ps.println("    - create new activity: " + config.getCreateActivity());
            if (config.getCreateActivity() != null && config.getCreateActivity()) {
                ps.println("      activity message pattern: " + config.getActivityMessagePattern());
            }
            if (config.getOverriddenSyncFromCommit() != null) {
                ps.println("    - assume current vob view at commit (ignoring stamp file): " + config.getOverriddenSyncFromCommit());
            } else if (config.getCommitStampFileName() != null) {
                ps.println("    - read current vob view at commit from stamp file: " + config.getCommitStampAbsoluteFile());
            }
            if (config.getOverriddenSyncCounter() != null) {
                ps.println("    - assume current vob view at commit (ignoring stamp file): " + config.getOverriddenSyncCounter());
            } else if (config.getCounterStampFileName() != null) {
                ps.println("    - read current vob view at commit from stamp file: " + config.getCounterStampAbsoluteFile());
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
