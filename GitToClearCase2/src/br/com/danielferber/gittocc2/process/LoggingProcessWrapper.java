/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.process;

import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.Marker;

public class LoggingProcessWrapper<ProcessType extends LoggingProcessWrapper> extends CommandLineProcess<ProcessType> {

    protected final Meter meter;
    protected final Logger logger;
    protected final Marker stdoutMarker;
    protected final Marker stderrMarker;
    protected final Marker commandMarker;

    public LoggingProcessWrapper(String name, List<String> commandLine, File directory, Meter meter, Logger logger, Marker stdoutMarker, Marker stderrMarker, Marker commandMarker) {
        super(name, commandLine, directory);
        this.meter = meter;
        this.logger = logger;
        this.stdoutMarker = stdoutMarker;
        this.stderrMarker = stderrMarker;
        this.commandMarker = commandMarker;
        registerListeners();
    }

    @Override
    protected Process createProcess() throws IOException {
        logger.debug(formatCommandLine(commandLine));
        meter.start();
        try {
            return super.createProcess(); //To change body of generated methods, choose Tools | Templates.
        } catch (IOException e) {
            meter.fail(e);
            throw e;
        }
    }
    
    /**
     * Provides a pretty representation of the command that launches the
     * process. Override to customize.
     *
     * @return the pretty representation of the command that launches the
     * process.
     */
    protected String formatCommandLine(List<String> commands) {
        String executable = new File(commands.get(0)).getName();
        StringBuilder sb = new StringBuilder(executable);
        final Iterator<String> iterator = commands.iterator();
        iterator.next();
        for (Iterator<String> it = iterator; it.hasNext();) {
            sb.append(' ');
            sb.append(it.next());
        }
        return sb.toString();
    }

    private void registerListeners() {
        outRepeater.with(new LineSplittingWriter() {
            @Override
            protected void processLine(String line) {
                logger.trace(stdoutMarker, line);
            }
        });
        errRepeater.with(new LineSplittingWriter() {
            @Override
            protected void processLine(String line) {
                logger.trace(stderrMarker, line);
            }
        });
        processWaiter.with(new Runnable() {
            public void run() {
                meter.ok("exitValue", getProcess().exitValue());
            }
        });
    }
}
