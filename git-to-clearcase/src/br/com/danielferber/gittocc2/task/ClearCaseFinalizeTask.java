package br.com.danielferber.gittocc2.task;

import br.com.danielferber.gittocc2.cleartool.ClearToolCommander;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import java.io.PrintStream;
import java.util.Collection;
import org.slf4j.Logger;

/**
 * Checks VOB sanity after synchronization.
 *
 * @author Daniel Felix Ferber
 */
public class ClearCaseFinalizeTask extends MeterCallable<Void> {

    private final ClearCaseFinalizeConfig config;
    private final ClearToolCommander commander;
    private final ClearCaseVobConfig vobConfig;

    public ClearCaseFinalizeTask(ClearCaseFinalizeConfig config, ClearCaseVobConfig vobConfig, ClearToolCommander commander) {
        super("ClearCase finalization task");
        this.config = config;
        this.vobConfig = vobConfig;
        this.commander = commander;
    }

    @Override
    public Void meteredCall() throws Exception {
        if (config.getValidateExistingCheckout()) {
            findForgottenCheckout();
        }
        return null;
    }

    private static class void findForgottenCheckout() {
        new MeterCallable(this.getMeter(), "ValidateExistingCheckout", "Validate existing checkouts") {
            @Override
            protected Void meteredCall() throws Exception {
                Collection<ClearToolCommander.LsCheckoutItem> list = commander.listCheckouts(vobConfig.getVobViewAbsoluteDir());
                if (!list.isEmpty()) {
                    Logger logger = getLogger();
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
                return null;
            }
        }.call();
    }
}
