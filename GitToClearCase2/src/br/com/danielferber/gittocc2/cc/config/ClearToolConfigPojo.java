/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.cc.config;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author Daniel
 */
public class ClearToolConfigPojo implements ClearToolConfig, Serializable {

    private File cleartoolExec;
    private File vobrootDir;

    public ClearToolConfigPojo() {
        super();
    }

    public ClearToolConfigPojo(File cleartoolExec, File vobrootDir) {
        this.cleartoolExec = cleartoolExec;
        this.vobrootDir = vobrootDir;
    }

    public ClearToolConfigPojo(ClearToolConfig other) {
        this.cleartoolExec = other.getClearToolExec();
        this.vobrootDir = other.getVobRootDir();
    }

    @Override
    public File getClearToolExec() {
        return cleartoolExec;
    }

    @Override
    public File getVobRootDir() {
        return vobrootDir;
    }

    @Override
    public void setClearToolExec(File cleartoolExec) {
        this.cleartoolExec = cleartoolExec;
    }

    @Override
    public void setVobRootDir(File vobrootDir) {
        this.vobrootDir = vobrootDir;
    }
}
