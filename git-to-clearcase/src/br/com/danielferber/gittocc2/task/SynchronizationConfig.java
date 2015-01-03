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
public interface SynchronizationConfig {

    /**
     * @return If true, use the given counter value instead of value read from
     * counter stamp file.
     */
    Boolean getUseOverriddenCounterStamp();

    /**
     * @return If set, the counter to use instead of value read from counter
     * stamp file.
     */
    Long getOverriddenCounterStamp();

    /**
     * @return If true, use the given commit value instead of value read from
     * commit stamp file.
     */
    Boolean getUseOverriddenCommitStamp();

    /**
     * @return If set, the commit to use instead of value read from commit stamp
     * file.
     */
    String getOverriddenCommitStamp();

    public static void printConfig(PrintStream ps, SynchronizationConfig config) {
        ps.println(" * Synchronization configuration:");
        Boolean useOverriddenCounterStamp = config.getUseOverriddenCounterStamp();
        if (useOverriddenCounterStamp != null) {
            if (useOverriddenCounterStamp) {
                ps.println("   - use overridden counter stamp: " + config.getOverriddenCounterStamp());
            } else {
                ps.println("   - use counter stamp from file");
            }
        }
        Boolean useOverriddenCommitStamp = config.getUseOverriddenCommitStamp();
        if (useOverriddenCommitStamp != null) {
            if (useOverriddenCommitStamp) {
                ps.println("   - use overridden commit stamp: " + config.getOverriddenCommitStamp());
            } else {
                ps.println("   - use commit stamp from file");
            }
        }
    }

    static void validate(final SynchronizationConfig wrapped) {
        if (wrapped.getUseOverriddenCounterStamp() == null) {
            throw new ConfigException("Use overridden counter stamp: missing value.");
        }
        if (wrapped.getUseOverriddenCounterStamp() != null && wrapped.getUseOverriddenCounterStamp() && wrapped.getOverriddenCounterStamp() == null) {
            throw new ConfigException("Overridden counter stamp: missing value.");
        }
        if (wrapped.getUseOverriddenCommitStamp() == null) {
            throw new ConfigException("Use overridden commit stamp: missing value.");
        }
        if (wrapped.getUseOverriddenCommitStamp() != null && wrapped.getUseOverriddenCommitStamp() && wrapped.getOverriddenCommitStamp() == null) {
            throw new ConfigException("Overridden commit stamp: missing value.");
        }
    }

}
