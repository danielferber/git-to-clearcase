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
public interface GitPrepareConfig {

    static void printConfig(PrintStream ps, GitPrepareConfig config) {
        ps.println(" * Git preparation:");
        ps.println("    - apply default git configuration: " + config.getApplyDefaultGitConfig());
        ps.println("    - clean repository: " + config.getCleanLocalGitRepository());
        ps.println("    - reset repository: " + config.getResetLocalGitRepository());
        ps.println("    - fetch from remote repository: " + config.getFetchRemoteGitRepository());
        ps.println("    - fast forward repository: " + config.getFastForwardLocalGitRepository());
    }

    Boolean getCleanLocalGitRepository();

    Boolean getFastForwardLocalGitRepository();

    Boolean getFetchRemoteGitRepository();

    Boolean getResetLocalGitRepository();

    Boolean getApplyDefaultGitConfig();

}
