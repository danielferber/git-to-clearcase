/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.config;

import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigSource;
import br.com.danielferber.gittocc2.config.git.GitConfigSource;
import java.io.File;
import java.io.PrintStream;

/**
 *
 * @author Daniel Felix Ferber
 */
public class CompareConfiguration extends SynchronizerConfiguration {

    final File compareRoot;

    public CompareConfiguration(GitConfigSource gitConfig, ClearToolConfigSource clearToolConfig, File compareRoot) {
        super(gitConfig, clearToolConfig);
        this.compareRoot = compareRoot;
    }

    public File getCompareRoot() {
        return compareRoot;
    }

    @Override
    protected void writeSpecificPropertiesToLog(PrintStream ps) {
        ps.println("Infer changes from: file by file comparison");
        ps.println(" - Compare root: " + compareRoot);
    }
}
