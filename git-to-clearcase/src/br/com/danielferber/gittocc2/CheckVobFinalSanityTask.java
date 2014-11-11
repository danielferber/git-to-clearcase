package br.com.danielferber.gittocc2;

import java.io.File;
import java.util.concurrent.Callable;

import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigSource;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import java.io.PrintStream;
import java.util.Collection;
import org.slf4j.Logger;

/**
 * Checks VOB sanity after synchronization.
 *
 * @author Daniel Felix Ferber
 */
public class CheckVobFinalSanityTask implements Callable<Void> {

    private final ClearToolConfigSource cleartoolConfig;
    private final ClearToolCommander ctCommander;
    private final Meter taskMeter;

    public CheckVobFinalSanityTask(final ClearToolConfigSource environmentConfig, final ClearToolCommander ctCommander, final Meter outerMeter) {
        this.cleartoolConfig = environmentConfig;
        this.ctCommander = ctCommander;
        this.taskMeter = outerMeter.sub("CheckVobFinalSanity").m("Check VOB final sanity.");
    }

    @Override
    public Void call() throws Exception {
        taskMeter.start();
        try {
            if (cleartoolConfig.getCheckForgottenCheckout()) {
                findForgottenCheckout();
            }
            taskMeter.ok();
        } catch (final Exception e) {
            taskMeter.fail(e);
            throw e;
        }
        return null;
    }

    private void findForgottenCheckout() {
        final Meter m = taskMeter.sub("findForgottenCheckout").m("Find forgotten checkouts.").start();
        Collection<ClearToolCommander.LsCheckoutItem> list = ctCommander.listCheckouts(cleartoolConfig.getVobViewDir());
        if (! list.isEmpty()) {
            Logger logger = m.getLogger();
            logger.warn("There are {} checkout out files!", list.size());
            PrintStream ps = LoggerFactory.getInfoPrintStream(logger);
            ps.println("Checked out out files:");
            int counter = 0;
            for (ClearToolCommander.LsCheckoutItem item : list) {
                if (counter >= 10) {
                    return;
                }
                counter++;
                ps.println(" - "+item.file.getPath()+" ("+item.user+")");
            }
            if (counter >= 10) {
                ps.println("   and more...");
            }
            ps.close();
        }
        m.ok();
    }
}
