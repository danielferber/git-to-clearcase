/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.git.GitConfigSource;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import java.util.concurrent.Callable;

/**
 *
 * @author daniel
 */
public class GitTreeDiffTask implements Callable<GitTreeDiff> {

    private final GitConfigSource gitConfig;
    private final GitCommander gitCommander;
    private final String fromCommit;
    private final Meter globalMeter;

    public GitTreeDiffTask(GitConfigSource gitConfig, GitCommander gitCommander, String fromCommit, Meter outerMeter) {
        this.gitConfig = gitConfig;
        this.gitCommander = gitCommander;
        this.fromCommit = fromCommit;
        this.globalMeter = outerMeter.sub("GitDiff").m("Collect changes from GIT repository.");
    }

    @Override
    public GitTreeDiff call() throws Exception {
        globalMeter.start();

        Meter m2 = null;
        try {

            m2 = globalMeter.sub("currentCommit").m("Read current commit hash.").start();
            String gitCommit = gitCommander.currentCommit();
            m2.ctx("commit", gitCommit).ok();

//            m2 = globalMeter.sub("report").m("Gerar relatório de commits.").ctx("fromCommit", fromCommit).ctx("toCommit", gitCommit).start();
//            String report = gitCommander.commitMessagesReport(fromCommit, gitCommit, "%an (%ad):%n%s%n");
//            m2.ok();

            m2 = globalMeter.sub("treeDif").m("Execute tree-diff.").ctx("fromCommit", fromCommit).ctx("toCommit", gitCommit).start();
            GitTreeDiff diff = gitCommander.treeDif(fromCommit, gitCommit);
            m2.ok();

            globalMeter.ok();

            return diff;
        } catch (Exception e) {
            globalMeter.fail(e);
            throw e;
        }
    }

}
