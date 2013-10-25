/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.io;

import br.com.danielferber.gittocc.process.ProcessOutputRepeater;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import org.slf4j.Logger;

/**
 *
 * @author X7WS
 */
public class ProcessWrapper {

    protected final String name;
    protected final String commandLine;
    protected final Process process;
    protected final ProcessOutputRepeater outRepeater;
    protected final ProcessOutputRepeater errRepeater;
    
    boolean started = false;

    public ProcessWrapper(String name, String commandLine, Process process) {
        this.name = name;
        this.commandLine = commandLine;
        this.outRepeater = new ProcessOutputRepeater(process.getInputStream());
        this.errRepeater = new ProcessOutputRepeater(process.getErrorStream());
        this.process = process;
    }

    public String getName() {
        return name;
    }

    public String getCommandLine() {
        return commandLine;
    }
    
    public synchronized void start() {
        if (!started) {
            started = true;
            this.outRepeater.start();
            this.errRepeater.start();
        }
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

    public ProcessWrapper addOutWriter(Writer w) {
        this.outRepeater.with(w);
        return this;
    }

    public ProcessWrapper addErrWriter(Writer w) {
        this.errRepeater.with(w);
        return this;
    }

    public void waitFor() throws IOException {
        start();
        try {
            outRepeater.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            errRepeater.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            process.waitFor();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
