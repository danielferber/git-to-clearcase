/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.EnvironmentConfigProperties;
import br.com.danielferber.gittocc2.config.EnvironmentConfigSource;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import java.io.PrintStream;
import java.util.Properties;
import joptsimple.OptionException;
import org.slf4j.Logger;

/**
 *
 * @author Daniel Felix Ferber
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger("GitToClearCase");
    private static final Logger environmentConfigLogger = LoggerFactory.getLogger(logger, "environmentConfig");

    public static void main(String[] argv) {
        final EnvironmentConfigSource environmentConfig;

        {
            final CommandLineParser clp = new CommandLineParser();
            try {
                environmentConfig = clp.commandLineToEnvironmentConfig(argv);
            } catch (OptionException e) {
                clp.printHelp();
                return;
            }
        }

//        if (environmentConfigLogger.isInfoEnabled()) {
//            EnvironmentConfigProperties c = new EnvironmentConfigProperties(environmentConfig);
//            Properties properties = ().getProperties();
//            PrintStream ps = LoggerFactory.getInfoPrintStream();
//            ps.println("Environment Properties:");
//            ps.println(environmentConfig.to)
//        }
    }

}
