/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.task;

import br.com.danielferber.gittocc2.config.ConfigException;
import java.io.File;
import java.io.PrintStream;

/**
 *
 * @author Daniel Felix Ferber
 */
public interface ComparisonConfig {

    public File getCompareRoot();

    public File getCompareAbsoluteRoot();

    static void printConfig(PrintStream ps, ComparisonConfig config) {
        ps.println(" * Comparison configuration:");
        ps.println("   - root directory: " + config.getCompareRoot());
    }

    static void validate(final ComparisonConfig config) {
        final File dir = config.getCompareRoot();
        if (dir == null) {
            throw new ConfigException("Compare root directory: missing value.");
        }
        final File absoluteDir = config.getCompareAbsoluteRoot();
        if (!absoluteDir.exists()) {
            throw new ConfigException("Compare root directory: does not exist.");
        }
        if (!absoluteDir.isDirectory()) {
            throw new ConfigException("Compare root directory: not a directory.");
        }
    }
}
