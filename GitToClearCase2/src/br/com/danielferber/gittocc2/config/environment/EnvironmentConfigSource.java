package br.com.danielferber.gittocc2.config.environment;

import java.io.File;

/**
 *
 * @author Daniel
 */
public interface EnvironmentConfigSource {

    File getGitExec();

    File getRepositoryDir();

    File getClearToolExec();

    File getVobViewDir();
}
