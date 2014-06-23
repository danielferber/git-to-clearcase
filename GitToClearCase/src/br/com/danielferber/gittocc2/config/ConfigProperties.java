/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.config;

import java.io.File;
import java.util.Properties;

/**
 *
 * @author Daniel Felix Ferber
 */
public class ConfigProperties extends Properties {

    public ConfigProperties() {
        super();
    }

    public ConfigProperties(Properties properties) {
        super();
        this.putAll(properties);
    }

    public File getFile(String name) {
        final String property = this.getProperty(name);
        return property == null ? null : new File(property);
    }

    public void setFile(String name, File file) {
        this.setProperty(name, file == null ? "" : file.getPath());
    }

    public Boolean getBoolean(String name) {
        final String property = this.getProperty(name);
        return property == null ? null : Boolean.valueOf(property);
    }

    public void setBoolean(String name, Boolean value) {
        this.setProperty(name, value == null ? "" : Boolean.toString(value));
    }
    
    public Long getLong(String name) {
        final String property = this.getProperty(name);
        return property == null ? null : Long.valueOf(property);
    }

    public void setLong(String name, Long value) {
        this.setProperty(name, value == null ? "" : Long.toString(value));
    }
    
    public String getString(String name) {
        return this.getProperty(name);
    }

    public void setString(String name, String value) {
        this.setProperty(name, value);
    }
}
