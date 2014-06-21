/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.environment.EnvironmentConfig;
import br.com.danielferber.gittocc2.config.environment.EnvironmentConfigChain;
import br.com.danielferber.gittocc2.config.environment.EnvironmentConfigPojo;
import br.com.danielferber.gittocc2.config.environment.EnvironmentConfigProperties;
import br.com.danielferber.gittocc2.config.environment.EnvironmentConfigSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import joptsimple.ValueConversionException;

/**
 *
 * @author Daniel Felix Ferber
 */
public class CommandLineParser {

    private final OptionParser parser = new OptionParser();
    private final OptionSpec<File> propertyFileOpt = parser.accepts("p", "Properties file.").withRequiredArg().ofType(File.class);
    private final OptionSpec<File> gitExecOpt = parser.accepts("g", "Git executable file.").withRequiredArg().required().ofType(File.class);
    private final OptionSpec<File> repositoryDirOpt = parser.accepts("r", "Git repository directory.").withRequiredArg().required().ofType(File.class);
    private final OptionSpec<File> clearToolExecOpt = parser.accepts("c", "CleartTool executable file.").withRequiredArg().required().ofType(File.class);
    private final OptionSpec<File> vobViewDirOpt = parser.accepts("v", "Snapshot vob view directory.").withRequiredArg().required().ofType(File.class);

    public EnvironmentConfigSource commandLineToEnvironmentConfig(String[] argv) {

        final OptionSet options = parser.parse(argv);
        final File propertyFile = propertyFileOpt.value(options);
        final EnvironmentConfig commandLineConfig = readArguments(options);

        if (propertyFile != null) {
            return new EnvironmentConfigChain(readPropertiesFile(propertyFile), commandLineConfig);
        } else {
            return commandLineConfig;
        }
    }

    private EnvironmentConfig readArguments(final OptionSet options) {
        return new EnvironmentConfigPojo(gitExecOpt.value(options), repositoryDirOpt.value(options), clearToolExecOpt.value(options), vobViewDirOpt.value(options));
    }

    private EnvironmentConfig readPropertiesFile(final File propertyFile) throws ValueConversionException {
        try (InputStream is = new FileInputStream(propertyFile)) {
            Properties properties = new Properties();
            properties.load(is);
            return new EnvironmentConfigProperties(properties);
        } catch (FileNotFoundException e) {
            throw new ValueConversionException("Properties file: failed to open.", e);
        } catch (IOException e) {
            throw new ValueConversionException("Properties file: failed to read.", e);
        }
    }

    public void printHelp() {
        try {
            parser.printHelpOn(System.out);
        } catch (IOException ee) {
            ee.printStackTrace();
        }
    }
}
