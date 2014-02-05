/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.process;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * A skeleton of a processes running a specific command line executable. The
 * extending class shall offer friendly API for the command line executable
 * domain to retrieve the process output.
 * <br> The process is not started until create is called. Meanwhile, the
 * process name, command line and working directory are kept as attributes.
 * <br> Domain friendly handling of process output should be provided through
 * the #outRepeater, #errRepeater and #processWaiter interfaces.
 *
 * @author Daniel Felix Ferber
 */
public class CommandLineProcess<ProcessType extends CommandLineProcess> {

    /**
     * Pretty name for the wrapped process.
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
    private Process process;

    /**
     * Constructor that receives all mandatory data required to create the
     * wrapped processs.
     *
     * @param name Pretty name for the process.
     * @param commandLine Command line that starts the wrapped process.
     * @param directory Working directory of the process.
     */
    protected CommandLineProcess(final String name, final List<String> commandLine, final File directory) {
        this.name = name;
        this.directory = directory;
        this.commandLine = Collections.unmodifiableList(commandLine);
    }

    /**
     * Actually starts (creates) the underlying command line executable process.
     *
     * @throws IOException If the JVM fails to create the process.
     */
    public final void start() throws IOException {
        if (this.process != null) {
            return;
        }
        this.process = createProcess();
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
     * @throws IOException If the stdout stream cannot be read.
     */
    public final Scanner createOutScanner() throws IOException {
        return new Scanner(outRepeater.split());
    }

    /**
     * Creates a Scanner that parses the process stderr stream. An arbitrary
     * number of Scanners may be created for this stream.
     *
     * @return a Scanner that parses the process stderr stream.
     * @throws IOException If the stderr stream cannot be read.
     */
    public final Scanner createErrScanner() throws IOException {
        return new Scanner(errRepeater.split());
    }

    /**
     * Creates a Reader for the process stdout stream. An arbitrary number of
     * Readers may be created for this stream.
     *
     * @return a Reader for the process stdout stream.
     * @throws IOException If the stdout stream cannot be read.
     */
    public final Reader createOutReader() throws IOException {
        return outRepeater.split();
    }

    /**
     * Creates a Reader for the process stderr stream. An arbitrary number of
     * Readers may be created for this stream.
     *
     * @return a Reader for the process stderr stream.
     * @throws IOException If the stderr stream cannot be read.
     */
    public final Reader createErrReader() throws IOException {
        return errRepeater.split();
    }

    /**
     * Forwards the stdout stream to the given Writer. This stream may be
     * forwarded to an arbitrary number of Writers.
     *
     * @param w the writer forwarded to
     * @throws IOException If the stdout stream cannot be read.
     */
    public final ProcessType addOutWriter(final Writer w) {
        this.outRepeater.with(w);
        return (ProcessType) this;
    }

    /**
     * Forwards the stderr stream to the given Writer. This stream may be
     * forwarded to an arbitrary number of Writers.
     *
     * @param w the writer forwarded to
     * @throws IOException If the stderr stream cannot be read.
     */
    public final ProcessType addErrWriter(final Writer w) {
        this.errRepeater.with(w);
        return (ProcessType) this;
    }

    /**
     * The process exit value, if finished.
     *
     * @return The process exit value, if finished.
     */
    protected final int exitValue() {
        return process.exitValue();
    }

    /**
     * Block until the command line executable finishes. Starts the command line
     * executable if not already running
     *
     * @throws IOException If the JVM fails to create the process.
     */
    public final void waitFor() throws IOException {
        start();

        try {
            process.waitFor();
        } catch (InterruptedException ex) {
            // ignore
        }
    }

    /**
     * Creates the underlying wrapped process. Override this method if a custom
     * process creating strategy is required.
     *
     * @return The underlying wrapped process running the command line
     * executable.
     * @throws IOException If the JVM fails to create the process.
     */
    protected Process createProcess() throws IOException {
        return new ProcessBuilder(commandLine).directory(directory).start();
    }
}
