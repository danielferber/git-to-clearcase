/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.io;

import br.com.danielferber.gittocc.process.LineSplittingWriter;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;
import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public abstract class LoggingProcessBuilder<ProcessBuilderType extends LoggingProcessBuilder, ProcessType extends ProcessWrapper> extends ProcessBuilderWrapper<ProcessBuilderType, ProcessType> {

    private Meter meter;

    public LoggingProcessBuilder(File executionDirectory, File executableFile) {
        super(executionDirectory, executableFile);
    }

    @Override
    protected Process createProcess() throws IOException {
        meter.start();
        final Process process = super.createProcess();
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        process.waitFor();
                        meter.ok("exitValue", process.exitValue());
                        break;
                    } catch (InterruptedException e) {
                        // ignorar
                    }
                }
            }
        }.start();
        return process;
    }

    protected Meter createMeter() {
        return MeterFactory.getMeter(LoggingProcessBuilder.class, name);
    }

    protected Logger createLogger() {
        return LoggerFactory.getLogger(LoggingProcessBuilder.class, name);
    }

    protected Marker createCommandMarker() {
        return MarkerFactory.getMarker("cmd");
    }

    protected Marker createStdOutMarker() {
        return MarkerFactory.getMarker("out");
    }

    protected Marker createStdErrMarker() {
        return MarkerFactory.getMarker("err");
    }

    @Override
    public ProcessType create() throws IOException {
        meter = createMeter();
        final Logger logger = createLogger();

        final ProcessType processWrapper = super.create();
        logger.debug(createCommandMarker(), "{}: {}", name, processWrapper.commandLine);

        processWrapper.outRepeater.with(new LineSplittingWriter() {
            final Marker stdoutMarker = createStdOutMarker();

            @Override
            protected void processLine(String line) {
                logger.trace(stdoutMarker, line);
            }
        });
        processWrapper.errRepeater.with(new LineSplittingWriter() {
            final Marker stderrMarker = createStdErrMarker();

            @Override
            protected void processLine(String line) {
                logger.trace(stderrMarker, line);
            }
        });
        return processWrapper;
    }
}
