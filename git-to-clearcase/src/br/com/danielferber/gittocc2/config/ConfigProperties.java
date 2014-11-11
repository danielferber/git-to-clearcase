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
	private static final long serialVersionUID = 1L;

    public ConfigProperties() {
        super();
    }

    public ConfigProperties(final Properties properties) {
        super();
        this.putAll(properties);
    }

    public File getFile(final String name) {
        final String property = this.getProperty(name);
        return property == null ? null : new File(property);
    }

    public void setFile(final String name, final File file) {
        this.setProperty(name, file == null ? "" : file.getPath());
    }

    public Boolean getBoolean(final String name) {
        final String property = this.getProperty(name);
        return property == null ? null : Boolean.valueOf(property);
    }

    public void setBoolean(final String name, final Boolean value) {
        this.setProperty(name, value == null ? "" : Boolean.toString(value));
    }

    public Long getLong(final String name) {
        final String property = this.getProperty(name);
        return property == null ? null : Long.valueOf(property);
    }

    public void setLong(final String name, final Long value) {
        this.setProperty(name, value == null ? "" : Long.toString(value));
    }

    public String getString(final String name) {
        return this.getProperty(name);
    }

    public void setString(final String name, final String value) {
        this.setProperty(name, value);
    }
}
