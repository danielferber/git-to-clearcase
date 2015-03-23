/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.cc;

import br.com.danielferber.gittocc2.config.ConfigProperties;
import java.io.File;
import java.util.Map;
import java.util.Properties;

public final class ClearToolConfigProperties extends ClearToolConfigImpl {

    public static final String PROP_VOB_VIEW_DIR = "vobview.dir";
    public static final String PROP_CLEAR_TOOL_EXEC = "cleartool.exec";
    private final ConfigProperties properties;

    public ClearToolConfigProperties(final ClearToolConfig other) {
        this.properties = new ConfigProperties();
        this.setClearToolExec(other.getClearToolExec());
        this.setVobViewDir(other.getVobViewDir());
    }

    public ClearToolConfigProperties(final Map<String, String> map) {
        this(map, "");
    }

    public ClearToolConfigProperties(final Map<String, String> map, final String prefix) {
        this.properties = new ConfigProperties();
        this.properties.putAll(map);

    }

    public ClearToolConfigProperties(final Properties properties) {
        this(properties, "");
    }

    public ClearToolConfigProperties(final Properties properties, final String prefix) {
        this.properties = new ConfigProperties(properties);
    }

    @Override
    public File getClearToolExec() {
        return properties.getFile(PROP_CLEAR_TOOL_EXEC);
    }

    @Override
    public File getVobViewDir() {
        return properties.getFile(PROP_VOB_VIEW_DIR);
    }

    public ClearToolConfig setClearToolExec(final File file) {
        properties.setFile(PROP_CLEAR_TOOL_EXEC, file);
        return this;
    }

    public ClearToolConfig setVobViewDir(final File dir) {
        properties.setFile(PROP_VOB_VIEW_DIR, dir);
        return this;
    }

    @Override
    public String toString() {
        return properties.toString();
    }
}
