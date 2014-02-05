/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.danielferber.gittocc2.sync.config;

/**
 *
 * @author Daniel
 */
public interface SyncConfig {
    boolean getUpdateVobRoot();
    boolean getFetchRemoteGitRepository();
    boolean getFastForwardLocalGitRepository();
    boolean getResetLocalGitRepository();
    boolean getCleanLocalGitRepository();
    boolean getCreateActivity();
    String getActivityMessagePattern();
    
    void setUpdateVobRoot(boolean value);
    void setFetchRemoteGitRepository(boolean value);
    void setFastForwardLocalGitRepository(boolean value);
    void setResetLocalGitRepository(boolean value);
    void setCleanLocalGitRepository(boolean value);
    void setCreateActivity(boolean value);
    void setActivityMessagePattern(String value);
}
