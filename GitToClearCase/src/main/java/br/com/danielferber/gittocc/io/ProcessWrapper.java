/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.io;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author X7WS
 */
public class ProcessWrapper<ProcessType extends ProcessWrapper> {

    protected final String name;
    protected final File directory;
    protected final List<String> commandLine;
    protected final ProcessOutputRepeater outRepeater = new ProcessOutputRepeater();
    protected final ProcessOutputRepeater errRepeater = new ProcessOutputRepeater();
    protected final ProcessWaiter processWaiter = new ProcessWaiter();

    private Process process;

    protected ProcessWrapper(String name, List<String> commandLine, File directory) {
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
