/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.io;

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
     * Command line that started the wrapped process.
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
     * The wrapped process.
     */
    private Process process;

    protected CommandLineProcess(String name, List<String> commandLine, File directory) {
        this.name = name;
        this.directory = directory;
        this.commandLine = Collections.unmodifiableList(commandLine);
    }

    public void start() throws IOException {
        if (this.process != null) {
            return;
        }
        this.process = createProcess();
        this.processWaiter.start(process);
        this.outRepeater.start(process.getInputStream());
        this.errRepeater.start(process.getErrorStream());
    }

    protected Process getProcess() {
        return process;
    }

    public Scanner createOutScanner() throws IOException {
        return new Scanner(outRepeater.split());
    }

    public Scanner createErrScanner() throws IOException {
        return new Scanner(errRepeater.split());
    }

    public Reader createOutReader() throws IOException {
        return outRepeater.split();
    }

    public Reader createErrReader() throws IOException {
        return errRepeater.split();
    }

    public ProcessType addOutWriter(Writer w) {
        this.outRepeater.with(w);
        return (ProcessType) this;
    }

    public ProcessType addErrWriter(Writer w) {
        this.errRepeater.with(w);
        return (ProcessType) this;
    }

    public int exitValue() {
        return process.exitValue();
    }

    public void waitFor() throws IOException {
        start();

        try {
            process.waitFor();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    protected Process createProcess() throws IOException {
        return new ProcessBuilder(commandLine).directory(directory).start();
    }
}
