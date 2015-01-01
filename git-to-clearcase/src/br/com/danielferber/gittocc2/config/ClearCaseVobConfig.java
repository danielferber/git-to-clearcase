/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.config;

import java.io.File;
import java.io.PrintStream;

/**
 *
 * @author Daniel Felix Ferber
 */
public interface ClearCaseVobConfig {

    public static void printConfig(PrintStream ps, ClearCaseVobConfig config) {
        ps.println(" * VOB view directory: " + config.getVobViewDir());
        ps.println("   - commit stamp file " + config.getCommitStampFile());
        ps.println("   - counter stamp file " + config.getCounterStampFile());
    }

    /**
     * @return Path to the vob view directory.
     */
    File getVobViewDir();

    /**
     * @return Calculated absolute path of the vob view directory.
     */
    File getVobViewAbsoluteDir();

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

}
