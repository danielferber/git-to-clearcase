package br.com.danielferber.gittocc2.config.git;

import br.com.danielferber.gittocc2.config.GitConfig;
import br.com.danielferber.gittocc2.config.GitFinishConfig;
import br.com.danielferber.gittocc2.config.GitPrepareConfig;
import br.com.danielferber.gittocc2.config.GitRepositoryConfig;

/**
 *
 * @author Daniel Felix Ferber
 */
public interface GitConfigSource extends GitConfig, GitPrepareConfig, GitFinishConfig, GitRepositoryConfig {

}
