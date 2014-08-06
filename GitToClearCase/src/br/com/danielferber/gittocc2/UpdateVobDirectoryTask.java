package br.com.danielferber.gittocc2;

import java.io.File;
import java.util.concurrent.Callable;

import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigSource;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;

/**
 * Updates the ClearCase VOB view directory. Depending on the ClearTool
 * configuration, performs a full recursive update or just control stamp files.
 *
 * @author Daniel Felix Ferber
 */
public class UpdateVobDirectoryTask implements Callable<Void> {

    private final ClearToolConfigSource cleartoolConfig;
    private final ClearToolCommander ctCommander;
    private final Meter taskMeter;

    public UpdateVobDirectoryTask(final ClearToolConfigSource environmentConfig, final ClearToolCommander ctCommander, final Meter outerMeter) {
        this.cleartoolConfig = environmentConfig;
        this.ctCommander = ctCommander;
        this.taskMeter = outerMeter.sub("UpdateVob").m("Update vob view directory.");
    }

    @Override
    public Void call() throws Exception {
        taskMeter.start();
        try {
            if (cleartoolConfig.getVobViewDirUpdate()) {
                updateFullVob();
            } else {
                updateCommitStampFile();
                updateCounterStampFile();
            }
            taskMeter.ok();
        } catch (final Exception e) {
            taskMeter.fail(e);
            throw e;
        }
        return null;
    }

    private void updateCommitStampFile() {
        final File commitStampFile = cleartoolConfig.getCommitStampAbsoluteFile();
        final Meter m = taskMeter.sub("commitFile").m("Update sync commit control file.").ctx("file", commitStampFile).start();
        ctCommander.updateFiles(commitStampFile);
        m.ok();
    }

    private void updateCounterStampFile() {
        final File counterStampFile = cleartoolConfig.getCounterStampAbsoluteFile();
        final Meter m = taskMeter.sub("counterFile").m("Update sync counter control file.").ctx("file", counterStampFile).start();
        ctCommander.updateFiles(counterStampFile);
        m.ok();
    }

    private void updateFullVob() {
        final Meter m = taskMeter.sub("vobDir").m("Update entire VOB directory.").ctx("dir", cleartoolConfig.getVobViewDir()).start();
        ctCommander.updateVobViewDir();
        m.ok();
    }
}
