/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.EnvironmentConfig;
import br.com.danielferber.gittocc2.config.EnvironmentConfigPojo;
import java.io.File;
import java.io.IOException;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

/**
 *
 * @author Daniel Felix Ferber
 */
public class Main {

    public static void main(String[] argv) {
        OptionParser parser = new OptionParser();
        OptionSpec<File> gitExec = parser.accepts("g", "Git executable file.").withRequiredArg().required().ofType(File.class);
        OptionSpec<File> repositoryDir = parser.accepts("r", "Git repository directory.").withRequiredArg().required().ofType(File.class);
        OptionSpec<File> clearToolExec = parser.accepts("c", "CleartTool executable file.").withRequiredArg().required().ofType(File.class);
        OptionSpec<File> vobViewDir = parser.accepts("v", "Snapshot vob view directory.").withRequiredArg().required().ofType(File.class);

        EnvironmentConfig config;
        try { 
            OptionSet options = parser.parse(argv);
            config = new EnvironmentConfigPojo(gitExec.value(options), repositoryDir.value(options), clearToolExec.value(options), vobViewDir.value(options));
        } catch (OptionException e) {
            try {
                parser.printHelpOn(System.out);
            } catch (IOException ee) {
                ee.printStackTrace();
            }
            return ;
        }
        
    }
}
