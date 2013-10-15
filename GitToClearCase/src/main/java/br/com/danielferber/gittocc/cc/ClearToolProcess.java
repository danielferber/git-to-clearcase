/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.git;

import br.com.danielferber.gittocc.io.ProcessWrapper;
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
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 *
 * @author X7WS
 */
public class ClearToolProcess extends ProcessWrapper {

    final Logger logger;
    final static Marker STDOUT_MARKER = MarkerFactory.getMarker("ct_out");
    final static Marker STDERR_MARKER = MarkerFactory.getMarker("ct_err");
    final static Marker COMMAND_MARKER = MarkerFactory.getMarker("ct_cmd");

    public ClearToolProcess(String name, String commandLine, Process process) {
        super(name, commandLine, process);
        
        this.logger = LoggerFactory.getLogger(GitProcess.class, name);
        this.logger.info(COMMAND_MARKER, "{}: {}", name, commandLine);
        this.outRepeater.with(new LineSplittingWriter() {
            @Override
            protected void processLine(String line) {
                logger.debug(STDOUT_MARKER, line);
            }
        });
        this.errRepeater.with(new LineSplittingWriter() {
            @Override
            protected void processLine(String line) {
                logger.debug(STDERR_MARKER, line);
            }
        });
    }

    @Override
    public void waitFor() throws IOException {
        super.waitFor();
    }
}
