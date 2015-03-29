/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.change;

import java.io.File;

/**
 *
 * @author x7ws
 */
public interface ChangeConfig {
    boolean doDefineActivity();
    String getActiviyName();
    boolean hasCommitStampFile();
    boolean hasCounterStampFile();
    File getCommitStampFile();
    File getCounterStampFile();
    File getCommitStampAbsoluteFile();
    File getCounterStampAbsoluteFile();
    String getCommitStampOverride();
    Long getCounterStampOverride();
}
