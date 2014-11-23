package br.com.danielferber.gittocc2.task.config;

import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigPojo;
import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigSource;
import br.com.danielferber.gittocc2.config.git.GitConfigPojo;
import br.com.danielferber.gittocc2.config.git.GitConfigSource;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

/**
 *
 * @author Daniel Felix Ferber
 */
class CommandLine {

    final static OptionParser parser = new OptionParser();
    final static OptionSpec<Void> helpOpt = parser.accepts("help", "Dislay command line instructions.");
    final static OptionSpec<File> propertyFileOpt = parser.accepts("properties", "Properties file.").withRequiredArg().ofType(File.class);
    final static OptionSpec<File> compareOpt = parser.accepts("compare", "Compare file by file and ignore git history (slower but safer).").withRequiredArg().ofType(File.class);
    final static OptionSpec<File> gitExecOpt = parser.accepts("git", "Git executable file.").requiredUnless(propertyFileOpt, helpOpt).withRequiredArg().ofType(File.class);
    final static OptionSpec<File> gitRepositoryDirOpt = parser.accepts("repo", "Git repository directory.").requiredUnless(propertyFileOpt, helpOpt).withRequiredArg().ofType(File.class);
    final static OptionSpec<Void> gitFastForwardLocalGitRepositoryOpt = parser.accepts("forward", "Before synchronizing, fast forward logal git repository.");
    final static OptionSpec<Void> gitFetchRemoteGitRepositoryOpt = parser.accepts("fetch", "Before synchronizing, fetch remote commits from default remote git repository.");
    final static OptionSpec<Void> gitResetLocalGitRepositoryOpt = parser.accepts("reset", "Before synchronizing, reset (hard) local git repository.");
    final static OptionSpec<Void> gitCleanLocalGitRepositoryOpt = parser.accepts("clean", "Before synchronizing, clean completely local git repository.");
    final static OptionSpec<Void> gitApplyDefaultGitConfigOpt = parser.accepts("configure", "Before synchronizing, apply default git configuration to repository.");
    final static OptionSpec<File> ccClearToolExecOpt = parser.accepts("ct", "CleartTool executable file.").requiredUnless(propertyFileOpt, helpOpt).withRequiredArg().ofType(File.class);
    final static OptionSpec<File> ccVobViewDirOpt = parser.accepts("view", "Snapshot vob view directory.").requiredUnless(propertyFileOpt, helpOpt).withRequiredArg().ofType(File.class);
    final static OptionSpec<Void> ccVobRootUpdateOpt = parser.accepts("update", "Before synchronizing, update ClearCase VOB view directory.");
    final static OptionSpec<String> ccActivityOpt = parser.accepts("activity", "Create or resuse ClearCase activity for all synchronized files.").withRequiredArg().ofType(String.class);
    final static OptionSpec<File> ccCommitStampFileOpt = parser.accepts("commitstamp", "Last synchronization commit stamp file relative to vob directory.").withOptionalArg().ofType(File.class);
    final static OptionSpec<File> ccCounterStampFileOpt = parser.accepts("counterstamp", "Synchronization counter stamp file relative to vob directory.").withOptionalArg().ofType(File.class);
    final static OptionSpec<Long> ccOverriddenSyncCounterOpt = parser.accepts("counter", "Assume given counter and ignore counter stamp file.").withRequiredArg().ofType(Long.class);
    final static OptionSpec<String> ccOverriddenSyncFromCommitOpt = parser.accepts("commit", "Assume given commit and ignore commit stamp file.").withRequiredArg().ofType(String.class);

    public static void printHelp(final PrintStream ps) {
        try {
            parser.printHelpOn(ps);
        } catch (IOException ex) { 
            Logger.getLogger(CommandLine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    final OptionSet options;

    public CommandLine(final String[] argv) {
        options = parser.parse(argv);
    }

    public boolean useHelpMessage() {
        return options.has(helpOpt);
    }

    public boolean usePropertiesFile() {
        return options.has(propertyFileOpt);
    }

    public File getPropertiesFile() {
        if (useHelpMessage()) {
            return null;
        }
        return propertyFileOpt.value(options);
    }

    public GitConfigSource getGitConfig() {
        if (useHelpMessage()) {
            return null;
        }
        final GitConfigPojo config = new GitConfigPojo(gitExecOpt.value(options), gitRepositoryDirOpt.value(options));
        if (options.has(gitFastForwardLocalGitRepositoryOpt)) {
            config.setFastForwardLocalGitRepository(true);
        }
        if (options.has(gitCleanLocalGitRepositoryOpt)) {
            config.setCleanLocalGitRepository(true);
        }
        if (options.has(gitResetLocalGitRepositoryOpt)) {
            config.setResetLocalGitRepository(true);
        }
        if (options.has(gitFastForwardLocalGitRepositoryOpt)) {
            config.setFastForwardLocalGitRepository(true);
        }
        if (options.has(gitFetchRemoteGitRepositoryOpt)) {
            config.setFetchRemoteGitRepository(true);
        }
        if (options.has(gitApplyDefaultGitConfigOpt)) {
            config.setApplyDefaultGitConfig(true);
        }
        return config;
    }

    public ClearToolConfigSource getClearToolConfig() {
        if (useHelpMessage()) {
            return null;
        }
        final ClearToolConfigPojo config = new ClearToolConfigPojo(ccClearToolExecOpt.value(options), ccVobViewDirOpt.value(options));
        if (options.has(ccVobRootUpdateOpt)) {
            config.setUpdateVobRoot(true);
        }
        if (options.has(ccActivityOpt)) {
            config.setUseActivity(true);
            config.setActivityName(options.valueOf(ccActivityOpt));
        }
        if (options.has(ccCommitStampFileOpt)) {
            config.setUseCommitStampFile(true);
            if (options.hasArgument(ccCommitStampFileOpt)) {
                config.setCommitStampFile(options.valueOf(ccCommitStampFileOpt));
            }
        }
        if (options.has(ccCounterStampFileOpt)) {
            config.setUseCounterStampFile(true);
            if (options.hasArgument(ccCounterStampFileOpt)) {
                config.setCounterStampFile(options.valueOf(ccCounterStampFileOpt));
            }
        }
        if (options.has(ccOverriddenSyncCounterOpt)) {
            config.setOverriddenSyncCounter(options.valueOf(ccOverriddenSyncCounterOpt));
        }
        if (options.has(ccOverriddenSyncFromCommitOpt)) {
            config.setOverriddenSyncFromCommit(options.valueOf(ccOverriddenSyncFromCommitOpt));
        }
        return config;
    }

    public boolean isCompareOnly() {
        return options.has(compareOpt);
    }

    public File getCompareRoot() {
        return options.valueOf(compareOpt);
    }

}
