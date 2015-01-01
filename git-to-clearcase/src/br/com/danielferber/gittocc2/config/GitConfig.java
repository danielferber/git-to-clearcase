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
public interface GitConfig {

    static void printConfig(PrintStream ps, GitConfig config) {
        ps.println(" * Executable file: " + config.getGitExec());
    }

    /**
     * @return Path to the Git executable.
     */
    File getGitExec();

    /**
     * @return Calculated absolute path of the Git executable.
     */
//    File getGitAbsoluteExec();

}
