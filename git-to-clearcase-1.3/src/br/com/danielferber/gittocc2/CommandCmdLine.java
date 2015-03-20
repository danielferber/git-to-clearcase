package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.ConfigException;
import br.com.danielferber.gittocc2.git.GitCommander;
import br.com.danielferber.gittocc2.git.GitConfigProperties;
import br.com.danielferber.gittocc2.git.GitTasks;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import joptsimple.ValueConversionException;

/**
 *
 * @author Daniel Felix Ferber
 */
class CommandCmdLine {

    final static OptionParser parser = new OptionParser();
    final static OptionSpec<Void> helpCmdOpt = parser.accepts("help", "Dislay command line instructions.");
    final static OptionSpec<File> propertyFileOpt = parser.accepts("properties", "Properties file.").withRequiredArg().ofType(File.class);
    final static OptionSpec<File> gitExecOpt = parser.accepts("git", "Git executable file.").requiredUnless(propertyFileOpt, helpCmdOpt).withRequiredArg().ofType(File.class);
    final static OptionSpec<File> gitRepositoryDirOpt = parser.accepts("repo", "Git repository directory.").requiredUnless(propertyFileOpt, helpCmdOpt).withRequiredArg().ofType(File.class);
    final static OptionSpec<File> ccClearToolExecOpt = parser.accepts("ct", "CleartTool executable file.").requiredUnless(propertyFileOpt, helpCmdOpt).withRequiredArg().ofType(File.class);
    final static OptionSpec<File> ccVobViewDirOpt = parser.accepts("view", "Snapshot vob view directory.").requiredUnless(propertyFileOpt, helpCmdOpt).withRequiredArg().ofType(File.class);
    final static OptionSpec<Void> setupCmdOpt = parser.accepts("setup", "Set up git repository properties.");
    final static OptionSpec<Void> resetCmdOpt = parser.accepts("reset", "Reset git repository (hard).");
    final static OptionSpec<Void> cleanOpt = parser.accepts("clean", "Clean git repository (directories and ignored files).");
    final static OptionSpec<Void> fetchOpt = parser.accepts("fetch", "Fetch remote branch.");
    final static OptionSpec<Void> fastForwardOpt = parser.accepts("ff", "Fast forward on branch.");
    final static OptionSpec<Void> pullOpt = parser.accepts("pull", "Pull remote branch (fetch and fast foward, no merge).");

    public static void printHelp(final PrintStream ps) {
        try {
            parser.printHelpOn(ps);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static TaskQueue transform(final String[] argv) {
        final OptionSet options;
        try {
            options = parser.parse(argv);
        } catch (final ValueConversionException | OptionException e) {
            throw new ConfigException("Incorrect command line arguments: " + e.getMessage(), e);
        }
        TaskQueue taskQueue = new TaskQueue();
        if (options.has(helpCmdOpt)) {
            taskQueue.add(0, "help", () -> {
                printHelp(System.out);
            });
            return taskQueue;
        }
        Properties properties = readProperties(options);
        fillProperties(options, properties);
        fillTasks(options, properties, taskQueue);
        return taskQueue;
    }

    private static Properties readProperties(final OptionSet options) throws ConfigException {
        Properties properties = null;
        if (options.has(propertyFileOpt)) {
            try (InputStream is = new FileInputStream(propertyFileOpt.value(options))) {
                properties = new Properties();
                properties.load(is);
                return properties;
            } catch (final FileNotFoundException e) {
                throw new ConfigException("Properties file: failed to open.", e);
            } catch (final IOException e) {
                throw new ConfigException("Properties file: failed to read.", e);
            }
        } else {
            return new Properties();
        }
    }

//     return propertyFileOpt.value(options);
    private static void fillProperties(OptionSet options, Properties properties) {
        final GitConfigProperties config = new GitConfigProperties(properties);
        if (options.has(gitExecOpt)) {
            config.setGitExec(gitExecOpt.value(options));
        }
        if (options.has(gitRepositoryDirOpt)) {
            config.setRepositoryDir(gitRepositoryDirOpt.value(options));
        }
    }

    private static void fillTasks(OptionSet options, Properties properties, TaskQueue taskQueue) {
        int priorityCounter = 0;
        GitConfigProperties gitConfig = new GitConfigProperties(properties);
        gitConfig.validate();
        GitCommander gitCommander = new GitCommander(gitConfig);
        GitTasks gitTasks = new GitTasks(gitCommander);
        for (OptionSpec<?> spec : options.specs()) {
            if (spec == resetCmdOpt) {
                taskQueue.add(priorityCounter++, gitTasks.new Reset());
            } else if (spec == cleanOpt) {
                taskQueue.add(priorityCounter++, gitTasks.new Clean());
            } else if (spec == fastForwardOpt) {
                taskQueue.add(priorityCounter++, gitTasks.new FastForward());
            } else if (spec == fetchOpt) {
                taskQueue.add(priorityCounter++, gitTasks.new Fetch());
            } else if (spec ==setupCmdOpt ) {
                taskQueue.add(priorityCounter++, gitTasks.new SetUpRepository());
            }
        }
    }
}
