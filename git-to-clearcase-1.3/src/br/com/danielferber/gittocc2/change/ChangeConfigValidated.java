/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.change;

import java.io.File;

public class ChangeConfigValidated implements ChangeConfig {

    private final ChangeConfig config;

    @Override
    public boolean doDefineActivity() {
        return config.doDefineActivity();
    }

    @Override
    public boolean hasCommitStampFile() {
        return config.hasCommitStampFile();
    }

    @Override
    public boolean hasCounterStampFile() {
        return config.hasCounterStampFile();
    }

    @Override
    public String getActiviyName() {
        return config.getActiviyName();
    }

    public ChangeConfigValidated(ChangeConfig config) {
        this.config = config;
    }

    @Override
    public File getCommitStampFile() {
        return config.getCommitStampFile();
    }

    @Override
    public File getCounterStampFile() {
        return config.getCounterStampFile();
    }

    @Override
    public File getCommitStampAbsoluteFile() {
        return config.getCommitStampAbsoluteFile();
    }

    @Override
    public File getCounterStampAbsoluteFile() {
        return config.getCounterStampAbsoluteFile();
    }

    @Override
    public String getCommitStampOverride() {
        return config.getCommitStampOverride();
    }

    @Override
    public Long getCounterStampOverride() {
        return config.getCounterStampOverride();
    }
}
