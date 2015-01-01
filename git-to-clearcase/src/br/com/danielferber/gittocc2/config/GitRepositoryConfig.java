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
public interface GitRepositoryConfig {

    static void printConfig(PrintStream ps, GitRepositoryConfig config) {
        ps.println(" * Repository directory: " + config.getRepositoryDir());
    }
    
    File getRepositoryDir();
}
