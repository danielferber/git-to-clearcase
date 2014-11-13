/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.config;

import br.com.danielferber.gittocc2.MeterCallable;
import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigChain;
import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigPojo;
import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigProperties;
import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigValidated;
import br.com.danielferber.gittocc2.config.git.GitConfigChain;
import br.com.danielferber.gittocc2.config.git.GitConfigPojo;
import br.com.danielferber.gittocc2.config.git.GitConfigProperties;
import br.com.danielferber.gittocc2.config.git.GitConfigValidated;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import joptsimple.OptionException;
import joptsimple.ValueConversionException;

/**
 *
 * @author Daniel Felix Ferber
 */
public class ConfigurationReaderTask extends MeterCallable<SynchronizerConfiguration> {

    private final String[] arguments;

    public ConfigurationReaderTask(String... arguments) {
        super("ConfigurationReader");
        this.arguments = arguments;
    }

    @Override
    protected SynchronizerConfiguration meteredCall() throws Exception {
        final CommandLine commandLine;
        try {
            commandLine = new CommandLine(arguments);
        } catch (final ValueConversionException | OptionException e) {
            throw new ConfigException("Incorrect command line arguments: {} " + e.getMessage(), e);
        }
        
        if (commandLine.useHelpMessage()) {
            CommandLine.printHelp(System.out);
            return null;
        }

        GitConfigChain gitConfig = new GitConfigChain(commandLine.getGitConfig());
        ClearToolConfigChain clearToolConfig = new ClearToolConfigChain(commandLine.getClearToolConfig());

        if (commandLine.usePropertiesFile()) {
            try (InputStream is = new FileInputStream(commandLine.getPropertiesFile())) {
                Properties properties = new Properties();
                properties.load(is);
                is.close();
                gitConfig.addLowPriority(new GitConfigProperties(properties));
                clearToolConfig.addLowPriority(new ClearToolConfigProperties(properties));
            } catch (final FileNotFoundException e) {
                throw new ConfigException("Properties file: failed to open.", e);
            } catch (final IOException e) {
                throw new ConfigException("Properties file: failed to read.", e);
            }
        }

        gitConfig.addLowPriority(createDefaultGitConfig());
        clearToolConfig.addLowPriority(createDefaultClearToolConfig());

        GitConfigValidated gitConfigValidated = new GitConfigValidated(gitConfig);
        ClearToolConfigValidated clearToolConfigValidated = new ClearToolConfigValidated(clearToolConfig);
        
        if (commandLine.isCompareOnly()) {
            return new CompareConfiguration(gitConfigValidated, clearToolConfigValidated, commandLine.getCompareRoot());
        } else {
            return new ReplayChangesConfiguration(gitConfigValidated, clearToolConfigValidated);

        }
    }

    private static GitConfigPojo createDefaultGitConfig() {
        final GitConfigPojo gitConfigDefault = new GitConfigPojo();
        gitConfigDefault.setApplyDefaultGitConfig(false);
        gitConfigDefault.setCleanLocalGitRepository(false);
        gitConfigDefault.setFastForwardLocalGitRepository(false);
        gitConfigDefault.setFetchRemoteGitRepository(false);
        gitConfigDefault.setResetLocalGitRepository(false);
        return gitConfigDefault;
    }

    private static ClearToolConfigPojo createDefaultClearToolConfig() {
        final ClearToolConfigPojo clearToolConfigDefault = new ClearToolConfigPojo();
        clearToolConfigDefault.setUseCommitStampFile(false);
        clearToolConfigDefault.setUseCounterStampFile(false);
        clearToolConfigDefault.setCommitStampFile(new File("sync-commit-stamp.txt"));
        clearToolConfigDefault.setCounterStampFile(new File("sync-counter-stamp.txt"));
        clearToolConfigDefault.setUseActivity(false);
        clearToolConfigDefault.setUpdateVobRoot(false);
        clearToolConfigDefault.setCheckForgottenCheckout(false);
        return clearToolConfigDefault;
    }

}
