/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.EnvironmentConfig;
import br.com.danielferber.gittocc2.config.EnvironmentConfigException;
import br.com.danielferber.gittocc2.config.EnvironmentConfigPojo;
import br.com.danielferber.gittocc2.config.EnvironmentConfigSource;
import br.com.danielferber.gittocc2.config.EnvironmentConfigValidated;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import java.io.PrintStream;
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
        final EnvironmentConfig environmentConfig;

        {
            final CommandLineParser clp = new CommandLineParser();
            try {
                environmentConfig = new EnvironmentConfigPojo(clp.commandLineToEnvironmentConfig(argv));
            } catch (OptionException e) {
                clp.printHelp();
                return;
            }
        }

        if (environmentConfigLogger.isInfoEnabled()) {
            try (PrintStream ps = LoggerFactory.getInfoPrintStream(environmentConfigLogger)) {
                ps.println("Environment Properties:");
                ps.println(" - git executable file: " + environmentConfig.getGitExec());
                ps.println(" - git repository directory: " + environmentConfig.getRepositoryDir());
                ps.println(" - cleartool executable file: " + environmentConfig.getClearToolExec());
                ps.println(" - snapshot vob view directory: " + environmentConfig.getVobViewDir());
            }
        }

        try {
            EnvironmentConfig validatedConfig = new EnvironmentConfigValidated(environmentConfig);
            validatedConfig.getGitExec();
            validatedConfig.getRepositoryDir();
            validatedConfig.getClearToolExec();
            validatedConfig.getVobViewDir();
        } catch (EnvironmentConfigException e) {
            logger.error("Environmente configuration. "+e.getMessage());
        }
    }

}
