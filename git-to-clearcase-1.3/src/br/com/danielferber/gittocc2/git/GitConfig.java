package br.com.danielferber.gittocc2.git;

import java.io.File;
import java.io.PrintStream;

/**
 *
 * @author Daniel Felix Ferber
 */
public interface GitConfig {

    File getGitExec();

    File getRepositoryDir();

    static void printConfig(PrintStream ps, GitConfig config) {
        ps.println("Git properties:");
        ps.println(" * Executable file: " + config.getGitExec());
        ps.println(" * Repository directory: " + config.getRepositoryDir());
    }
}
