package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.cc.CCTasks;
import br.com.danielferber.gittocc2.cc.ClearToolConfig;
import br.com.danielferber.gittocc2.cc.ClearToolConfigProperties;
import br.com.danielferber.gittocc2.cc.ClearToolConfigValidated;
import br.com.danielferber.gittocc2.change.ApplyTask;
import br.com.danielferber.gittocc2.change.ChangeConfig;
import br.com.danielferber.gittocc2.change.ChangeConfigProperties;
import br.com.danielferber.gittocc2.change.ChangeContext;
import br.com.danielferber.gittocc2.change.ChangeTasks;
import br.com.danielferber.gittocc2.change.CompareTask;
import br.com.danielferber.gittocc2.change.DiffTreeTask;
import br.com.danielferber.gittocc2.config.ConfigException;
import br.com.danielferber.gittocc2.git.GitConfig;
import br.com.danielferber.gittocc2.git.GitConfigProperties;
import br.com.danielferber.gittocc2.git.GitConfigValidated;
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

    private static final String setupCmdStr = "Set up git repository properties.";
    private static final String resetCmdStr = "Reset git repository (hard).";
    private static final String cleanCmdStr = "Clean git repository (directories and ignored files).";
    private static final String fetchCmdStr = "Fetch remote branch.";
    private static final String fastForwardCmdStr = "Fast forward on branch.";
    private static final String pullCmdStr = "Pull remote branch (fetch and fast foward, no merge).";
    private static final String diffTreeCmdStr = "Load tree diff from vob view commit to repository current commit.";
    private static final String findCheckoutCmdStr = "Find files and directoties with checkout.";
    private static final String addViewPrivateCmdStr = "Find view private files and directories.";
    private static final String checkinAllCmdStr = "Checkin all known checkouts.";
    private static final String updateVobCmdStr = "Update vob.";
    private static final String compareCmdStr = "Create change set by comparing vob view with Git repository.";
    private static final String applyCmdStr = "Apply change set on vob view directory.";
    private static final String unsetActivityCmdStr = "Unset activity on vob view directory.";
    private static final String defineActivityCmdStr = "Set activity on vob view directory.";
    private static final String checkoutCmdStr = "Checkout or create stamp files to lock vob view directory.";
    private static final String writeStampCmdStr = "Write stamp files on vob view directory.";
    private static final String updateStampCmdStr = "Update stamp files on vob view directory.";

    private final static OptionParser parser = new OptionParser();
    private final static OptionSpec<Void> helpCmdOpt = parser.accepts("help", "Dislay command line instructions.");
    private final static OptionSpec<File> propertyFileOpt = parser.accepts("properties", "Properties file.").withRequiredArg().ofType(File.class);
    private final static OptionSpec<File> gitExecOpt = parser.accepts("git", "Git executable file.").requiredUnless(propertyFileOpt, helpCmdOpt).withRequiredArg().ofType(File.class);
    private final static OptionSpec<File> gitRepositoryDirOpt = parser.accepts("repo", "Git repository directory.").requiredUnless(propertyFileOpt, helpCmdOpt).withRequiredArg().ofType(File.class);
    private final static OptionSpec<File> ccClearToolExecOpt = parser.accepts("ct", "CleartTool executable file.").requiredUnless(propertyFileOpt, helpCmdOpt).withRequiredArg().ofType(File.class);
    private final static OptionSpec<File> ccVobViewDirOpt = parser.accepts("view", "Snapshot vob view directory.").requiredUnless(propertyFileOpt, helpCmdOpt).withRequiredArg().ofType(File.class);
    private final static OptionSpec<Void> setupCmdOpt = parser.accepts("setup", setupCmdStr);
    private final static OptionSpec<Void> resetCmdOpt = parser.accepts("reset", resetCmdStr);
    private final static OptionSpec<Void> cleanCmdOpt = parser.accepts("clean", cleanCmdStr);
    private final static OptionSpec<Void> fetchCmdOpt = parser.accepts("fetch", fetchCmdStr);
    private final static OptionSpec<Void> fastForwardCmdOpt = parser.accepts("ff", fastForwardCmdStr);
    private final static OptionSpec<Void> pullCmdOpt = parser.accepts("pull", pullCmdStr);
    private final static OptionSpec<Void> diffTreeOpt = parser.accepts("diff-tree", diffTreeCmdStr);
    private final static OptionSpec<Void> findCheckoutCmdOpt = parser.accepts("find-checkout", findCheckoutCmdStr);
    private final static OptionSpec<Void> addViewPrivateCmdOpt = parser.accepts("add-private", addViewPrivateCmdStr);
    private final static OptionSpec<Void> checkinAllCmdOpt = parser.accepts("checkin", checkinAllCmdStr);
    private final static OptionSpec<Void> updateVobCmdOpt = parser.accepts("update", updateVobCmdStr);
    private final static OptionSpec<File> compareCmdOpt = parser.accepts("compare", compareCmdStr).withRequiredArg().ofType(File.class);
    private final static OptionSpec<Void> applyCmdOpt = parser.accepts("apply", applyCmdStr);
    private final static OptionSpec<Void> unsetActivityCmdOpt = parser.accepts("unset-activity", unsetActivityCmdStr);
    private final static OptionSpec<String> defineActivityCmdOpt = parser.accepts("activity", defineActivityCmdStr).withOptionalArg().ofType(String.class);

    private final static OptionSpec<File> counterStampFileOpt = parser.accepts("counter-stamp", "Counter stamp file within vob view directory.").withRequiredArg().ofType(File.class);
    private final static OptionSpec<File> commitStampFileOpt = parser.accepts("commit-stamp", "Commit stamp file within vob view directory.").withRequiredArg().ofType(File.class);
    private final static OptionSpec<Long> counterStampOverrideOpt = parser.accepts("counter", "Assume counter for vob view directory state.").withRequiredArg().ofType(Long.class);
    private final static OptionSpec<String> commitStampOverrideOpt = parser.accepts("commit", "Assume commit for vob view directory state.").withRequiredArg().ofType(String.class);

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
        
        final Properties properties = readProperties(options);
        final GitConfig gitConfig = new GitConfigValidated(extractGitConfig(options, properties));
        final GitTasks gitTasks = new GitTasks(gitConfig);
        final ClearToolConfig ccConfig = new ClearToolConfigValidated(extractClearToolConfig(options, properties));
        final CCTasks cCTasks = new CCTasks(ccConfig);
        final ChangeContext changeContext = new ChangeContext();
        
        int priorityCounter = 0;
        ChangeConfig changeConfig = null;
        ChangeTasks changeTasks = null;
        ChangeTasks.CheckoutStampTask checkoutStampTask = null;
        ChangeTasks.UpdateStampTask updateStampTask = null;
        
        for (OptionSpec<?> spec : options.specs()) {
            if (spec == resetCmdOpt) {
                taskQueue.add(priorityCounter++, resetCmdStr, gitTasks.new Reset());
            } else if (spec == cleanCmdOpt) {
                taskQueue.add(priorityCounter++, cleanCmdStr, gitTasks.new Clean());
            } else if (spec == fastForwardCmdOpt) {
                taskQueue.add(priorityCounter++, fastForwardCmdStr, gitTasks.new FastForward());
            } else if (spec == fetchCmdOpt) {
                taskQueue.add(priorityCounter++, fetchCmdStr, gitTasks.new Fetch());
            } else if (spec == setupCmdOpt) {
                taskQueue.add(priorityCounter++, setupCmdStr, gitTasks.new SetUpRepository());
            } else if (spec == pullCmdOpt) {
                taskQueue.add(priorityCounter++, pullCmdStr, gitTasks.new Pull());
            } else if (spec == findCheckoutCmdOpt) {
                taskQueue.add(priorityCounter++, findCheckoutCmdStr, cCTasks.new FindCheckouts());
            } else if (spec == checkinAllCmdOpt) {
                taskQueue.add(priorityCounter++, checkinAllCmdStr, cCTasks.new CheckinAll());
            } else if (spec == unsetActivityCmdOpt) {
                taskQueue.add(priorityCounter++, unsetActivityCmdStr, cCTasks.new UnsetActivity());
            } else if (spec == defineActivityCmdOpt) {
                if (options.hasArgument(defineActivityCmdOpt)) {
                    taskQueue.add(priorityCounter++, defineActivityCmdStr, changeTasks.new DefineActivityStampTask(options.valueOf(defineActivityCmdOpt)));
                } else {
                    changeConfig = extractChangeConfig(changeConfig, ccConfig, options, properties);
//                    taskQueue.add(priorityCounter++, defineActivityCmdStr, changeTasks.new DefineActivityStampTask(changeConfig.getActivityName()));
                }
            } else if (spec == updateVobCmdOpt) {
                taskQueue.add(priorityCounter++, updateVobCmdStr, cCTasks.new UpdateVob());
            } else if (spec == compareCmdOpt) {
                taskQueue.add(priorityCounter++, compareCmdStr, new CompareTask(changeContext, gitConfig.getRepositoryAbsoluteDir().toPath(), ccConfig.getVobViewAbsoluteDir().toPath(), options.valueOf(compareCmdOpt).toPath()));
            } else if (spec == diffTreeOpt) {
                if (updateStampTask == null) {
                    changeConfig = extractChangeConfig(changeConfig, ccConfig, options, properties);
                    changeTasks = extractChangeTasks(changeTasks, changeContext, changeConfig, ccConfig);
                    taskQueue.add(priorityCounter++, updateStampCmdStr, updateStampTask = changeTasks.new UpdateStampTask());
                }
                taskQueue.add(priorityCounter++, diffTreeCmdStr, new DiffTreeTask(changeContext, gitConfig, changeConfig));
            } else if (spec == applyCmdOpt) {
                changeConfig = extractChangeConfig(changeConfig, ccConfig, options, properties);
                if (checkoutStampTask == null) {
                    changeTasks = extractChangeTasks(changeTasks, changeContext, changeConfig, ccConfig);
                    taskQueue.add(priorityCounter++, checkoutCmdStr, checkoutStampTask = changeTasks.new CheckoutStampTask());
                    taskQueue.add(priorityCounter++, writeStampCmdStr, changeTasks.new WriteStampTask());
                }
                taskQueue.add(priorityCounter++, applyCmdStr, new ApplyTask(changeContext, ccConfig, changeConfig));
            }
        }
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

    private static GitConfigProperties extractGitConfig(OptionSet options, Properties properties) {
        final GitConfigProperties config = new GitConfigProperties(properties);
        if (options.has(gitExecOpt)) {
            config.setGitExec(gitExecOpt.value(options));
        }
        if (options.has(gitRepositoryDirOpt)) {
            config.setRepositoryDir(gitRepositoryDirOpt.value(options));
        }
        return config;
    }

    private static ClearToolConfigProperties extractClearToolConfig(OptionSet options, Properties properties) {
        final ClearToolConfigProperties ccConfig = new ClearToolConfigProperties(properties);
        if (options.has(ccClearToolExecOpt)) {
            ccConfig.setClearToolExec(ccClearToolExecOpt.value(options));
        }
        if (options.has(ccVobViewDirOpt)) {
            ccConfig.setVobViewDir(ccVobViewDirOpt.value(options));
        }
        return ccConfig;
    }

    private static ChangeConfig extractChangeConfig(ChangeConfig current, ClearToolConfig clearToolConfig, OptionSet options, Properties properties) {
        if (current != null) {
            return current;
        }
        final ChangeConfigProperties changeConfig = new ChangeConfigProperties(clearToolConfig.getVobViewAbsoluteDir().toPath(), properties);
        if (options.has(counterStampFileOpt)) {
            changeConfig.setCommitStampFile(counterStampFileOpt.value(options));
        }
        if (options.has(commitStampFileOpt)) {
            changeConfig.setCommitStampFile(commitStampFileOpt.value(options));
        }
        if (options.has(counterStampOverrideOpt)) {
            changeConfig.setSyncCounterOverride(counterStampOverrideOpt.value(options));
        }
        if (options.has(commitStampOverrideOpt)) {
            changeConfig.setCommitStampOverride(commitStampOverrideOpt.value(options));
        }
        return changeConfig;
    }

    private static ChangeTasks extractChangeTasks(ChangeTasks current, ChangeContext context, ChangeConfig config, ClearToolConfig ctConfig) {
        if (current != null) {
            return current;
        }
        return new ChangeTasks(context, config, ctConfig);
    }

}
