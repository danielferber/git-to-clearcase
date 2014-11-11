package br.com.danielferber.gittocc2;

import java.util.concurrent.Callable;

import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;

/**
 *
 * @author daniel
 */
class GitTreeDiffTask implements Callable<TreeDiff> {

    private final GitCommander gitCommander;
    private final String syncFromCommit;
    private final String syncToCommit;
    private final Meter meter;

    GitTreeDiffTask(final GitCommander gitCommander, final String suncFromCommit, final String syncToCommit) {
        this.gitCommander = gitCommander;
        this.syncFromCommit = suncFromCommit;
        this.syncToCommit = syncToCommit;
        this.meter = MeterFactory.getMeter("GitTreeDiffTask").m("Calculate differences from Git histrory.");
    }

    @Override
    public TreeDiff call() throws Exception {
        meter.start();

        Meter m2 = null;
        try {
            m2 = meter.sub("treeDif").m("Execute tree-diff.").ctx("fromCommit", syncFromCommit).ctx("toCommit", syncToCommit).start();
            final TreeDiff diff = gitCommander.treeDif(syncFromCommit, syncToCommit);
            m2.ok();

            meter.ok();

            return diff;
        } catch (final Exception e) {
            meter.fail(e);
            throw e;
        }
    }

}
