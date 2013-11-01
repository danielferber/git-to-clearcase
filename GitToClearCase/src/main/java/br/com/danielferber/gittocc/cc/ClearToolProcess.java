/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.cc;

import br.com.danielferber.gittocc.git.GitProcess;
import br.com.danielferber.gittocc.io.ProcessWrapper;
import br.com.danielferber.gittocc.process.LineSplittingWriter;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 *
 * @author X7WS
 */
public class ClearToolProcess extends ProcessWrapper {

    public ClearToolProcess(String name, String commandLine, Process process) {
        super(name, commandLine, process);
    }

    @Override
    public void waitFor() throws IOException {
        super.waitFor();
    }
}
