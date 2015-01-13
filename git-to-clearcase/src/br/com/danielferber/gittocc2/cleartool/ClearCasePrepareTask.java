package br.com.danielferber.gittocc2.cleartool;

import br.com.danielferber.gittocc2.cleartool.ClearToolCommander;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;

/**
 * Updates the ClearCase VOB view directory. Depending on the ClearTool
 * configuration, performs a full recursive update or just control stamp files.
 *
 * @author Daniel Felix Ferber
 */
public class ClearCasePrepareTask implements Runnable {

    private final ClearCasePrepareConfig config;
    private final ClearToolCommander ctCommander;

    public ClearCasePrepareTask(final ClearCasePrepareConfig config, final ClearToolCommander ctCommander) {
        this.config = config;
        this.ctCommander = ctCommander;
    }

    @Override
    public void run() {
        final Meter m = MeterFactory.getMeter("ClearCasePrepare");
        m.run(() -> {
            if (config.getUpdateVobViewDir()) {
                m.sub("updateFullVob").run(() -> {
                    ctCommander.updateVobViewDir();
                });
            }
        });
    }

//    private void updateCommitStampFile() {
//        final File commitStampFile = cleartoolConfig.getCommitStampAbsoluteFile();
//        final Meter m = getMeter().sub("commitFile").m("Update sync commit control file.").ctx("file", commitStampFile).start();
//        ctCommander.updateFiles(commitStampFile);
//        m.ok();
//    }
//
//    private void updateCounterStampFile() {
//        final File counterStampFile = cleartoolConfig.getCounterStampAbsoluteFile();
//        final Meter m = getMeter().sub("counterFile").m("Update sync counter control file.").ctx("file", counterStampFile).start();
//        ctCommander.updateFiles(counterStampFile);
//        m.ok();
//    }
}
