package br.com.danielferber.gittocc2.git;

import br.com.danielferber.gittocc2.config.ConfigException;
import java.io.File;
import java.io.PrintStream;

/**
 *
 * @author Daniel Felix Ferber
 */
public interface GitConfig {

    File getGitExec();

    File getRepositoryDir();
    
    File getGitAbsoluteExec();

    File getRepositoryAbsoluteDir();

    static void printConfig(PrintStream ps, GitConfig config) {
        ps.println("Git properties:");
        ps.println(" * Executable file: " + config.getGitExec());
        ps.println("    - resolves to: " + config.getGitAbsoluteExec());
        ps.println(" * Repository directory: " + config.getRepositoryDir());
        ps.println("    - resolves to: " + config.getRepositoryAbsoluteDir());
    }
    
    static void validate(GitConfig config) throws ConfigException {
        final File gitExec = config.getGitExec();
        if (gitExec == null) {
            throw new ConfigException("Git executable: missing value.");
        }
        final File gitAbsoluteExec = config.getGitAbsoluteExec();
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
        
        final File repositoryDir = config.getRepositoryDir();
        if (repositoryDir == null) {
            throw new ConfigException("Repository directory: missing value.");
        }
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
    }
}
