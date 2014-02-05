/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.cc.config;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

public class ClearToolConfigBean implements ClearToolConfig {

    private final ClearToolConfig wrappedClearToolConfig;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    public static final String VOBROOT_DIR_PROPERTY = "vobrootDir";
    public static final String CLEARTOOL_EXEC_PROPERTY = "cleartoolExec";

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    public ClearToolConfigBean() {
        super();
        this.wrappedClearToolConfig = new ClearToolConfigPojo();
    }

    public ClearToolConfigBean(ClearToolConfig other) {
        this.wrappedClearToolConfig = other;
    }

    @Override
    public File getClearToolExec() {
        return wrappedClearToolConfig.getClearToolExec();
    }

    @Override
    public File getVobRootDir() {
        return wrappedClearToolConfig.getVobRootDir();
    }

    @Override
    public void setClearToolExec(File cleartoolExec) {
        final File oldValue = wrappedClearToolConfig.getClearToolExec();
        wrappedClearToolConfig.setClearToolExec(cleartoolExec);
        this.pcs.firePropertyChange(CLEARTOOL_EXEC_PROPERTY, oldValue, cleartoolExec);
    }

    @Override
    public void setVobRootDir(File vobrootDir) {
        final File oldValue = wrappedClearToolConfig.getVobRootDir();
        wrappedClearToolConfig.setVobRootDir(vobrootDir);
        this.pcs.firePropertyChange(VOBROOT_DIR_PROPERTY, oldValue, vobrootDir);
    }
}
