package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.git.GitConfigSource;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import java.util.concurrent.Callable;

/**
 *
 * @author daniel
 */
class GitTreeDiffTask implements Callable<TreeDiff> {

    private final GitCommander gitCommander;
    private final String syncFromCommit;
    private final String syncToCommit;
    private final Meter globalMeter;

    GitTreeDiffTask(GitCommander gitCommander, String suncFromCommit, String syncToCommit, Meter outerMeter) {
        this.gitCommander = gitCommander;
        this.syncFromCommit = suncFromCommit;
        this.syncToCommit = syncToCommit;
        this.globalMeter = outerMeter.sub("GitDiff").m("Collect changes from GIT repository.");
    }

    @Override
    public TreeDiff call() throws Exception {
        globalMeter.start();

        Meter m2 = null;
        try {

//            m2 = globalMeter.sub("report").m("Gerar relatório de commits.").ctx("fromCommit", fromCommit).ctx("toCommit", gitCommit).start();
//            String report = gitCommander.commitMessagesReport(fromCommit, gitCommit, "%an (%ad):%n%s%n");
//            m2.ok();
            m2 = globalMeter.sub("treeDif").m("Execute tree-diff.").ctx("fromCommit", syncFromCommit).ctx("toCommit", syncToCommit).start();
            TreeDiff diff = gitCommander.treeDif(syncFromCommit, syncToCommit);
            m2.ok();

            globalMeter.ok();

            return diff;
        } catch (Exception e) {
            globalMeter.fail(e);
            throw e;
        }
    }

}
