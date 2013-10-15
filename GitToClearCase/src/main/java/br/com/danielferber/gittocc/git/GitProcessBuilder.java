/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.git;

import br.com.danielferber.gittocc.io.ProcessBuilderWrapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author X7WS
 */
public class GitProcessBuilder extends ProcessBuilderWrapper<GitProcessBuilder, GitProcess> {

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
}
