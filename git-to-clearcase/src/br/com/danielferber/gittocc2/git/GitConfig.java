package br.com.danielferber.gittocc2.git;

import br.com.danielferber.gittocc2.config.ConfigException;
import java.io.File;
import java.io.PrintStream;

/**
 * Git executable and repository directory configuration. For convenience, the repository directory is also the working
 * directory.
 *
 * @author Daniel Felix Ferber
 */
public interface GitConfig {

    /**
     * @return Path to the Git executable.
     */
    File getGitExec();

    /**
     * @return Calculated absolute path to the Git executable.
     */
    File getGitAbsoluteExec();

    /**
     * @return Path to the Git repository.
     */
    File getRepositoryDir();

    /**
     * @return Calculated absolute path to the Git repository.
     */
    File getRepositoryAbsoluteDir();

    /**
     * Writes a readable printout of the configuration.
     *
     * @param ps Printstream that receives the printout.
     * @param config Configuration to print.
     */
    static void printConfig(PrintStream ps, GitConfig config) {
        ps.println(" * Git configuration:");
        ps.println("   - executable file: " + config.getGitExec());
        ps.println("   - resolves to: " + config.getGitAbsoluteExec());
        ps.println(" * Git repository configuration:");
        ps.println("   - directory: " + config.getRepositoryDir());
        ps.println("   - resolves to: " + config.getRepositoryAbsoluteDir());
    }

    /**
     * Validates the configuration.
     *
     * @param config the configuration to validate.
     * @throws ConfigException thrown for some arbitrary missing or invalid configuration value.
     */
    static void validate(final GitConfig config) throws ConfigException {
        final File exec = config.getGitExec();
        if (exec == null) {
            throw new ConfigException("Git executable: missing value.");
        }
        final File absoluteExec = config.getGitAbsoluteExec();
        if (!absoluteExec.exists()) {
            throw new ConfigException("Git executable: does not exist.");
        }
        if (!absoluteExec.isFile()) {
            throw new ConfigException("Git executable: not a file.");
        }
        if (!absoluteExec.canExecute()) {
            throw new ConfigException("Git executable: not executable.");
        }

        final File dir = config.getRepositoryDir();
        if (dir == null) {
            throw new ConfigException("Git repository directory: missing value.");
        }
        final File absoluteDir = config.getRepositoryDir();
        if (!absoluteDir.exists()) {
            throw new ConfigException("Git repository directory: does not exist.");
        }
        if (!absoluteDir.isDirectory()) {
            throw new ConfigException("Git repository directory: not a directory.");
        }
        final File repositoryMetadataDir = new File(absoluteDir, ".git");
        if (!repositoryMetadataDir.isDirectory() || !repositoryMetadataDir.isDirectory()) {
            throw new ConfigException("Git repository directory: does not look like a git repository.");
        }
    }

}
