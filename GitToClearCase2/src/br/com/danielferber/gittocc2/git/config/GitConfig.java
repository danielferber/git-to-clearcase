/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.danielferber.gittocc2.git.config;

import java.io.File;

/**
 *
 * @author Daniel
 */
public interface GitConfig {
    File getGitExec();
    File getRepositoryDir();
    void setGitExec(File gitExec);
    void setRepositoryDir(File repositoryDir);
}
