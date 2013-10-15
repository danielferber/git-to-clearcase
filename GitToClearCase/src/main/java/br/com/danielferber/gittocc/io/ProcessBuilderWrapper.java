/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.io;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author X7WS
 */
public abstract class ProcessBuilderWrapper<Wrapper extends ProcessBuilderWrapper, Wrapper2 extends ProcessWrapper> {

    protected final File executionDirectory;
    protected final File executableFile;
    protected final ProcessBuilder processBuilder;
    protected String name;

    public ProcessBuilderWrapper(File executionDirectory, File executableFile) {
        this.executionDirectory = executionDirectory;
        this.executableFile = executableFile;
        this.processBuilder = new ProcessBuilder(executableFile.getAbsolutePath());
        this.processBuilder.directory(executionDirectory.getAbsoluteFile());
    }

    public Wrapper reset(String name) {
        this.name = name;
        return (Wrapper) this;
    }

    public Wrapper2 create() throws IOException {
        List<String> commandLine = composeCommandLine();
        String commandLineStr = formatCommandLine(commandLine);
        processBuilder.command(commandLine);
        Wrapper2 gitProcess = createProcessWrapper(name, commandLineStr, processBuilder.start());
        return gitProcess;
    }

    private static String formatCommandLine(List<String> commands) {
        String executable = new File(commands.get(0)).getName();
        StringBuilder sb = new StringBuilder(executable);
        final Iterator<String> iterator = commands.iterator();
        iterator.next();
        for (Iterator<String> it = iterator; it.hasNext();) {
            sb.append(' ');
            sb.append(it.next());
        }
        return sb.toString();
    }

    protected abstract List<String> composeCommandLine();

    protected abstract Wrapper2 createProcessWrapper(String name, String commandLine, Process process);
}
