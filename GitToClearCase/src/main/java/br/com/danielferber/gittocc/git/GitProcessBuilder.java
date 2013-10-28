/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.git;

import br.com.danielferber.gittocc.io.LoggingProcessBuilder;
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
public class GitProcessBuilder extends LoggingProcessBuilder<GitProcessBuilder, GitProcess> {

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
    protected List<String> composeCommandLine() {
        List<String> commandLine = new ArrayList<String>(options.size() + parameters.size() + arguments.size() + 2);
        commandLine.add(executableFile.getAbsolutePath());
        commandLine.addAll(options);
        commandLine.add(command);
        commandLine.addAll(parameters);
        commandLine.addAll(arguments);
        return commandLine;
    }

    @Override
    protected GitProcess createProcessWrapper(String name, String commandLine, Process process) {
        return new GitProcess(name, commandLine, process);
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
    final static Marker STDOUT_MARKER = MarkerFactory.getMarker("git_out");
    final static Marker STDERR_MARKER = MarkerFactory.getMarker("git_err");
    final static Marker COMMAND_MARKER = MarkerFactory.getMarker("git_cmd");

    @Override
    protected Marker createCommandMarker() {
        return COMMAND_MARKER;
    }

    @Override
    protected Marker createStdErrMarker() {
        return STDERR_MARKER;
    }

    @Override
    protected Marker createStdOutMarker() {
        return STDOUT_MARKER;
    }

    @Override
    protected Meter createMeter() {
        return MeterFactory.getMeter(GitProcess.class, name);
    }

    @Override
    protected Logger createLogger() {
        return LoggerFactory.getLogger(GitProcess.class, name);
    }
}
