/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.config;

import java.io.File;
import java.util.Properties;

public class EnvironmentConfigProperties implements EnvironmentConfig {

    private final Properties properties;
    private final String prefix;

    public EnvironmentConfigProperties(Properties properties, String prefix) {
        this.properties = properties;
        this.prefix = prefix;
    }

    @Override
    public File getGitExec() {
        final String property = properties.getProperty(prefix + "git.exec");
        return property == null ? null : new File(property);
    }

    @Override
    public File getRepositoryDir() {
        final String property = properties.getProperty(prefix + "repository.dir");
        return property == null ? null : new File(property);
    }

    @Override
    public void setGitExec(File file) {
        properties.setProperty(prefix + "git.exec", file == null ? null : file.getPath());
    }

    @Override
    public void setRepositoryDir(File dir) {
        properties.setProperty(prefix + "repository.dir", dir == null ? null : dir.getPath());
    }

    @Override
    public File getClearToolExec() {
        final String property = properties.getProperty(prefix + "cleartool.exec");
        return property == null ? null : new File(property);
    }

    @Override
    public File getVobViewDir() {
        final String property = properties.getProperty(prefix + "vobview.dir");
        return property == null ? null : new File(property);
    }

    @Override
    public void setClearToolExec(File file) {
        properties.setProperty(prefix + "cleartool.exec", file == null ? null : file.getPath());
    }

    @Override
    public void setVobViewDir(File dir) {
        properties.setProperty(prefix + "vobview.dir", dir == null ? null : dir.getPath());
    }

}
