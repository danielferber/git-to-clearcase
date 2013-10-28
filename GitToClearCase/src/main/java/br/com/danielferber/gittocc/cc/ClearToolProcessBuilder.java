/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.cc;

import br.com.danielferber.gittocc.git.GitProcessBuilder;
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
public class ClearToolProcessBuilder extends LoggingProcessBuilder<ClearToolProcessBuilder, ClearToolProcess> {

    String command;
//    final List<String> parameters = new ArrayList<String>();
//    final List<String> options = new ArrayList<String>();
    final List<String> arguments = new ArrayList<String>();

    public ClearToolProcessBuilder(File vobDir, File ctExecutable) {
        super(vobDir, ctExecutable);
    }

    @Override
    public ClearToolProcessBuilder reset(String name) {
        super.reset(name);
//        this.parameters.clear();
//        this.options.clear();
        this.arguments.clear();
        this.command = null;
        return this;
    }

    public ClearToolProcessBuilder command(String command) {
        this.command = command;
        return this;
    }
//
//    public GitProcessBuilder option(String option) {
//        options.add("--"+option);
//        return this;
//    }
//
//    public GitProcessBuilder parameter(String parameter) {
//        parameters.add("--" + parameter);
//        return this;
//    }
//
//    public GitProcessBuilder parameter(String parameter, String value) {
//        parameters.add("--" + parameter + "=" + value);
//        return this;
//    }

    public ClearToolProcessBuilder arguments(String... arguments) {
        for (String string : arguments) {
            this.arguments.add(string);
        }
        return this;
    }

    public ClearToolProcessBuilder argument(String argument) {
        arguments.add(argument);
        return this;
    }

    @Override
    protected List<String> composeCommandLine() {
        List<String> commandLine = new ArrayList<String>(/* options.size() + parameters.size() +*/arguments.size() + 2);
        commandLine.add(executableFile.getAbsolutePath());
//        commandLine.addAll(options);
        commandLine.add(command);
//        commandLine.addAll(parameters);
        commandLine.addAll(arguments);
        return commandLine;
    }

    @Override
    protected ClearToolProcess createProcessWrapper(String name, String commandLine, Process process) {
        return new ClearToolProcess(name, commandLine, process);
    }

    public ClearToolProcessBuilder preserveTime() {
        arguments.add("-ptime");
        return this;
    }

    public ClearToolProcessBuilder noComment() {
        arguments.add("-nc");
        return this;
    }

    public ClearToolProcessBuilder force() {
        arguments.add("-force");
        return this;
    }

    final static Marker STDOUT_MARKER = MarkerFactory.getMarker("ct_out");
    final static Marker STDERR_MARKER = MarkerFactory.getMarker("ct_err");
    final static Marker COMMAND_MARKER = MarkerFactory.getMarker("ct_cmd");

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
        return MeterFactory.getMeter(ClearToolProcess.class, name);
    }

    @Override
    protected Logger createLogger() {
        return LoggerFactory.getLogger(ClearToolProcess.class, name);
    }
}
