/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.process;

import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * A processes running a command line executable.
 * <br> The process is not started until {@link #start()} is called. Meanwhile,
 * the process name, command line and working directory are kept as attributes.
 * <br> Process stdout and stderr may be read by {@link #createErrReader()} and
 * {@link #createOutReader()}.
 * <br> Process stdout and stderr may be parsed by {@link #createErrScanner() }
 * and {@link #createOutScanner() }.
 * <br> Process stdout and stderr may be redirected by {@link #addErrWriter(java.io.Writer)
 * } and {@link #addOutWriter(java.io.Writer) }.
 *
 * @author Daniel Felix Ferber
 */
public class CommandLineProcess {

    /**
     * Name for the wrapped process.
     */
    private final String name;
    /**
     * Working directory of the wrapped process.
     */
    private final File directory;
    /**
     * Command line that starts the wrapped process.
     */
    private final List<String> commandLine;
    /**
     * Interface to read the wrapped process stdout stream.
     */
    private final ProcessOutputRepeater outRepeater = new ProcessOutputRepeater();
    /**
     * Interface to read the wrapped process stderr stream.
     */
    private final ProcessOutputRepeater errRepeater = new ProcessOutputRepeater();
    /**
     * Interface to register tasks to be executed when process finishes.
     */
    private final ProcessWaiter processWaiter = new ProcessWaiter();
    /**
     * The wrapped process created upon the object immutable state. If null, the
     * process has not been created (actually, started) yet.
     */
    private Process process;
    private final Meter meter;
    public static final Marker stdoutMarker = MarkerFactory.getMarker("stdout");
    public static final Marker stderrMarker = MarkerFactory.getMarker("stderr");

    /**
     * Constructor that receives all mandatory data required to create the
     * wrapped processs.
     *
     * @param name Pretty name for the process.
     * @param commandLine Command line that starts the wrapped process.
     * @param directory Working directory of the process.
     * @param logger
     */
    protected CommandLineProcess(final String name, final List<String> commandLine, final File directory, final Logger logger) {
        this.name = name;
        this.directory = directory;
        this.commandLine = Collections.unmodifiableList(commandLine);
        this.meter = MeterFactory.getMeter(logger, name);
        if (meter.getLogger().isTraceEnabled()) {
            this.outRepeater.with(new LineSplittingWriter() {
                @Override
                protected void processLine(final String line) {
                    if (!line.equals("\r")) {
                        meter.getLogger().trace(stdoutMarker, line);
                    }
                }
            });
            this.errRepeater.with(new LineSplittingWriter() {
                @Override
                protected void processLine(final String line) {
                    if (!line.equals("\r")) {
                        meter.getLogger().trace(stderrMarker, line);
                    }
                }
            });
        }
        this.processWaiter.with(() -> {
            final Exception stderrException = errRepeater.getException();
            if (stderrException != null) {
                meter.ctx("stderrException", stderrException);
            }
            final Exception stdoutException = outRepeater.getException();
            if (stdoutException != null) {
                meter.ctx("stdoutException", stdoutException);
            }
            meter.ctx("exitValue", process.exitValue()).ok();
        });
    }

    /**
     * Actually starts (creates) the underlying command line executable process.
     */
    public final void start() {
        if (this.process != null) {
            return;
        }
        try {
            meter.ctx("cmd", formatCommandLine(commandLine)).start();
            this.process = new ProcessBuilder(commandLine).directory(directory).start();
        } catch (final IOException e) {
            meter.fail(e);
            throw new CommandLineProcessCreationException("Failed to create process", e);
        }
        this.processWaiter.start(process);
        this.outRepeater.start(process.getInputStream());
        this.errRepeater.start(process.getErrorStream());
    }

    /**
     * Creates a Scanner that parses the process stdout stream. An arbitrary
     * number of Scanners may be created for this stream.
     *
     * @return a Scanner that parses the process stdout stream.
     */
    public final Scanner createOutScanner() {
        try {
            return new Scanner(outRepeater.split());
        } catch (final IOException ex) {
            throw new CommandLineProcessCreationException("Failed to create stdout scanner", ex);
        }
    }

    /**
     * Creates a Scanner that parses the process stderr stream. An arbitrary
     * number of Scanners may be created for this stream.
     *
     * @return a Scanner that parses the process stderr stream.
     */
    public final Scanner createErrScanner() {
        try {
            return new Scanner(errRepeater.split());
        } catch (final IOException ex) {
            throw new CommandLineProcessCreationException("Failed to create stderr scanner", ex);
        }
    }

    /**
     * Creates a Reader for the process stdout stream. An arbitrary number of
     * Readers may be created for this stream.
     *
     * @return a Reader for the process stdout stream.
     */
    public final Reader createOutReader() {
        try {
            return outRepeater.split();
        } catch (final IOException ex) {
            throw new CommandLineProcessCreationException("Failed to create stdout repeater", ex);
        }
    }

    /**
     * Creates a Reader for the process stderr stream. An arbitrary number of
     * Readers may be created for this stream.
     *
     * @return a Reader for the process stderr stream.
     */
    public final Reader createErrReader() {
        try {
            return errRepeater.split();
        } catch (final IOException ex) {
            throw new CommandLineProcessCreationException("Failed to create stderr repeater", ex);
        }
    }

    /**
     * Forwards the stdout stream to the given Writer. This stream may be
     * forwarded to an arbitrary number of Writers.
     *
     * @param w the writer forwarded to
     * @return itself
     */
    public final CommandLineProcess addOutWriter(final Writer w) {
        this.outRepeater.with(w);
        return this;
    }

    /**
     * Forwards the stderr stream to the given Writer. This stream may be
     * forwarded to an arbitrary number of Writers.
     *
     * @param w the writer forwarded to
     * @return itself
     */
    public final CommandLineProcess addErrWriter(final Writer w) {
        this.errRepeater.with(w);
        return this;
    }

    /**
     * The process exit value, if finished.
     *
     * @return The process exit value, if finished.
     */
    public final int exitValue() {
        return process.exitValue();
    }

    public Exception getException() {
        final Exception errException = errRepeater.getException();
        if (errException != null) {
            return errException;
        }
        final Exception outException = outRepeater.getException();
        if (outException != null) {
            return outException;
        }
        return null;
    }

    /**
     * Block until the command line executable finishes. Starts the command line
     * executable if not already running
     */
    public final void waitFor() {
        start();

        try {
            process.waitFor();
        } catch (final InterruptedException ex) {
            // ignore
        }
    }

    /**
     * Provides a pretty representation of the command that launches the
     * process. Override to customize.
     */
    private String formatCommandLine(final List<String> commands) {
        final String executable = new File(commands.get(0)).getName();
        final StringBuilder sb = new StringBuilder(executable);
        final Iterator<String> iterator = commands.iterator();
        iterator.next();
        for (final Iterator<String> it = iterator; it.hasNext();) {
            sb.append(' ');
            sb.append(it.next());
        }
        return sb.toString();
    }
}
