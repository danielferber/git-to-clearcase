/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.git;

import br.com.danielferber.gittocc2.config.ConfigException;
import java.io.File;
import java.io.PrintStream;

/**
 *
 * @author Daniel Felix Ferber
 */
public interface GitConfig {

    /**
     * @return Path to the Git executable.
     */
    File getGitExec();

    File getRepositoryDir();

    /**
     * @return Calculated absolute path of the Git executable.
     */
//    File getGitAbsoluteExec();
    static void printConfig(PrintStream ps, GitConfig config) {
        ps.println(" * Git configuration:");
        ps.println("   - executable file: " + config.getGitExec());
        ps.println(" * Git repository configuration:");
        ps.println("   - directory: " + config.getRepositoryDir());
    }

    static void validate(final GitConfig config) throws ConfigException {
        final File exec = config.getGitExec();
        if (exec == null) {
            throw new ConfigException("Git executable: missing value.");
        }
        if (!exec.exists()) {
            throw new ConfigException("Git executable: does not exist.");
        }
        if (!exec.isFile()) {
            throw new ConfigException("Git executable: not a file.");
        }
        if (!exec.canExecute()) {
            throw new ConfigException("Git executable: not executable.");
        }

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
