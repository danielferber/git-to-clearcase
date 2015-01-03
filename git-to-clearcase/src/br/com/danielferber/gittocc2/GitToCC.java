/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.ConfigContainer;
import br.com.danielferber.gittocc2.config.ConfigException;
import br.com.danielferber.gittocc2.config.ConfigFactory;
import br.com.danielferber.gittocc2.config.PrintHelpException;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import java.io.IOException;
import java.io.PrintStream;
import joptsimple.OptionException;
import joptsimple.ValueConversionException;
import org.slf4j.Logger;

/**
 *
 * @author Daniel Felix Ferber
 */
public class GitToCC {

    public static final Logger logger = LoggerFactory.getLogger("GitToCC");

    public static void main(final String[] argv) {
        final ConfigContainer container;
        try {
            container = ConfigFactory.parse(argv);
        } catch (final ValueConversionException | OptionException e) {
            logger.error("Incorrect command line arguments: {} ", e.getMessage());
            return;
        } catch (PrintHelpException e) {
            try (PrintStream ps = LoggerFactory.getInfoPrintStream(logger)) {
                ConfigFactory.printHelp(ps);
            } catch (final IOException ex) {
                logger.error("Failed to print command line help.", ex);
            }
            return;
        }

        if (logger.isInfoEnabled()) {
            try (PrintStream ps = LoggerFactory.getInfoPrintStream(logger)) {
                container.printConfig(ps);
            }
        }

        try {
            container.validateAll();
        } catch (ConfigException e) {
            logger.error("Incorrect configuration: {}", e.getMessage());
            return;
        }
    }
}
