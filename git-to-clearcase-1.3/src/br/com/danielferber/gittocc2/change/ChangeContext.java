/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.change;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author x7ws
 */
public class ChangeContext {
    private List<ChangeSet> changeSetCollection = new ArrayList<>();
    
    public void addChangeSet(ChangeSet changeSet) {
        this.changeSetCollection.add(changeSet);
    }
}
