/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.git;

import br.com.danielferber.gittocc.process.LineSplittingWriter;
import br.com.danielferber.gittocc.process.ProcessOutputRepeater;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
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
public class GitProcess {
    
    final String name;
    final String commandLine;
    final Process process;
    final ProcessOutputRepeater outRepeater;
    final ProcessOutputRepeater errRepeater;
    final Logger logger;
    boolean started = false;
    
    public GitProcess(String name, List<String> commands, Process process) {
        this.name = name;
        this.commandLine = formatCommandLine(commands);
        
        this.logger = LoggerFactory.getLogger(GitProcess.class, name);
        this.logger.info("{}: {}", name, commandLine);
        
        this.outRepeater = new ProcessOutputRepeater(process.getInputStream());
        this.outRepeater.with(new LineSplittingWriter() {
            @Override
            protected void processLine(String line) {
                logger.debug(line);
            }
        });
        
        this.errRepeater = new ProcessOutputRepeater(process.getErrorStream());
        this.errRepeater.with(new LineSplittingWriter() {
            @Override
            protected void processLine(String line) {
                logger.debug(line);
            }
        });
        
        this.process = process;
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
    
    public GitProcess addOutWriter(Writer w) {
        this.outRepeater.with(w);
        return this;
    }
    
    public GitProcess addErrWriter(Writer w) {
        this.errRepeater.with(w);
        return this;
    }
    
    private static String formatCommandLine(List<String> commands) {
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
        if (process.exitValue() != 0) {
            throw new IOException("Processo falhou: " + name);
        }
    }
}
