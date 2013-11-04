/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.cc;

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
public class ClearToolProcess extends LoggingProcessWrapper<ClearToolProcess> {

    final static Logger logger = LoggerFactory.getLogger(ClearToolProcess.class.getSimpleName());

    public ClearToolProcess(String name, List<String> commandLine, File directory) {
        super(name, commandLine, directory,
                MeterFactory.getMeter(logger, name),
                LoggerFactory.getLogger(logger, name),
                MarkerFactory.getMarker("ct_out"),
                MarkerFactory.getMarker("ct_err"),
                MarkerFactory.getMarker("ct_cmd"));
    }
}
