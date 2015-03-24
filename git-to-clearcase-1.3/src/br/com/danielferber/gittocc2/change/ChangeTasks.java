/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.change;

import br.com.danielferber.gittocc2.cc.ClearToolCommander;
import br.com.danielferber.gittocc2.cc.ClearToolConfig;
import br.com.danielferber.gittocc2.config.ConfigException;

/**
 *
 * @author Daniel Felix Ferber
 */
public class ChangeTasks {
    private final ChangeContext context;

    private final ChangeConfig config;
    private final ClearToolConfig ctConfig;
    private ClearToolCommander ctCommander;

    public ChangeTasks(ChangeContext context, ChangeConfig config, ClearToolConfig ctConfig) {
        this.context = context;
        this.config = config;
        this.ctConfig = ctConfig;
    }

    private ClearToolCommander extractClearToolCommander() throws ConfigException {
        if (ctCommander == null) {
            ctCommander = new ClearToolCommander(ctConfig);
        }
        return ctCommander;
    }

    public class CheckoutStampTask implements Runnable {

        final ClearToolCommander commander = extractClearToolCommander();

        public CheckoutStampTask() {
            // Fails for inconsistent config.
            config.getCommitStampAbsoluteFile();
            config.getCounterStampAbsoluteFile();
        }

        @Override
        public void run() {
            commander.checkoutFile(config.getCommitStampAbsoluteFile());
            commander.checkoutFile(config.getCounterStampAbsoluteFile());
        }
    }

    public class UpdateStampTask implements Runnable {

        final ClearToolCommander commander = extractClearToolCommander();

        public UpdateStampTask() {
            // Fails for inconsistent config.
            config.getCommitStampAbsoluteFile();
            config.getCounterStampAbsoluteFile();
        }

        @Override
        public void run() {
            commander.update(config.getCommitStampAbsoluteFile());
            commander.update(config.getCounterStampAbsoluteFile());
        }
    }

    public class WriteStampTask implements Runnable {

        final ClearToolCommander commander = extractClearToolCommander();

        public WriteStampTask() {
            // Fails for inconsistent config.
            config.getCommitStampAbsoluteFile();
            config.getCounterStampAbsoluteFile();
        }

        @Override
        public void run() {
            commander.checkoutFile(config.getCommitStampAbsoluteFile());
            commander.checkoutFile(config.getCounterStampAbsoluteFile());
            config.writeCommitStampFromFile(context.getTargetCommit());
            long counter = config.readCounterStampFromFile();
            counter ++;
            config.writeCounterStampFromFile(counter);
        }
    }
}
