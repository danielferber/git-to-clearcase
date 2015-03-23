/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.change;

import java.io.File;

public class ChangeConfigValidated implements ChangeConfig {

    private final ChangeConfig config;

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
    public Boolean getUseCommitStampFile() {
        return config.getUseCommitStampFile();
    }

    @Override
    public Boolean getUseCounterStampFile() {
        return config.getUseCounterStampFile();
    }

    @Override
    public String getCommitStampOverride() {
        return config.getCommitStampOverride();
    }

    @Override
    public Long getCounterStampOverride() {
        return config.getCounterStampOverride();
    }

    @Override
    public String readCommitStampFromFile() {
        return config.readCommitStampFromFile();
    }

    @Override
    public long readCounterStampFromFile() {
        return config.readCounterStampFromFile();
    }

    @Override
    public void writeCommitStampFromFile(String commit) {
        config.writeCommitStampFromFile(commit);
    }

    @Override
    public void writeCounterStampFromFile(long counter) {
        config.writeCounterStampFromFile(counter);
    }
}
