/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.git;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author X7WS
 */
public class GitProcessBuilder {

    final File gitDir;
    final File gitExecutable;
    final ProcessBuilder processBuilder;
    String command;
    String name;
    final List<String> parameters = new ArrayList<String>();
    final List<String> options = new ArrayList<String>();
    final List<String> arguments = new ArrayList<String>();

    public GitProcessBuilder(File gitDir, File gitExecutable) {
        this.gitDir = gitDir;
        this.gitExecutable = gitExecutable;
        this.processBuilder = new ProcessBuilder(gitExecutable.getAbsolutePath());
        this.processBuilder.directory(gitDir.getAbsoluteFile());
    }

    public File getGitDir() {
        return gitDir;
    }

    public GitProcessBuilder reset(String name) {
        this.parameters.clear();
        this.options.clear();
        this.arguments.clear();
        this.command = null;
        this.name = name;
        return this;
    }

    public GitProcessBuilder command(String command) {
        this.command = command;
        return this;
    }

    public GitProcessBuilder rawOption(String option) {
        options.add(option);
        return this;
    }

    public GitProcessBuilder option(String option) {
        options.add("--"+option);
        return this;
    }

    public GitProcessBuilder rawParam(String parameter) {
        parameters.add(parameter);
        return this;
    }

    public GitProcessBuilder rawParams(String... parameters) {
        for (String string : parameters) {
            this.parameters.add(string);
        }
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

    public GitProcess create() throws IOException {
        List<String> commandLine = new ArrayList<String>(options.size() + parameters.size() + arguments.size() + 2);
        commandLine.add(gitExecutable.getAbsolutePath());
        commandLine.addAll(options);
        commandLine.add(command);
        commandLine.addAll(parameters);
        commandLine.addAll(arguments);
        processBuilder.command(commandLine);
        GitProcess gitProcess = new GitProcess(name, commandLine, processBuilder.start());
        return gitProcess;
    }

    public static String getProperty(String name, String defaultValue) {
        return System.getProperty("git." + name, defaultValue);
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
