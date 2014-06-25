package br.com.danielferber.gittocc2.config.git;

import java.io.File;
import java.io.PrintStream;

/**
 *
 * @author Daniel Felix Ferber
 */
public interface GitConfigSource {

    Boolean getCleanLocalGitRepository();

    Boolean getFastForwardLocalGitRepository();

    Boolean getFetchRemoteGitRepository();

    File getGitExec();

    File getRepositoryDir();

    Boolean getResetLocalGitRepository();

    Boolean getApplyDefaultGitConfig();

    final class Utils {

        public static void printConfig(PrintStream ps, GitConfigSource config) {
            ps.println("Git properties:");
            ps.println(" * Executable file: " + config.getGitExec());
            ps.println(" * Repository directory: " + config.getRepositoryDir());
            ps.println(" * Before synchronizing, do:");
            ps.println("    - apply default git configuration: " + config.getApplyDefaultGitConfig());
            ps.println("    - clean repository: " + config.getCleanLocalGitRepository());
            ps.println("    - reset repository: " + config.getResetLocalGitRepository());
            ps.println("    - fetch from remote repository: " + config.getFetchRemoteGitRepository());
            ps.println("    - fast forward repository: " + config.getFastForwardLocalGitRepository());
        }
    }
}
