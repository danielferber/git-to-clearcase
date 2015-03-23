/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.cc;

import br.com.danielferber.gittocc2.config.ConfigException;
import java.io.File;


public class ClearToolConfigValidated implements ClearToolConfig {

    private final ClearToolConfig config;

    public ClearToolConfigValidated(ClearToolConfig config) {
        this.config = config;
    }

    @Override
    public File getClearToolExec() {
        final File clearToolExec = config.getClearToolExec();
        if (clearToolExec == null) {
            throw new ConfigException("ClearTool executable: missing property.");
        }
        return clearToolExec;
    }

    @Override
    public File getVobViewDir() {
        final File vobViewDir = config.getVobViewDir();
        if (vobViewDir == null) {
            throw new ConfigException("Vob view directory: missing property.");
        }
        return vobViewDir;
    }

    @Override
    public File getClearToolAbsoluteExec() {
        final File clearToolAbsoluteExec = config.getClearToolAbsoluteExec();
        if (clearToolAbsoluteExec == null) {
            throw new ConfigException("ClearTool absolute executable: missing property.");
        }
        if (!clearToolAbsoluteExec.exists()) {
            throw new ConfigException("ClearTool executable: does not exist.");
        }
        if (!clearToolAbsoluteExec.isFile()) {
            throw new ConfigException("ClearTool executable: not a file.");
        }
        if (!clearToolAbsoluteExec.canExecute()) {
            throw new ConfigException("ClearTool executable: not executable.");
        }
        return clearToolAbsoluteExec;
    }

    @Override
    public File getVobViewAbsoluteDir() {
        final File vobViewAbsoluteDir = config.getVobViewDir();
        if (vobViewAbsoluteDir == null) {
            throw new ConfigException("Vob view absolute directory: missing property.");
        }
        if (!vobViewAbsoluteDir.exists()) {
            throw new ConfigException("Vob view directory: does not exist.");
        }
        if (!vobViewAbsoluteDir.isDirectory()) {
            throw new ConfigException("Vob view directory: not a directory.");
        }
        return vobViewAbsoluteDir;
    }
}
