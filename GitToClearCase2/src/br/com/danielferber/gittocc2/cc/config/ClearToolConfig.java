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
public interface ClearToolConfig {
    File getClearToolExec();
    File getVobRootDir();
    void setClearToolExec(File clearToolExec);
    void setVobRootDir(File vobrootDir);
}
