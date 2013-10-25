/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.git;

import br.com.danielferber.gittocc.io.ProcessWrapper;
import br.com.danielferber.gittocc.process.LineSplittingWriter;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 *
 * @author X7WS
 */
public class GitProcess extends ProcessWrapper {

    public GitProcess(String name, String commandLine, Process process) {
        super(name, commandLine, process);
    }

    public void waitFor() throws IOException {
        super.waitFor();

        if (process.exitValue() != 0) {
            throw new IOException("Processo falhou: " + name);
        }
    }    
}
