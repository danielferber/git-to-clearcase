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
    private String targetCommit = null;
    private Long counter;

    public void addChangeSet(ChangeSet changeSet) {
        this.changeSetCollection.add(changeSet);
    }

    public ChangeSet nextChangeSet() {
        return this.changeSetCollection.poll();
    }

    public String getTargetCommit() {
        return targetCommit;
    }

    public void setTargetCommit(String targetCommit) {
        this.targetCommit = targetCommit;
    }

    public void setCounter(Long counter) {
        this.counter = counter;
    }

    public Long getCounter() {
        return counter;
    }
}
