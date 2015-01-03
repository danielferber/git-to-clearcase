/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.config;

import br.com.danielferber.gittocc2.task.ComparisonConfig;
import java.io.File;
import java.io.PrintStream;
import java.util.Properties;

/**
 *
 * @author Daniel Felix Ferber
 */
public class ComparisonConfigContainter extends ConfigContainer {

    public static final String PROP_COMPARE_ROOT_DIR = "compare.root.dir";
    private final ComparisonBean comparisonBean = new ComparisonBean();

    public ComparisonConfigContainter(Properties properties) {
        super(properties);
    }

    @Override
    public void validateAll() throws ConfigException {
        super.validateAll();
        ComparisonConfig.validate(comparisonBean);
    }

    @Override
    public void printConfig(PrintStream ps) {
        ps.println("Comparison strategy:");
        ComparisonConfig.printConfig(ps, comparisonBean);
        super.printConfig(ps);
    }

    public ComparisonBean getComparisonBean() {
        return comparisonBean;
    }

    public class ComparisonBean implements ComparisonConfig {

        @Override
        public File getCompareRoot() {
            return properties.getFile(PROP_COMPARE_ROOT_DIR);
        }

        public ComparisonBean setCompareRoot(final File file) {
            properties.setFile(PROP_COMPARE_ROOT_DIR, file);
            return this;
        }

        @Override
        public File getCompareAbsoluteRoot() {
            return getCompareRoot().getAbsoluteFile();
        }
    }
}
