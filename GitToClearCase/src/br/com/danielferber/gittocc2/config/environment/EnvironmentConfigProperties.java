/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.config.environment;

import br.com.danielferber.gittocc2.config.ConfigProperties;
import java.io.File;
import java.util.Map;
import java.util.Properties;

public class EnvironmentConfigProperties implements EnvironmentConfig {

    private final ConfigProperties properties;
    private final String prefix;

    public EnvironmentConfigProperties(EnvironmentConfigSource other, String prefix) {
        this.properties = new ConfigProperties();
        this.prefix = prefix;
        this.setGitExec(other.getGitExec());
        this.setRepositoryDir(other.getRepositoryDir());
        this.setClearToolExec(other.getClearToolExec());
        this.setVobViewDir(other.getVobViewDir());
    }

    public EnvironmentConfigProperties(EnvironmentConfigSource other) {
        this(other, "");
    }

    public EnvironmentConfigProperties(Properties properties, String prefix) {
        this.properties = new ConfigProperties(properties);
        this.prefix = prefix;
    }

    public EnvironmentConfigProperties(Properties properties) {
        this(properties, "");
    }

    public EnvironmentConfigProperties(Map<String, String> map, String prefix) {
        this.properties = new ConfigProperties();
        this.properties.putAll(map);
        this.prefix = prefix;

    }

    public EnvironmentConfigProperties(Map<String, String> map) {
        this(map, "");
    }

    public Properties getProperties() {
        return new Properties(properties);
    }

    @Override
    public File getGitExec() {
        return properties.getFile(prefix + "git.exec");
    }

    @Override
    public File getRepositoryDir() {
        return properties.getFile(prefix + "repository.dir");
    }

    @Override
    public EnvironmentConfig setGitExec(File file) {
        properties.setFile(prefix + "git.exec", file);
        return this;
    }

    @Override
    public EnvironmentConfig setRepositoryDir(File dir) {
        properties.setFile(prefix + "repository.dir", dir);
        return this;
    }

    @Override
    public File getClearToolExec() {
        return properties.getFile(prefix + "cleartool.exec");
    }

    @Override
    public File getVobViewDir() {
        return properties.getFile(prefix + "vobview.dir");
    }

    @Override
    public EnvironmentConfig setClearToolExec(File file) {
        properties.setFile(prefix + "cleartool.exec", file);
        return this;
    }

    @Override
    public EnvironmentConfig setVobViewDir(File dir) {
        properties.setFile(prefix + "vobview.dir", dir);
        return this;
    }

    @Override
    public String toString() {
        return properties.toString();
    }
}
