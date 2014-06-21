/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.ConfigException;
import br.com.danielferber.gittocc2.config.environment.EnvironmentConfigPojo;
import br.com.danielferber.gittocc2.config.environment.EnvironmentConfigSource;
import br.com.danielferber.gittocc2.config.environment.EnvironmentConfigValidated;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import java.io.PrintStream;
import joptsimple.OptionException;
import joptsimple.ValueConversionException;
import org.slf4j.Logger;

/**
 *
 * @author Daniel Felix Ferber
 */
public class Synchronizer {

    private static final Logger logger = LoggerFactory.getLogger("GitToClearCase");

    public static void main(String[] argv) {
        final EnvironmentConfigSource environmentConfig;

        {
            final CommandLineParser clp = new CommandLineParser();
            try {
                environmentConfig = new EnvironmentConfigPojo(clp.commandLineToEnvironmentConfig(argv));
            } catch (ValueConversionException e) {
                logger.error("Incorrect command line arguments: {} ", e.getMessage());
                return;
            } catch (OptionException e) {
                logger.error("Incorrect command line arguments: {} ", e.getMessage());
                clp.printHelp();
                return;
            }
        }

        if (logger.isInfoEnabled()) {
            try (PrintStream ps = LoggerFactory.getInfoPrintStream(logger)) {
                ps.println("Environment Properties:");
                ps.println(" - git executable file: " + environmentConfig.getGitExec());
                ps.println(" - git repository directory: " + environmentConfig.getRepositoryDir());
                ps.println(" - cleartool executable file: " + environmentConfig.getClearToolExec());
                ps.println(" - snapshot vob view directory: " + environmentConfig.getVobViewDir());
            }
        }

        try {
            EnvironmentConfigSource validatedConfig = new EnvironmentConfigValidated(environmentConfig);
            validatedConfig.getGitExec();
            validatedConfig.getRepositoryDir();
            validatedConfig.getClearToolExec();
            validatedConfig.getVobViewDir();
        } catch (ConfigException e) {
            logger.error("Incorrent environment configuration: {}", e.getMessage());
        }
    }

}
