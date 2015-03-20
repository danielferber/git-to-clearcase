/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import joptsimple.OptionException;
import joptsimple.ValueConversionException;
import org.slf4j.Logger;

/**
 *
 * @author Daniel Felix Ferber
 */
public class Main {

    public static final Logger logger = LoggerFactory.getLogger("Main");

    public static void main(final String[] argv) {

        try {
            final TaskQueue taskQueue;
            try {
                final CommandLine commandLine = new CommandLine(argv);
                taskQueue = commandLine.createTaskQueue();
            } catch (final ValueConversionException | OptionException e) {
                logger.error("Incorrect command line arguments: {} ", e.getMessage());
                return;
            }
            taskQueue.run();
        } catch (Exception ex) {
            logger.error("Failed to execute.", ex);
        }
    }
}
