/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.git;

import br.com.danielferber.gittocc2.config.ConfigException;
import java.io.File;

/**
 *
 * @author x7ws
 */
public class GitConfigValidated implements GitConfig {

    private final GitConfig config;

    public GitConfigValidated(GitConfig wrapped) {
        this.config = wrapped;
    }

    @Override
    public File getGitExec() {
        final File gitExec = config.getGitExec();
        if (gitExec == null) {
            throw new ConfigException("Git executable: missing value.");
        }
        return gitExec;
    }

    @Override
    public File getRepositoryDir() {
        final File repositoryDir = config.getRepositoryDir();
        if (repositoryDir == null) {
            throw new ConfigException("Repository directory: missing value.");
        }
        return repositoryDir;
    }

    @Override
    public File getGitAbsoluteExec() {
        final File gitAbsoluteExec = config.getGitExec();
        if (gitAbsoluteExec == null) {
            throw new ConfigException("Git absolute executable: missing value.");
        }
        if (!gitAbsoluteExec.exists()) {
            throw new ConfigException("Git executable: does not exist.");
        }
        if (!gitAbsoluteExec.isFile()) {
            throw new ConfigException("Git executable: not a file.");
        }
        if (!gitAbsoluteExec.canExecute()) {
            throw new ConfigException("Git executable: not executable.");
        }
        return gitAbsoluteExec;
    }

    @Override
    public File getRepositoryAbsoluteDir() {
        final File repositoryAbsoluteDir = config.getRepositoryAbsoluteDir();
        if (repositoryAbsoluteDir == null) {
            throw new ConfigException("Repository absolute directory: missing value.");
        }
        if (!repositoryAbsoluteDir.exists()) {
            throw new ConfigException("Repository directory: does not exist.");
        }
        if (!repositoryAbsoluteDir.isDirectory()) {
            throw new ConfigException("Repository directory: not a directory.");
        }
        final File repositoryMetadataDir = new File(repositoryAbsoluteDir, ".git");
        if (!repositoryMetadataDir.isDirectory() || !repositoryMetadataDir.isDirectory()) {
            throw new ConfigException("Repository directory: not like a git repository.");
        }
        return repositoryAbsoluteDir;
    }
}
