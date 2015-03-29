/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.cc;

import java.io.File;


abstract class CcConfigImpl implements CcConfig {

    @Override
    public File getClearToolAbsoluteExec() {
        return getClearToolExec().getAbsoluteFile();
    }

    @Override
    public File getVobViewAbsoluteDir() {
        return getVobViewDir().getAbsoluteFile();
    }
    
    

}
