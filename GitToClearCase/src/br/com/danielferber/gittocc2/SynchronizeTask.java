/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.Callable;

import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigSource;
import br.com.danielferber.gittocc2.config.git.GitConfigSource;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;

/**
 * Syncronizes the Git repository to the ClearCase VOB view directory.
 *
 * @author Daniel Felix Ferber
 */
class SynchronizeTask implements Callable<Void> {

    private final GitConfigSource gitConfig;
    private final ClearToolConfigSource cleartoolConfig;
    private final GitCommander gitCommander;
    private final ClearToolCommander ctCommander;
    private final File commitStampFile;
    private final File counterStampFile;
    private final Meter globalMeter;
    private final boolean compareOnly;
    private final File compareRoot;

    SynchronizeTask(final ClearToolConfigSource cleartoolConfig, final GitConfigSource gitConfig, final boolean compareOnly, final File compareRoot) {
        this.gitCommander = new GitCommander(gitConfig);
        this.ctCommander = new ClearToolCommander(cleartoolConfig);
        this.cleartoolConfig = cleartoolConfig;
        this.gitConfig = gitConfig;
        this.compareOnly = compareOnly;
        this.compareRoot = compareRoot;
        this.commitStampFile = new File(cleartoolConfig.getVobViewDir(), cleartoolConfig.getCommitStampFileName().getPath());
        this.counterStampFile = new File(cleartoolConfig.getVobViewDir(), cleartoolConfig.getCounterStampFileName().getPath());
        this.globalMeter = MeterFactory.getMeter("SyncTask").m("Synchronize Git to ClearCase.");
    }

    @Override
    public Void call() throws Exception {
        if (compareOnly) {
            globalMeter.ctx("stategy", "DeepFileCompare").ctx("roots", compareRoot);
        } else {
            globalMeter.ctx("stategy", "GitCommitHistory");
        }
        globalMeter.start();

        try {
            new UpdateVobDirectoryTask(cleartoolConfig, ctCommander, globalMeter).call();

            new UpdateGitRepositoryTask(gitConfig, gitCommander, globalMeter).call();

            long syncCounter;
            if (cleartoolConfig.getOverriddenSyncCounter() == null) {
                syncCounter = readSyncCounterFile() + 1;
            } else {
                syncCounter = cleartoolConfig.getOverriddenSyncCounter();
            }

            String syncFromCommit;
            if (cleartoolConfig.getOverriddenSyncCounter() == null) {
                syncFromCommit = readCommitStampFile();
            } else {
                syncFromCommit = cleartoolConfig.getOverriddenSyncFromCommit();
            }
            final String syncToCommit = readCurrentCommit();

            final TreeDiff diff;
            if (compareOnly) {
                diff = (new CompareTreeDiffTask(gitConfig.getRepositoryDir(), cleartoolConfig.getVobViewDir(), compareRoot, globalMeter)).call();
            } else {
                diff = (new GitTreeDiffTask(gitCommander, syncFromCommit, syncToCommit, globalMeter)).call();
            }

            if (!diff.hasStuff()) {
                return null;
            }

            new ClearCaseChangeTask(cleartoolConfig, gitConfig, ctCommander, diff, syncToCommit, syncCounter, globalMeter).call();

            globalMeter.ok();

            return null;
        } catch (final Exception e) {
            globalMeter.fail(e);
            throw e;
        }
    }

    private String readCommitStampFile() throws SyncTaskException {
        final Meter m = globalMeter.sub("commitStamp.read").m("Read sync commit control file.").ctx("file", this.commitStampFile).start();
        try (Scanner scanner = new Scanner(this.commitStampFile)) {
            final String result = scanner.next();
            m.ctx("fromCommit", result).ok();
            return result;
        } catch (final FileNotFoundException ex) {
            m.fail(ex);
            throw new SyncTaskException("Commit stamp file not readable.", ex);
        }
    }

    private long readSyncCounterFile() throws SyncTaskException {
        final Meter m = globalMeter.sub("syncCounter.read").m("Read sync counter control file.").ctx("file", this.commitStampFile).start();
        try (Scanner scanner = new Scanner(this.counterStampFile)) {
            final long result = scanner.nextLong();
            m.ctx("counter", result).ok();
            return result;
        } catch (final FileNotFoundException ex) {
            m.fail(ex);
            throw new SyncTaskException("Counter stamp file not readable.", ex);
        }
    }

    private String readCurrentCommit() {
        final Meter m = globalMeter.sub("currentCommit.read").m("Read current commit hash.").start();
        final String gitCommit = gitCommander.currentCommit();
        m.ctx("commit", gitCommit).ok();
        return gitCommit;
    }
}
