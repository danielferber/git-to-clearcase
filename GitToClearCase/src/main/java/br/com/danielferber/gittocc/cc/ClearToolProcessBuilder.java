/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.cc;

import br.com.danielferber.gittocc.io.ProcessBuilderWrapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import br.com.danielferber.gittocc.git.ClearToolProcess;

/**
 *
 * @author X7WS
 */
public class ClearToolProcessBuilder extends ProcessBuilderWrapper<ClearToolProcessBuilder, ClearToolProcess> {
    String command;
//    final List<String> parameters = new ArrayList<String>();
//    final List<String> options = new ArrayList<String>();
    final List<String> arguments = new ArrayList<String>();

    public ClearToolProcessBuilder(File vobDir, File ctExecutable) {
        super(vobDir, ctExecutable);
    }

    public ClearToolProcessBuilder reset(String name) {
//        this.parameters.clear();
//        this.options.clear();
        this.arguments.clear();
        this.command = null;
        return this;
    }

//    public GitProcessBuilder command(String command) {
//        this.command = command;
//        return this;
//    }
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
        List<String> commandLine = new ArrayList<String>(/* options.size() + parameters.size() +*/ arguments.size() + 2);
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
}
