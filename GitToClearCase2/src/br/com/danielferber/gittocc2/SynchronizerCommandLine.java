/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.environment.EnvironmentConfigChain;
import br.com.danielferber.gittocc2.config.environment.EnvironmentConfigPojo;
import br.com.danielferber.gittocc2.config.environment.EnvironmentConfigProperties;
import br.com.danielferber.gittocc2.config.environment.EnvironmentConfigSource;
import br.com.danielferber.gittocc2.config.sync.SyncConfigChain;
import br.com.danielferber.gittocc2.config.sync.SyncConfigPojo;
import br.com.danielferber.gittocc2.config.sync.SyncConfigProperties;
import br.com.danielferber.gittocc2.config.sync.SyncConfigSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import joptsimple.ValueConversionException;

/**
 *
 * @author Daniel Felix Ferber
 */
class SynchronizerCommandLine {

    final static OptionParser parser = new OptionParser();
    final static OptionSpec<File> propertyFileOpt = parser.accepts("p", "Properties file.").withRequiredArg().ofType(File.class);
    final static OptionSpec<File> gitExecOpt = parser.accepts("g", "Git executable file.").withRequiredArg().required().ofType(File.class);
    final static OptionSpec<File> repositoryDirOpt = parser.accepts("r", "Git repository directory.").withRequiredArg().required().ofType(File.class);
    final static OptionSpec<File> clearToolExecOpt = parser.accepts("c", "CleartTool executable file.").withRequiredArg().required().ofType(File.class);
    final static OptionSpec<File> vobViewDirOpt = parser.accepts("v", "Snapshot vob view directory.").withRequiredArg().required().ofType(File.class);

    static void printHelp(PrintStream ps) throws IOException {
        parser.printHelpOn(ps);
    }

    final OptionSet options;
    final Properties properties;

    SynchronizerCommandLine(String[] argv) {
        options = parser.parse(argv);
        File propertyFile = propertyFileOpt.value(options);

        if (propertyFile != null) {
            try (InputStream is = new FileInputStream(propertyFile)) {
                properties = new Properties();
                properties.load(is);
                is.close();
            } catch (FileNotFoundException e) {
                throw new ValueConversionException("Properties file: failed to open.", e);
            } catch (IOException e) {
                throw new ValueConversionException("Properties file: failed to read.", e);
            }
        } else {
            properties = null;
        }
    }

    EnvironmentConfigSource getEnvironmentConfig() {
        final EnvironmentConfigPojo config = new EnvironmentConfigPojo(gitExecOpt.value(options), repositoryDirOpt.value(options), clearToolExecOpt.value(options), vobViewDirOpt.value(options));
        if (properties == null) {
            return config;
        }
        return new EnvironmentConfigChain(config, new EnvironmentConfigProperties(properties));
    }

    SyncConfigSource getSyncConfig() {
        final SyncConfigPojo config = new SyncConfigPojo();
        if (properties == null) {
            return config;
        }
        return new SyncConfigChain(config, new SyncConfigProperties(properties));
    }

}
