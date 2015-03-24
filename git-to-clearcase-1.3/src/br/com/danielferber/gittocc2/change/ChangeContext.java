/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.change;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author x7ws
 */
public class ChangeContext {

    private Queue<ChangeSet> changeSetCollection = new LinkedList<>();
//    private String sourceCommit = null;
//    private String targetCommit = null;
//    private Long sourceCounter = null;
//    private Long targetCounter = null;

    public void addChangeSet(ChangeSet changeSet) {
        this.changeSetCollection.add(changeSet);
    }
    
    public ChangeSet nextChangeSet() {
        return this.changeSetCollection.poll();
    }

//    public String getSourceCommit() {
//        return sourceCommit;
//    }
//
//    public void setSourceCommit(String sourceCommit) {
//        this.sourceCommit = sourceCommit;
//    }
//
//    public String getTargetCommit() {
//        return targetCommit;
//    }
//
//    public void setTargetCommit(String targetCommit) {
//        this.targetCommit = targetCommit;
//    }
//
//    public Long getSourceCounter() {
//        return sourceCounter;
//    }
//
//    public void setSourceCounter(Long sourceCounter) {
//        this.sourceCounter = sourceCounter;
//        this.targetCounter = sourceCounter + 1;
//    }
//
//    public Long getTargetCounter() {
//        return targetCounter;
//    }
//
//    public void setTargetCounter(Long targetCounter) {
//        this.targetCounter = targetCounter;
//    }
}
