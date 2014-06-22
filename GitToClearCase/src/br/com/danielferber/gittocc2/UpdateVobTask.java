/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigSource;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import java.io.File;
import java.util.concurrent.Callable;

/**
 *
 * @author daniel
 */
public class UpdateVobTask implements Callable<Void> {

    private final ClearToolConfigSource cleartoolConfig;
    private final ClearToolCommander ctCommander;
    private final Meter globalMeter;

    public UpdateVobTask(ClearToolConfigSource environmentConfig, ClearToolCommander ctCommander, Meter outerMeter) {
        this.cleartoolConfig = environmentConfig;
        this.ctCommander = ctCommander;
        this.globalMeter = outerMeter.sub("UpdateVob");
    }

    @Override
    public Void call() throws Exception {
        globalMeter.start();
        try {
            if (cleartoolConfig.getUpdateVobRoot()) {
                updateFullVob();
            } else {
                updateCommitStampFile();
                updateCounterStampFile();
            }
            globalMeter.ok();
        } catch (Exception e) {
            globalMeter.fail(e);
            throw e;
        }
        return null;
    }

    private void updateCommitStampFile() throws SyncTaskException {
        File commitStampFile = cleartoolConfig.getCommitStampFile();
        Meter m = globalMeter.sub("commitFile").m("Update sync commit control file.").ctx("file", commitStampFile).start();
        ctCommander.updateFile(commitStampFile);
        m.ok();
    }

    private void updateCounterStampFile() throws SyncTaskException {
        File counterStampFile = cleartoolConfig.getCounterStampFile();
        Meter m = globalMeter.sub("counterFile").m("Update sync counter control file.").ctx("file", counterStampFile).start();
        ctCommander.updateFile(counterStampFile);
        m.ok();
    }

    private void updateFullVob() {
        Meter m = globalMeter.sub("vobDir").m("Update entire VOB directory.").ctx("dir", cleartoolConfig.getVobViewDir()).start();
        ctCommander.updateVobViewDir();
        m.ok();
    }

}
