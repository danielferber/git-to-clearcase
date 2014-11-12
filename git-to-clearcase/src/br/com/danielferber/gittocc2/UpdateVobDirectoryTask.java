package br.com.danielferber.gittocc2;

import java.io.File;

import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigSource;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;

/**
 * Updates the ClearCase VOB view directory. Depending on the ClearTool
 * configuration, performs a full recursive update or just control stamp files.
 *
 * @author Daniel Felix Ferber
 */
public class UpdateVobDirectoryTask extends MeterCallable<Void> {

    private final ClearToolConfigSource cleartoolConfig;
    private final ClearToolCommander ctCommander;

    public UpdateVobDirectoryTask(final ClearToolConfigSource environmentConfig, final ClearToolCommander ctCommander, final Meter outerMeter) {
        super(outerMeter, "UpdateVobDirectory", "Update vob view directory.");
        this.cleartoolConfig = environmentConfig;
        this.ctCommander = ctCommander;
    }

    @Override
    protected Void meteredCall() throws Exception {
        if (cleartoolConfig.getVobViewDirUpdate()) {
            updateFullVob();
        } else {
            updateCommitStampFile();
            updateCounterStampFile();
        }
        return null;
    }

    private void updateCommitStampFile() {
        final File commitStampFile = cleartoolConfig.getCommitStampAbsoluteFile();
        final Meter m = getMeter().sub("commitFile").m("Update sync commit control file.").ctx("file", commitStampFile).start();
        ctCommander.updateFiles(commitStampFile);
        m.ok();
    }

    private void updateCounterStampFile() {
        final File counterStampFile = cleartoolConfig.getCounterStampAbsoluteFile();
        final Meter m = getMeter().sub("counterFile").m("Update sync counter control file.").ctx("file", counterStampFile).start();
        ctCommander.updateFiles(counterStampFile);
        m.ok();
    }

    private void updateFullVob() {
        final Meter m = getMeter().sub("vobDir").m("Update entire VOB directory.").ctx("dir", cleartoolConfig.getVobViewDir()).start();
        ctCommander.updateVobViewDir();
        m.ok();
    }
}
