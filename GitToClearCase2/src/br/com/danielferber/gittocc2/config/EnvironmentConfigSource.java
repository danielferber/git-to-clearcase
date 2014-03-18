/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.config;

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
