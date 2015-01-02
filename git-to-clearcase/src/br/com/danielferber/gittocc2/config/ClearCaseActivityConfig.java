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
public interface ClearCaseActivityConfig {

    /**
     * @return If true, reuse or create an activity for file synchronization .
     * If false, reuse the current activity.
     */
    Boolean getUseActivity();

    /**
     * @return If {@link #getUseActivity()} is true, the expected name for the
     * file synchronization activity.
     */
    String getActivityName();

    public static void printConfig(PrintStream ps, ClearCaseActivityConfig config) {
        ps.println(" * Use activity: " + config.getUseActivity());
        if (config.getUseActivity() != null && config.getUseActivity()) {
            ps.println("   activity name: " + config.getActivityName());
        }
    }

    static void validate(final ClearCaseActivityConfig wrapped) {
        if (wrapped.getUseActivity() && wrapped.getActivityName() == null) {
            throw new ConfigException("Sync activity name: missing value.");
        }
        if (wrapped.getUseActivity() == null) {
            throw new ConfigException("Use sync activity: missing value.");
        }
    }
}
