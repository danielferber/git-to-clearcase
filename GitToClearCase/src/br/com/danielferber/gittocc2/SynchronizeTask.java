/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigSource;
import br.com.danielferber.gittocc2.config.git.GitConfigSource;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.Callable;

/**
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

    SynchronizeTask(ClearToolConfigSource cleartoolConfig, GitConfigSource gitConfig, boolean compareOnly, File compareRoot) {
        this.gitCommander = new GitCommander(gitConfig);
        this.ctCommander = new ClearToolCommander(cleartoolConfig);
        this.cleartoolConfig = cleartoolConfig;
        this.gitConfig = gitConfig;
        this.compareOnly = compareOnly;
        this.compareRoot = compareRoot;
        this.commitStampFile = new File(cleartoolConfig.getVobViewDir(), cleartoolConfig.getCommitStampFile().getPath());
        this.counterStampFile = new File(cleartoolConfig.getVobViewDir(), cleartoolConfig.getCounterStampFile().getPath());
        this.globalMeter = MeterFactory.getMeter("SyncTask").m("Sincronizar from Git to ClearCase.");
    }

    @Override
    public Void call() throws Exception {
        globalMeter.start();
        Meter m2;
        try {
            /* ClearCase Tasks */
            new UpdateVobTask(cleartoolConfig, ctCommander, globalMeter).call();

            /* Git Tasks */
            new UpdateGitRepositoryTask(gitConfig, gitCommander, globalMeter).call();

            /* Collect task data */
            long syncCounter;
            if (cleartoolConfig.getOverriddenSyncCounter() == null) {
                syncCounter = readSyncCounter() + 1;
            } else {
                syncCounter = cleartoolConfig.getOverriddenSyncCounter();
            }
            String syncFromCommit;
            if (cleartoolConfig.getOverriddenSyncCounter() == null) {
                syncFromCommit = readCommitStampFile();
            } else {
                syncFromCommit = cleartoolConfig.getOverriddenSyncFromCommit();
            }
            String syncToCommit = readCurrentCommit();

            /* TreeDiff Task */
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
        } catch (Exception e) {
            globalMeter.fail(e);
            throw e;
        }

    }

    private String readCommitStampFile() throws SyncTaskException {
        Meter m = globalMeter.sub("read.commitFile").m("Read sync commit control file.").ctx("file", this.commitStampFile).start();
        try (Scanner scanner = new Scanner(this.commitStampFile)) {
            final String result = scanner.next();
            m.ctx("fromCommit", result).ok();
            return result;
        } catch (FileNotFoundException ex) {
            m.fail(ex);
            throw new SyncTaskException("Commit stamp file not readable.", ex);
        }
    }

    private long readSyncCounter() throws SyncTaskException {
        Meter m = globalMeter.sub("read.counterFile").m("Read sync counter control file.").ctx("file", this.commitStampFile).start();
        try (Scanner scanner = new Scanner(this.counterStampFile)) {
            final long result = scanner.nextLong();
            m.ctx("counter", result).ok();
            return result;
        } catch (FileNotFoundException ex) {
            m.fail(ex);
            throw new SyncTaskException("Counter stamp file not readable.", ex);
        }
    }

    private String readCurrentCommit() {
        Meter m = globalMeter.sub("currentCommit").m("Read current commit hash.").start();
        String gitCommit = gitCommander.currentCommit();
        m.ctx("commit", gitCommit).ok();
        return gitCommit;
    }
}
