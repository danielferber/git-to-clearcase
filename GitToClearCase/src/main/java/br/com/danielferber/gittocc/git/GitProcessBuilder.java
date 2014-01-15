/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.git;

import br.com.danielferber.gittocc.io.CommandLineProcessBuilder;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 *
 * @author X7WS
 */
public class GitProcessBuilder extends CommandLineProcessBuilder<GitProcessBuilder, GitProcess> {

    String command;
    final List<String> parameters = new ArrayList<String>();
    final List<String> options = new ArrayList<String>();
    final List<String> arguments = new ArrayList<String>();

    public GitProcessBuilder(File executionDirectory, File executableFile) {
        super(executionDirectory, executableFile);
    }

    public GitProcessBuilder reset(String name) {
        super.reset(name);
        this.parameters.clear();
        this.options.clear();
        this.arguments.clear();
        this.command = null;
        return this;
    }

    public GitProcessBuilder command(String command) {
        this.command = command;
        return this;
    }

    public GitProcessBuilder option(String option) {
        options.add("--" + option);
        return this;
    }

    public GitProcessBuilder shortParameter(String parameter) {
        parameters.add("-" + parameter);
        return this;
    }

    public GitProcessBuilder parameter(String parameter) {
        parameters.add("--" + parameter);
        return this;
    }

    public GitProcessBuilder parameter(String parameter, String value) {
        parameters.add("--" + parameter + "=" + value);
        return this;
    }

    public GitProcessBuilder arguments(String... arguments) {
        for (String string : arguments) {
            this.arguments.add(string);
        }
        return this;
    }

    public GitProcessBuilder argument(String argument) {
        arguments.add(argument);
        return this;
    }

    @Override
    protected List<String> buildCommandLine() {
        List<String> commandLine = new ArrayList<String>(options.size() + parameters.size() + arguments.size() + 2);
        commandLine.add(executableFile.getAbsolutePath());
        commandLine.addAll(options);
        commandLine.add(command);
        commandLine.addAll(parameters);
        commandLine.addAll(arguments);
        return commandLine;
    }

    public GitProcessBuilder noPage() {
        option("no-pager");
        return this;
    }

    public GitProcessBuilder progress() {
        parameter("progress");
        return this;
    }

    public GitProcessBuilder verbose() {
        parameter("verbose");
        return this;
    }

    @Override
    protected GitProcess createProcessWrapper(String name, List<String> commandLine) {
        return new GitProcess(name, commandLine, executionDirectory);
    }
}
