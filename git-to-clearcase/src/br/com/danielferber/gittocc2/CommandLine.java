package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.PropertiesConfigSource;
import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigChain;
import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigPojo;
import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigProperties;
import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigSource;
import br.com.danielferber.gittocc2.config.git.GitConfigChain;
import br.com.danielferber.gittocc2.config.git.GitConfigPojo;
import br.com.danielferber.gittocc2.config.git.GitConfigProperties;
import br.com.danielferber.gittocc2.config.git.GitConfigSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import joptsimple.ValueConversionException;

/**
 *
 * @author Daniel Felix Ferber
 */
class CommandLine {

    final OptionParser parser = new OptionParser();
    final OptionSpec<Void> helpOpt = parser.accepts("help", "Dislay command line instructions.");
    final OptionSpec<File> propertyFileOpt = parser.accepts("properties", "Properties file.").withRequiredArg().ofType(File.class);
    final OptionSpec<File> compareOpt = parser.accepts("compare", "Compare file by file and ignore git history (slower but safer).").withRequiredArg().ofType(File.class);
    final OptionSpec<File> gitExecOpt = parser.accepts("git", "Git executable file.").requiredUnless(propertyFileOpt, helpOpt).withRequiredArg().ofType(File.class);
    final OptionSpec<File> gitRepositoryDirOpt = parser.accepts("repo", "Git repository directory.").requiredUnless(propertyFileOpt, helpOpt).withRequiredArg().ofType(File.class);
    final OptionSpec<Void> gitFastForwardLocalGitRepositoryOpt = parser.accepts("forward", "Before synchronizing, fast forward logal git repository.");
    final OptionSpec<Void> gitFetchRemoteGitRepositoryOpt = parser.accepts("fetch", "Before synchronizing, fetch remote commits from default remote git repository.");
    final OptionSpec<Void> gitResetLocalGitRepositoryOpt = parser.accepts("reset", "Before synchronizing, reset (hard) local git repository.");
    final OptionSpec<Void> gitCleanLocalGitRepositoryOpt = parser.accepts("clean", "Before synchronizing, clean completely local git repository.");
    final OptionSpec<Void> gitApplyDefaultGitConfigOpt = parser.accepts("configure", "Before synchronizing, apply default git configuration to repository.");
    final OptionSpec<File> ccClearToolExecOpt = parser.accepts("ct", "CleartTool executable file.").requiredUnless(propertyFileOpt, helpOpt).withRequiredArg().ofType(File.class);
    final OptionSpec<File> ccVobViewDirOpt = parser.accepts("view", "Snapshot vob view directory.").requiredUnless(propertyFileOpt, helpOpt).withRequiredArg().ofType(File.class);
    final OptionSpec<Void> ccVobRootUpdateOpt = parser.accepts("update", "Before synchronizing, update ClearCase VOB view directory.");
    final OptionSpec<String> ccActivityOpt = parser.accepts("activity", "Create or resuse ClearCase activity for all synchronized files.").withRequiredArg().ofType(String.class);
    final OptionSpec<File> ccCommitStampFileOpt = parser.accepts("commitstamp", "Last synchronization commit stamp file relative to vob directory.").withOptionalArg().ofType(File.class);
    final OptionSpec<File> ccCounterStampFileOpt = parser.accepts("counterstamp", "Synchronization counter stamp file relative to vob directory.").withOptionalArg().ofType(File.class);
    final OptionSpec<Long> ccOverriddenSyncCounterOpt = parser.accepts("counter", "Assume given counter and ignore counter stamp file.").withRequiredArg().ofType(Long.class);
    final OptionSpec<String> ccOverriddenSyncFromCommitOpt = parser.accepts("commit", "Assume given commit and ignore commit stamp file.").withRequiredArg().ofType(String.class);

    public void printHelp(final PrintStream ps) throws IOException {
        parser.printHelpOn(ps);
    }

    public Properties parse(final String[] argv) {
        final OptionSet options = parser.parse(argv);
        final Properties properties = new Properties();
        final PropertiesConfigSource config = new PropertiesConfigSource(properties);

        final File propertyFile = propertyFileOpt.value(options);

        if (propertyFile != null) {
            try (InputStream is = new FileInputStream(propertyFile)) {
                properties = new Properties();
                properties.load(is);
                is.close();
            } catch (final FileNotFoundException e) {
                throw new ValueConversionException("Properties file: failed to open.", e);
            } catch (final IOException e) {
                throw new ValueConversionException("Properties file: failed to read.", e);
            }
        } else {
            properties = null;
        }
    }

    boolean isCompareOnly() {
        return options.has(compareOpt);
    }

    File getCompareRoot() {
        return options.valueOf(compareOpt);
    }

    ClearToolConfigSource getClearToolConfig() {
        if (options.has(helpOpt)) {
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

        if (properties == null) {
            return new ClearToolConfigChain(config, clearToolConfigDefault);
        }
        return new ClearToolConfigChain(config, new ClearToolConfigProperties(properties), clearToolConfigDefault);
    }

    GitConfigSource getGitConfig() {
        if (options.has(helpOpt)) {
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

        if (properties == null) {
            return new GitConfigChain(config, gitConfigDefault);
        }
        return new GitConfigChain(config, new GitConfigProperties(properties), gitConfigDefault);
    }
}
