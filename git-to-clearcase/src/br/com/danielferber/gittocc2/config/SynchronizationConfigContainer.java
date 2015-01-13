/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.config;

import java.io.PrintStream;
import java.util.Properties;

/**
 *
 * @author Daniel Felix Ferber
 */
public class SynchronizationConfigContainer extends ConfigContainer {
    public SynchronizationConfigContainer(Properties properties) {
        super(properties);
    }

    @Override
    public void validateAll() throws ConfigException {
        super.validateAll();
    }

    @Override
    public void printConfig(PrintStream ps) {
        ps.println("Synchronization strategy:");
        super.printConfig(ps);
    }
}
