/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.git;

import br.com.danielferber.gittocc.io.LoggingProcessWrapper;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;
import java.io.File;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.MarkerFactory;

/**
 *
 * @author X7WS
 */
public class GitProcess extends LoggingProcessWrapper<GitProcess> {

    final static Logger logger = LoggerFactory.getLogger(GitProcess.class.getSimpleName());

    public GitProcess(String name, List<String> commandLine, File directory) {
        super(name, commandLine, directory,
                MeterFactory.getMeter(logger, name),
                LoggerFactory.getLogger(logger, name),
                MarkerFactory.getMarker("git_out"),
                MarkerFactory.getMarker("git_err"),
                MarkerFactory.getMarker("git_cmd"));
    }
}
