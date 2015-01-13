package br.com.danielferber.gittocc2.cleartool;

import br.com.danielferber.gittocc2.cleartool.ClearToolCommander;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;
import java.io.PrintStream;
import java.util.Collection;
import org.slf4j.Logger;

/**
 * Checks VOB sanity after synchronization.
 *
 * @author Daniel Felix Ferber
 */
public class ClearCaseFinalizeTask implements Runnable {

    private final ClearCaseFinalizeConfig config;
    private final ClearToolCommander commander;
    public final static Logger logger = LoggerFactory.getLogger("ClearCaseFinalize");

    public ClearCaseFinalizeTask(ClearCaseFinalizeConfig config, ClearToolCommander commander) {
        this.config = config;
        this.commander = commander;
    }

    @Override
    public void run() {
        final Meter m = MeterFactory.getMeter("ClearCaseFinalize");
        m.run(() -> {
            if (config.getValidateExistingCheckout()) {
                m.sub("findForgottenCheckout").run(() -> {
                    findForgottenCheckout();
                });
            }
        });
    }

    void findForgottenCheckout() {
        Collection<ClearToolCommander.LsCheckoutItem> list = commander.listCheckouts();
        if (!list.isEmpty()) {
            logger.warn("There are {} checkouts!", list.size());
            PrintStream ps = LoggerFactory.getInfoPrintStream(logger);
            ps.println("Checked out out files:");
            int counter = 0;
            for (ClearToolCommander.LsCheckoutItem item : list) {
                if (counter >= 10) {
                    break;
                }
                counter++;
                ps.println(" - " + item.file.getPath() + " (" + item.user + ")");
            }
            if (counter >= 10) {
                ps.println("   and more...");
            }
            ps.close();
        }
    }
}
