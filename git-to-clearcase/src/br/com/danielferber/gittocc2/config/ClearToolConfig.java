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
public interface ClearToolConfig {

    public static void printConfig(PrintStream ps, ClearToolConfig config) {
        ps.println(" * Executable file: " + config.getClearToolExec());
    }

    /**
     * @return Path to the cleartool executable.
     */
    File getClearToolExec();

    /**
     * @return Calculated absolute path of the cleartool executable.
     */
    File getClearToolAbsoluteExec();

}
