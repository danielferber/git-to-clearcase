/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.io.process;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;

/**
 * A skeleton of a builder that creates a processes for a specific constant
 * command line executable on a specific constant execution directory. The
 * extending class shall offer friendly API for the command line executable
 * domain and hide the complexity of generating the command line arguments for
 * the executable.
 * <br> The command line arguments itself are queried from buildCommandLine to
 * be overridden. This method shall translate the operation described by the
 * current factory state into command line arguments understood by the
 * executable.
 * <br> All processes created by this builder call the same command line
 * executable within the same working directory. Only command line arguments
 * vary according to the builder stated.
 * <br> Call reset to start building a new command.
 * <br> Call create to build a process based on the current factory
 * configuration.
 *
 * @author Daniel Felix Ferber
 */
public class CommandLineProcessBuilder {

    /**
     * The working directory of the CommandLineProcess created by this builder.
     */
    protected final File executionDirectory;
    /**
     * The command line executable of the CommandLineProcess created by this
     * builder.
     */
    protected final File executableFile;
    /**
     * The logger that tracks start/finish of the CommandLineProcess created by
     * this builder.
     */
    protected final Logger logger;
    /**
     * The name of the next CommandLineProcess created by this builder.
     */
    protected String name;
    
    String command;
    final List<String> parameters = new ArrayList<String>();
    final List<String> options = new ArrayList<String>();
    final List<String> arguments = new ArrayList<String>();

    public CommandLineProcessBuilder(File executionDirectory, File executableFile, Logger logger) {
        this.executionDirectory = executionDirectory;
        this.executableFile = executableFile;
        this.logger = logger;
    }

    public CommandLineProcessBuilder reset(String name) {
        this.name = name;
        this.parameters.clear();
        this.options.clear();
        this.arguments.clear();
        this.command = null;

        return (CommandLineProcessBuilder) this;
    }

    public final CommandLineProcess create() {
        return new CommandLineProcess(this.name, buildCommandLine(), this.executionDirectory, this.logger);
    }

    public CommandLineProcessBuilder command(String command) {
        this.command = command;
        return (CommandLineProcessBuilder) this;
    }

    public CommandLineProcessBuilder option(String option) {
        options.add("--" + option);
        return (CommandLineProcessBuilder) this;
    }

    public CommandLineProcessBuilder shortParameter(String parameter) {
        parameters.add("-" + parameter);
        return (CommandLineProcessBuilder) this;
    }

    public CommandLineProcessBuilder parameter(String parameter) {
        parameters.add("--" + parameter);
        return (CommandLineProcessBuilder) this;
    }

    public CommandLineProcessBuilder parameter(String parameter, String value) {
        parameters.add("--" + parameter + "=" + value);
        return (CommandLineProcessBuilder) this;
    }

    public CommandLineProcessBuilder arguments(String... arguments) {
        for (String string : arguments) {
            this.arguments.add(string);
        }
        return (CommandLineProcessBuilder) this;
    }

    public CommandLineProcessBuilder argument(String argument) {
        arguments.add(argument);
        return (CommandLineProcessBuilder) this;
    }

    /**
     * @return the command used to launch the process
     */
    protected List<String> buildCommandLine() {
        List<String> commandLine = new ArrayList<String>(options.size() + parameters.size() + arguments.size() + 2);
        commandLine.add(executableFile.getAbsolutePath());
        commandLine.addAll(options);
        commandLine.add(command);
        commandLine.addAll(parameters);
        commandLine.addAll(arguments);
        return commandLine;
    }
}
