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
public abstract class ProcessFactory<ProcessBuilderType extends ProcessFactory, ProcessType extends ProcessWrapper> {

    protected final File executionDirectory;
    protected final File executableFile;
    protected String name;

    public ProcessFactory(File executionDirectory, File executableFile) {
        this.executionDirectory = executionDirectory;
        this.executableFile = executableFile;
    }

    public ProcessBuilderType reset(String name) {
        this.name = name;
        return (ProcessBuilderType) this;
    }

    public final ProcessType create() throws IOException {
        String formattedName = formatName();
        List<String> commandLine = buildCommandLine();
        ProcessType processWrapper = createProcessWrapper(formattedName, commandLine);
        return processWrapper;
    }

    /**
     * Provides a pretty name for the process. Override to customize.
     *
     * @return the pretty name for the process.
     */
    protected String formatName() {
        return name;
    }

    /**
     * Builds the command used to launch the process. Override with a suitable
     * implementation that is specific for this ProcessBuilder domain.
     * Typically, it uses the ProcessBuilder state to gather information for the
     * command line.
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
