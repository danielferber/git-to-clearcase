/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.cc.config;

import java.io.File;

/**
 *
 * @author Daniel
 */
public class ClearToolConfigValidated implements ClearToolConfig {

    private final ClearToolConfig wrappedClearToolConfig;

    public ClearToolConfigValidated(ClearToolConfig wrappedClearToolConfig) {
        this.wrappedClearToolConfig = wrappedClearToolConfig;
    }

    @Override
    public File getClearToolExec() {
        final File cleartoolExec = wrappedClearToolConfig.getClearToolExec();
        if (cleartoolExec == null) {
            throw new ClearToolConfigException("ClearTool executable: missing property.");
        }
        if (!cleartoolExec.exists()) {
            throw new ClearToolConfigException("ClearTool executable: does not exist.");
        }
        if (!cleartoolExec.isFile()) {
            throw new ClearToolConfigException("ClearTool executable: not a file.");
        }
        if (!cleartoolExec.canExecute()) {
            throw new ClearToolConfigException("ClearTool executable: not executable.");
        }
        return wrappedClearToolConfig.getClearToolExec();
    }

    @Override
    public File getVobRootDir() {
        File vobrootDir = getVobRootDir();
        if (vobrootDir == null) {
            throw new ClearToolConfigException("VobRoot directory: missing property.");
        }
        if (!vobrootDir.exists()) {
            throw new ClearToolConfigException("VobRoot directory: does not exist.");
        }
        if (!vobrootDir.isDirectory()) {
            throw new ClearToolConfigException("VobRoot directory: not a directory.");
        }
        File vobrootMetadataDir = new File(vobrootDir, ".cleartool");
        if (!vobrootMetadataDir.isDirectory() || ! vobrootMetadataDir.isDirectory()) {
            throw new ClearToolConfigException("VobRoot directory: not like a cleartool vobroot.");
        }
        return vobrootDir;
    }

    @Override
    public void setClearToolExec(File cleartoolExec) {
        wrappedClearToolConfig.setClearToolExec(cleartoolExec);
    }

    @Override
    public void setVobRootDir(File vobrootDir) {
        wrappedClearToolConfig.setClearToolExec(vobrootDir);
    }    
}
