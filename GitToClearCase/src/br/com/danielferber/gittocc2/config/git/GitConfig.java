package br.com.danielferber.gittocc2.config.git;

import java.io.File;

/**
 *
 * @author Daniel Felix Ferber
 */
public interface GitConfig extends GitConfigSource {

    GitConfig setCleanLocalGitRepository(Boolean value);

    GitConfig setFastForwardLocalGitRepository(Boolean value);

    GitConfig setFetchRemoteGitRepository(Boolean value);

    GitConfig setGitExec(File file);

    GitConfig setRepositoryDir(File dir);

    GitConfig setResetLocalGitRepository(Boolean value);
}
