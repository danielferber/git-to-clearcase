/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.change;

import br.com.danielferber.gittocc2.cc.CcConfig;
import java.io.File;

abstract class ChangeConfigImpl implements ChangeConfig {

    private final CcConfig clearToolConfig;

    public ChangeConfigImpl(CcConfig clearToolConfig) {
        this.clearToolConfig = clearToolConfig;
    }

    @Override
    public boolean doDefineActivity() {
        return this.getActiviyName() != null;
    }

    @Override
    public boolean hasCommitStampFile() {
        return this.getCommitStampFile() != null;
    }

    @Override
    public boolean hasCounterStampFile() {
        return this.getCounterStampFile() != null;
    }

    @Override
    public File getCommitStampAbsoluteFile() {
        if (this.getCommitStampFile() == null) {
            return null;
        }
        return clearToolConfig.getVobViewAbsoluteDir().toPath().resolve(getCommitStampFile().toPath()).toFile();
    }

    @Override
    public File getCounterStampAbsoluteFile() {
        if (this.getCounterStampFile() == null) {
            return null;
        }
        return clearToolConfig.getVobViewAbsoluteDir().toPath().resolve(getCounterStampFile().toPath()).toFile();
    }
}
