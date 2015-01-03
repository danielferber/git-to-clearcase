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
public interface GitRepositoryConfig {

    File getRepositoryDir();

    static void printConfig(PrintStream ps, GitRepositoryConfig config) {
        ps.println(" * Git repository configuration:");
        ps.println("   - directory: " + config.getRepositoryDir());
    }

    static void validate(final GitRepositoryConfig config) throws ConfigException {
        final File dir = config.getRepositoryDir();
        if (dir == null) {
            throw new ConfigException("Repository directory: missing value.");
        }
        final File absoluteDir = config.getRepositoryDir();
        if (!absoluteDir.exists()) {
            throw new ConfigException("Repository directory: does not exist.");
        }
        if (!absoluteDir.isDirectory()) {
            throw new ConfigException("Repository directory: not a directory.");
        }
        final File repositoryMetadataDir = new File(absoluteDir, ".git");
        if (!repositoryMetadataDir.isDirectory() || !repositoryMetadataDir.isDirectory()) {
            throw new ConfigException("Repository directory: not like a git repository.");
        }
    }
}
