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
public interface ClearCasePrepareConfig {

    public static void printConfig(PrintStream ps, ClearCasePrepareConfig config) {
        ps.println(" * Update vob view directory: " + config.getVobViewDirUpdate());
    }

    /**
     * @return If true, update the entire vob before synchronization.
     */
    Boolean getVobViewDirUpdate();
}
