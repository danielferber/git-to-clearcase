/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.change;

import br.com.danielferber.gittocc2.cc.ClearToolConfig;
import java.io.File;

public abstract class ChangeConfigImpl implements ChangeConfig {

    private final ClearToolConfig clearToolConfig;

    public ChangeConfigImpl(ClearToolConfig clearToolConfig) {
        this.clearToolConfig = clearToolConfig;
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
