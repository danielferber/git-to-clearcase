/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.git.config;

import java.io.File;
import java.util.Properties;

public class GitConfigProperties implements GitConfig {

    private final Properties properties;
    private final String prefix;

    public GitConfigProperties(Properties properties, String prefix) {
        this.properties = properties;
        this.prefix = prefix;
    }

    @Override
    public File getGitExec() {
        final String property = properties.getProperty(prefix + "gitExec");
        return property == null ? null : new File(property);
    }

    @Override
    public File getRepositoryDir() {
        final String property = properties.getProperty(prefix + "repositoryDir");
        return property == null ? null : new File(property);
    }

    @Override
    public void setGitExec(File gitExec) {
        properties.setProperty(prefix + "gitExec", gitExec == null ? null : gitExec.getPath());
    }

    @Override
    public void setRepositoryDir(File repositoryDir) {
        properties.setProperty(prefix + "repositoryDir", repositoryDir == null ? null : repositoryDir.getPath());
    }

}
