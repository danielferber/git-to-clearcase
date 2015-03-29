/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.cc;

import br.com.danielferber.gittocc2.config.ConfigException;

/**
 *
 * @author Daniel Felix Ferber
 */
public class CcTasks {

    private final CcConfig config;
    private CcCommander commander;

    private CcCommander extractClearToolCommander() throws ConfigException {
        if (commander == null) {
            commander = new CcCommander(config);
        }
        return commander;
    }

    public CcTasks(CcConfig config) {
        this.config = config;
    }

    public class LoadCheckouts implements Runnable {

        final CcCommander commander = extractClearToolCommander();

        @Override
        public void run() {
            commander.loadCheckouts(config.getVobViewAbsoluteDir());
        }
    }

    public class UpdateVob implements Runnable {

        final CcCommander commander = extractClearToolCommander();

        @Override
        public void run() {
            commander.update(config.getVobViewAbsoluteDir());
        }
    }

    public class CheckinAll implements Runnable {

        final CcCommander commander = extractClearToolCommander();

        @Override
        public void run() {
            if (commander.checkinDirsRequired() || commander.checkinFilesRequired()) {
                commander.checkinAll();
            }
        }
    }

    public class UnsetActivity implements Runnable {

        final CcCommander commander = extractClearToolCommander();

        @Override
        public void run() {
            commander.unsetActivity();
        }
    }
}
