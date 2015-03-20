package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.change.ApplyTask;
import br.com.danielferber.gittocc2.cc.CCTasks;
import br.com.danielferber.gittocc2.cc.ClearToolConfigProperties;
import br.com.danielferber.gittocc2.change.ChangeContext;
import br.com.danielferber.gittocc2.change.CompareTask;
import br.com.danielferber.gittocc2.config.ConfigException;
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
class CommandLine {

    private final static OptionParser parser = new OptionParser();
    private final static OptionSpec<Void> helpCmdOpt = parser.accepts("help", "Dislay command line instructions.");
    private final static OptionSpec<File> propertyFileOpt = parser.accepts("properties", "Properties file.").withRequiredArg().ofType(File.class);
    private final static OptionSpec<File> gitExecOpt = parser.accepts("git", "Git executable file.").requiredUnless(propertyFileOpt, helpCmdOpt).withRequiredArg().ofType(File.class);
    private final static OptionSpec<File> gitRepositoryDirOpt = parser.accepts("repo", "Git repository directory.").requiredUnless(propertyFileOpt, helpCmdOpt).withRequiredArg().ofType(File.class);
    private final static OptionSpec<File> ccClearToolExecOpt = parser.accepts("ct", "CleartTool executable file.").requiredUnless(propertyFileOpt, helpCmdOpt).withRequiredArg().ofType(File.class);
    private final static OptionSpec<File> ccVobViewDirOpt = parser.accepts("view", "Snapshot vob view directory.").requiredUnless(propertyFileOpt, helpCmdOpt).withRequiredArg().ofType(File.class);
    private final static OptionSpec<Void> setupCmdOpt = parser.accepts("setup", "Set up git repository properties.");
    private final static OptionSpec<Void> resetCmdOpt = parser.accepts("reset", "Reset git repository (hard).");
    private final static OptionSpec<Void> cleanCmdOpt = parser.accepts("clean", "Clean git repository (directories and ignored files).");
    private final static OptionSpec<Void> fetchCmdOpt = parser.accepts("fetch", "Fetch remote branch.");
    private final static OptionSpec<Void> fastForwardCmdOpt = parser.accepts("ff", "Fast forward on branch.");
    private final static OptionSpec<Void> pullCmdOpt = parser.accepts("pull", "Pull remote branch (fetch and fast foward, no merge).");
    private final static OptionSpec<Void> treeDiffOpt = parser.accepts("treediff", "Load tree diff from vob view commit to repository current commit.");
    private final static OptionSpec<Void> findCheckoutCmdOpt = parser.accepts("findcheckout", "Find files and directoties with checkout.");
    private final static OptionSpec<Void> addViewPrivateCmdOpt = parser.accepts("addprivate", "Find view private files and directories.");
    private final static OptionSpec<Void> checkinAllCheckoutsCmdOpt = parser.accepts("checkin", "Checkin all known checkouts.");
    private final static OptionSpec<Void> updateVobCmdOpt = parser.accepts("update", "Update VOB.");
    private final static OptionSpec<File> compareCmdOpt = parser.accepts("compare", "Create change set by comparing VOB view with Git repository.").withRequiredArg().ofType(File.class);
    private final static OptionSpec<File> applyCmdOpt = parser.accepts("apply", "Apply change set on VOB view directory.").withRequiredArg().ofType(File.class);

    private final OptionSet options;

    public static void printHelp(final PrintStream ps) {
        try {
            parser.printHelpOn(ps);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public CommandLine(final String[] argv) {
        try {
            this.options = parser.parse(argv);
        } catch (final ValueConversionException | OptionException e) {
            throw new ConfigException("Incorrect command line arguments: " + e.getMessage(), e);
        }
    }

    public TaskQueue createTaskQueue() {
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
        GitTasks gitTasks = new GitTasks(gitConfig);

        ClearToolConfigProperties ccConfig = new ClearToolConfigProperties(properties);
        ccConfig.validate();
        CCTasks cCTasks = new CCTasks(ccConfig);
        
        ChangeContext changeContext = new ChangeContext();

        for (OptionSpec<?> spec : options.specs()) {
            if (spec == resetCmdOpt) {
                taskQueue.add(priorityCounter++, gitTasks.new Reset());
            } else if (spec == cleanCmdOpt) {
                taskQueue.add(priorityCounter++, gitTasks.new Clean());
            } else if (spec == fastForwardCmdOpt) {
                taskQueue.add(priorityCounter++, gitTasks.new FastForward());
            } else if (spec == fetchCmdOpt) {
                taskQueue.add(priorityCounter++, gitTasks.new Fetch());
            } else if (spec == setupCmdOpt) {
                taskQueue.add(priorityCounter++, gitTasks.new SetUpRepository());
            } else if (spec == pullCmdOpt) {
                taskQueue.add(priorityCounter++, gitTasks.new Pull());
            } else if (spec == findCheckoutCmdOpt) {
                taskQueue.add(priorityCounter++, cCTasks.new FindCheckouts());
            } else if (spec == checkinAllCheckoutsCmdOpt) {
                taskQueue.add(priorityCounter++, cCTasks.new CheckinAll());
            } else if (spec == updateVobCmdOpt) {
                taskQueue.add(priorityCounter++, cCTasks.new UpdateVob());
            } else if (spec == compareCmdOpt) {
                taskQueue.add(priorityCounter++, new CompareTask(changeContext, gitConfig.getRepositoryAbsoluteDir().toPath(), ccConfig.getVobViewAbsoluteDir().toPath(), options.valueOf(compareCmdOpt).toPath()));
            } else if (spec == applyCmdOpt) {
                taskQueue.add(priorityCounter++, new ApplyTask(changeContext, ccConfig, options.valueOf(compareCmdOpt).toPath()));
            }
        }
    }
}
