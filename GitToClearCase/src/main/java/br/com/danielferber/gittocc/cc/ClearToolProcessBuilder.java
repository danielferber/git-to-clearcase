/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.cc;

import br.com.danielferber.gittocc.io.CommandLineProcessBuilder;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;

/**
 *
 * @author X7WS
 */
public class ClearToolProcessBuilder extends CommandLineProcessBuilder<ClearToolProcessBuilder, ClearToolProcess> {

    String command;
    final List<String> arguments = new ArrayList<String>();
    final static Logger logger = LoggerFactory.getLogger(ClearToolProcess.class.getSimpleName());

    public ClearToolProcessBuilder(File vobDir, File ctExecutable) {
        super(vobDir, ctExecutable);
    }

    @Override
    public ClearToolProcessBuilder reset(String name) {
        super.reset(name);
        this.arguments.clear();
        this.command = null;
        return this;
    }

    public ClearToolProcessBuilder command(String command) {
        this.command = command;
        return this;
    }

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
    protected List<String> buildCommandLine() {
        List<String> commandLine = new ArrayList<String>(/* options.size() + parameters.size() +*/arguments.size() + 2);
        commandLine.add(executableFile.getAbsolutePath());
        commandLine.add(command);
        commandLine.addAll(arguments);
        return commandLine;
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
    
    public ClearToolProcessBuilder noquery() {
        arguments.add("-nquery");
        return this;
    }

    @Override
    protected ClearToolProcess createProcessWrapper(String name, List<String> commandLine) {
        return new ClearToolProcess(name, commandLine, executionDirectory);
    }
}
