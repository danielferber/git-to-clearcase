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

    static void validate(final GitRepositoryConfig config) {
        final File dir = config.getRepositoryDir();
        if (dir == null) {
            throw new ConfigException("Repository directory: missing value.");
        }
        if (!dir.exists()) {
            throw new ConfigException("Repository directory: does not exist.");
        }
        if (!dir.isDirectory()) {
            throw new ConfigException("Repository directory: not a directory.");
        }
        final File repositoryMetadataDir = new File(dir, ".git");
        if (!repositoryMetadataDir.isDirectory() || !repositoryMetadataDir.isDirectory()) {
            throw new ConfigException("Repository directory: not like a git repository.");
        }
    }
}
