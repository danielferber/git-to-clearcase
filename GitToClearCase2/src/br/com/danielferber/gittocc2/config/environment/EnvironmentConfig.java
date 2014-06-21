package br.com.danielferber.gittocc2.config.environment;

import java.io.File;

/**
 *
 * @author Daniel
 */
public interface EnvironmentConfig extends EnvironmentConfigSource {

	EnvironmentConfig setGitExec(File file);

	EnvironmentConfig setRepositoryDir(File dir);
    
	EnvironmentConfig setClearToolExec(File file);
    
	EnvironmentConfig setVobViewDir(File dir);
}
