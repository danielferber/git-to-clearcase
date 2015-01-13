/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.git;

import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;

/**
 *
 * @author Daniel Felix Ferber
 */
public class GitFinishTask implements Runnable {

    private final GitFinishConfig config;
    private final GitCommander gitCommander;

    public GitFinishTask(GitFinishConfig config, GitCommander gitCommander) {
        this.config = config;
        this.gitCommander = gitCommander;
    }

    @Override
    public void run() {
        MeterFactory.getMeter("GitFinish").run(() -> {
        });
    }
}
