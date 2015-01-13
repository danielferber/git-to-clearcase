/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.task;

import br.com.danielferber.gittocc2.config.ConfigException;
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
    Boolean getCreateActivity();

    /**
     * @return If {@link #getCreateActivity()} is true, the expected name for the
     * file synchronization activity.
     */
    String getActivityNamePattern();
    
    public static void printConfig(PrintStream ps, ClearCaseActivityConfig config) {
        ps.println(" * ClearCase Activity configuration:");
        final Boolean createActivity = config.getCreateActivity();
        if (createActivity != null) {
            if (createActivity) {
                ps.println("   - create activity with pattern: " + config.getActivityNamePattern());
            } else {
                ps.println("   - reuse activity");
            }
        }
    }

    static void validate(final ClearCaseActivityConfig wrapped) throws ConfigException {
        if (wrapped.getCreateActivity() == null) {
            throw new ConfigException("Use sync activity: missing value.");
        }
        if (wrapped.getCreateActivity() != null && wrapped.getCreateActivity() && wrapped.getActivityNamePattern() == null) {
            throw new ConfigException("Sync activity name: missing value.");
        }
    }
}
