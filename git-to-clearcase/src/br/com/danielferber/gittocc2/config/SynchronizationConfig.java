/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.config;

import java.io.PrintStream;

/**
 *
 * @author Daniel Felix Ferber
 */
public interface SynchronizationConfig {

    public static void printConfig(PrintStream ps, SynchronizationConfig config) {
        ps.println("* Synchronization tracking (current state of vob view directory):");
        ps.println("  - Commit hash of last synchronization:");
        boolean nothing = true;
        if (config.getOverriddenSyncFromCommit() != null) {
            ps.println("      initial commit stamp: " + config.getOverriddenSyncFromCommit());
            nothing = false;
        }
        if (config.getUseCommitStampFile() != null && config.getUseCommitStampFile()) {
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
            nothing = false;
        }
        if (nothing) {
            ps.println("      do not track counter");
        }
    }

    /**
     * @return If true, read and write the last commit hash from/to a file.
     */
    Boolean getUseCommitStampFile();

    /**
     * @return If true, read and write the last sync counter from/to a file.
     */
    Boolean getUseCounterStampFile();

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
}
