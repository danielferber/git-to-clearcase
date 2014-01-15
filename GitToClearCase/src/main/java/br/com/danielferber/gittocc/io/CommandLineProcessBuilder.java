/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * A skeleton of a builder that creates a processes for a specific command line
 * executable. The extending class shall offer friendly API for the command line
 * executable domain and hide the complexity of generating the command line
 * arguments for the executable. The resulting process is exposed as class
 * extending CommandLineProcess, which shall offer a friendly API to retrieve
 * the process output.
 * <br> The command line arguments itself are queried from buildCommandLine to
 * be overridden. This method shall translate the operation described by the
 * current factory state into command line arguments understood by the
 * executable.
 * <br> The process is wrapped by createProcessWrapper to be overridden.
 * <br> All processes created by this builder call the same command line
 * executable within the same working directory. Only command line arguments
 * vary according to the builder stated.
 * <br> Call reset to start building a new command.
 * <br> Call create to build a process based on the current factory
 * configuration.
 *
 * @author Daniel Felix Ferber
 */
public abstract class CommandLineProcessBuilder<BuilderType extends CommandLineProcessBuilder, ProcessType extends CommandLineProcess> {

    /**
     * The working directory of the next CommandLineProcess created by this
     * builder.
     */
    protected final File executionDirectory;
    /**
     * The command line executable of the next CommandLineProcess created by
     * this builder.
     */
    protected final File executableFile;
    /**
     * The name of the next CommandLineProcess created by this builder.
     */
    protected String name;

    public CommandLineProcessBuilder(File executionDirectory, File executableFile) {
        this.executionDirectory = executionDirectory;
        this.executableFile = executableFile;
    }

    public BuilderType reset(String name) {
        this.name = name;
        return (BuilderType) this;
    }

    public final ProcessType create() throws IOException {
        final String formattedName = formatName();
        final List<String> commandLine = buildCommandLine();
        final ProcessType processWrapper = createProcessWrapper(formattedName, commandLine);
        return processWrapper;
    }

    /**
     * Provides a pretty name for the process. Override to customize. By
     * default, return the name of the process.
     *
     * @return the pretty name for the process.
     */
    protected String formatName() {
        return name;
    }

    /**
     * Builds the command used to launch the process. Override with a suitable
     * implementation that is specific for this builder domain. Typically, it
     * uses the builder state to gather information for the command line.
     *
     * @return the command used to launch the process
     */
    protected abstract List<String> buildCommandLine();

    /**
     * Create a specific ProcessWrapper suitable for the ProcessBuilder domain.
     * This ProcessWrapper must create and start the underlying native process.
     *
     * @param process the native process being wrapped
     * @return the ProcessWrapper suitable for the ProcessBuilder domain.
     */
    protected abstract ProcessType createProcessWrapper(String name, List<String> commandLine);
}
