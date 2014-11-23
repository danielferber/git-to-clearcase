/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigSource;
import br.com.danielferber.gittocc2.config.git.GitConfigSource;
import br.com.danielferber.gittocc2.task.UpdateGitRepositoryTask;
import br.com.danielferber.gittocc2.task.UpdateVobDirectoryTask;
import br.com.danielferber.gittocc2.task.config.SyncStrategyConfiguration;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 *
 * @author Daniel Felix Ferber
 */
abstract class SyncStrategyTask extends MeterCallable<Void> {

    protected final GitConfigSource gitConfig;
    protected final ClearToolConfigSource cleartoolConfig;
    protected final GitCommander gitCommander;
    protected final ClearToolCommander ctCommander;
    protected final File commitStampFile;
    protected final File counterStampFile;

    public SyncStrategyTask(String name, String message, SyncStrategyConfiguration configuration) {
        super(name, message);
        this.cleartoolConfig = configuration.getClearToolConfig();
        this.gitConfig = configuration.getGitConfig();
        this.gitCommander = new GitCommander(gitConfig);
        this.ctCommander = new ClearToolCommander(cleartoolConfig);
        this.commitStampFile = new File(cleartoolConfig.getVobViewDir(), cleartoolConfig.getCommitStampFile().getPath());
        this.counterStampFile = new File(cleartoolConfig.getVobViewDir(), cleartoolConfig.getCounterStampFile().getPath());
    }

    protected String readCommitStampFile() throws SyncTaskException {
        final Meter m = getMeter().sub("commitStamp.read").m("Read sync commit control file.").ctx("file", this.commitStampFile).start();
        try (Scanner scanner = new Scanner(this.commitStampFile)) {
            final String result = scanner.next();
            m.ctx("fromCommit", result).ok();
            return result;
        } catch (final FileNotFoundException ex) {
            m.fail(ex);
            throw new SyncTaskException("Commit stamp file not readable.", ex);
        }
    }

    protected long readSyncCounterFile() throws SyncTaskException {
        final Meter m = getMeter().sub("counterStamp.read").m("Read sync counter control file.").ctx("file", this.commitStampFile).start();
        try (Scanner scanner = new Scanner(this.counterStampFile)) {
            final long result = scanner.nextLong();
            m.ctx("counter", result).ok();
            return result;
        } catch (final FileNotFoundException ex) {
            m.fail(ex);
            throw new SyncTaskException("Counter stamp file not readable.", ex);
        }
    }

    protected String readCurrentCommit() {
        final Meter m = getMeter().sub("currentCommit.read").m("Read current commit hash.").start();
        final String gitCommit = gitCommander.currentCommit();
        m.ctx("commit", gitCommit).ok();
        return gitCommit;
    }

    @Override
    protected Void meteredCall() throws Exception {
        new UpdateVobDirectoryTask(cleartoolConfig, ctCommander).call();

        new UpdateGitRepositoryTask(gitConfig, gitCommander).call();

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

        final TreeDiff diff = diffTaskResult();

        if (!diff.hasStuff()) {
            getMeter().getLogger().info("Nothing to synchronize.");
        } else {
            PrintStream ps = LoggerFactory.getInfoPrintStream(getLogger());
            ps.format("Directories: added: #%d; removed: #%d\n", diff.dirsAdded.size(), diff.dirsDeleted.size());
            ps.format("Files: added: #%d; removed: #%d; modified: #%d\n", diff.filesAdded.size(), diff.filesDeleted.size(), diff.filesModified.size());
            ps.format("       copied: #%d; moved: #%d", diff.filesCopiedFrom.size(), diff.filesMovedFrom.size());
            ps.close();

            new ApplyDiffTask(cleartoolConfig, gitConfig, ctCommander, diff, syncToCommit, syncCounter).call();
        }

        new CheckVobFinalSanityTask(cleartoolConfig, ctCommander).call();

        return null;

    }

    protected abstract TreeDiff diffTaskResult() throws Exception;
}