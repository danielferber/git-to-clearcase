/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.cc.config;

import java.io.File;
import java.util.Properties;

public class ClearToolConfigProperties implements ClearToolConfig {

    private final Properties properties;
    private final String prefix;

    public ClearToolConfigProperties(Properties properties, String prefix) {
        this.properties = properties;
        this.prefix = prefix;
    }

    @Override
    public File getClearToolExec() {
        final String property = properties.getProperty(prefix + "cleartoolExec");
        return property == null ? null : new File(property);
    }

    @Override
    public File getVobRootDir() {
        final String property = properties.getProperty(prefix + "vobrootDir");
        return property == null ? null : new File(property);
    }

    @Override
    public void setClearToolExec(File cleartoolExec) {
        properties.setProperty(prefix + "cleartoolExec", cleartoolExec == null ? null : cleartoolExec.getPath());
    }

    @Override
    public void setVobRootDir(File vobrootDir) {
        properties.setProperty(prefix + "vobrootDir", vobrootDir == null ? null : vobrootDir.getPath());
    }

}
