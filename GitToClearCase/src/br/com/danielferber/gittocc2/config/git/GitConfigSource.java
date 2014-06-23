package br.com.danielferber.gittocc2.config.git;

import java.io.File;

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
}
