/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.task;

import br.com.danielferber.gittocc2.config.ConfigException;
import java.io.File;
import java.io.PrintStream;

/**
 *
 * @author Daniel Felix Ferber
 */
public interface ClearCaseStampFileConfig {

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
     * @return If true, update commit hash file.
     */
    Boolean getUpdateCommitStampFile();

    /**
     * @return If true, update sync counter file.
     */
    Boolean getUpdateCounterStampFile();

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

    public static void printConfig(PrintStream ps, ClearCaseStampFileConfig config) {
        ps.println(" * Stamp file configuration:");
        ps.println("   - commit stamp file " + config.getCommitStampFile());
        ps.println("   - counter stamp file " + config.getCounterStampFile());
        ps.println("   - update commit stamp file " + config.getUpdateCommitStampFile());
        ps.println("   - update counter stamp file " + config.getUpdateCounterStampFile());
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

    static void validate(final ClearCaseStampFileConfig wrapped) throws ConfigException {

        if (wrapped.getCommitStampFile() == null) {
            throw new ConfigException("Commit stamp file name: missing value.");
        }
        if (wrapped.getCounterStampFile() == null) {
            throw new ConfigException("Commit counter stamp file name: missing value.");
        }
        if (wrapped.getUpdateCommitStampFile() == null) {
            throw new ConfigException("Update stamp file name: missing value.");
        }
        if (wrapped.getUpdateCounterStampFile() == null) {
            throw new ConfigException("Update counter  stamp file name: missing value.");
        }
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
