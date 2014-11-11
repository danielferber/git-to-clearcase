/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.io.process;

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

import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;

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
    protected final String name;
    /**
     * Working directory of the wrapped process.
     */
    protected final File directory;
    /**
     * Command line that starts the wrapped process.
     */
    protected final List<String> commandLine;
    /**
     * Interface to read the wrapped process stdout stream.
     */
    protected final ProcessOutputRepeater outRepeater = new ProcessOutputRepeater();
    /**
     * Interface to read the wrapped process stderr stream.
     */
    protected final ProcessOutputRepeater errRepeater = new ProcessOutputRepeater();
    /**
     * Interface to register tasks to be executed when process finishes.
     */
    protected final ProcessWaiter processWaiter = new ProcessWaiter();
    /**
     * The wrapped process created upon the object immutable state. If null, the
     * process has not been created (actually, started) yet.
     */
    protected Process process;
    protected final Meter meter;
    protected static final Marker stdoutMarker = MarkerFactory.getMarker("stdout");
    protected static final Marker stderrMarker = MarkerFactory.getMarker("stderr");

    public static class CommandLineProcessCreationException extends RuntimeException {
    	private static final long serialVersionUID = 1L;

        public CommandLineProcessCreationException(final String message) {
            super(message);
        }

        public CommandLineProcessCreationException(final String message, final Throwable cause) {
            super(message, cause);
        }

        public CommandLineProcessCreationException(final Throwable cause) {
            super(cause);
        }
    }
    
      public static class CommandLineProcessExecutionException extends RuntimeException {
    	private static final long serialVersionUID = 1L;

        public CommandLineProcessExecutionException(final String message) {
            super(message);
        }

        public CommandLineProcessExecutionException(final String message, final Throwable cause) {
            super(message, cause);
        }

        public CommandLineProcessExecutionException(final Throwable cause) {
            super(cause);
        }
    }

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
        this.outRepeater.with(new LineSplittingWriter() {
            @Override
            protected void processLine(final String line) {
                if (! line.equals("\r")) {
                    meter.getLogger().trace(stdoutMarker, line);
                }
            }
        });
        this.errRepeater.with(new LineSplittingWriter() {
            @Override
            protected void processLine(final String line) {
                if (! line.equals("\r")) {
                    meter.getLogger().trace(stderrMarker, line);
                }
            }
        });
        this.processWaiter.with(new Runnable() {
            @Override
            public void run() {
                final Exception stderrException = CommandLineProcess.this.errRepeater.getException();
                if (stderrException != null) {
                    meter.ctx("stderrException", stderrException);
                }
                final Exception stdoutException = CommandLineProcess.this.outRepeater.getException();
                if (stdoutException != null) {
                    meter.ctx("stdoutException", stdoutException);
                }
                meter.ctx("exitValue", getProcess().exitValue()).ok();
            }
        });
    }

    /**
     * Actually starts (creates) the underlying command line executable process.
     *
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
     * The underlying wrapped process or null if the process has not been
     * started/created yet.
     *
     * @return
     */
    protected final Process getProcess() {
        return process;
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
     *
     * @throws IOException If the JVM fails to create the process.
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
     *
     * @return the pretty representation of the command that launches the
     * process.
     */
    protected String formatCommandLine(final List<String> commands) {
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
