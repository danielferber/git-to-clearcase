/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.cc;

/**
 *
 * @author Daniel Felix Ferber
 */
public class CCTasks {

    private final ClearToolConfig config;

    public CCTasks(ClearToolConfig config) {
        this.config = config;
    }

    public class FindCheckouts implements Runnable {
        final ClearToolCommander commander = new ClearToolCommander(config);

        @Override
        public void run() {
            commander.loadCheckouts(config.getVobViewAbsoluteDir());
        }
    }

    public class UpdateVob implements Runnable {
        final ClearToolCommander commander = new ClearToolCommander(config);

        @Override
        public void run() {
            commander.update(config.getVobViewAbsoluteDir());
        }
    }

    public class CheckinAll implements Runnable {
        final ClearToolCommander commander = new ClearToolCommander(config);

        @Override
        public void run() {
            if (commander.checkinDirsRequired() || commander.checkinFilesRequired()) {
                commander.checkinAll();
            }
        }
    }

}
