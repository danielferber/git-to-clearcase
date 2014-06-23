package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfig;
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
class SynchronizerCommandLine {

    final static OptionParser parser = new OptionParser();
    final static OptionSpec<File> propertyFileOpt = parser.accepts("properties", "Properties file.").withRequiredArg().ofType(File.class);
    final static OptionSpec<Void> compareOpt = parser.accepts("compare", "Compare file by file and ignore git history (slower but safer).");

    final static OptionSpec<File> gitExecOpt = parser.accepts("git", "Git executable file.").withRequiredArg().required().ofType(File.class);
    final static OptionSpec<File> gitRepositoryDirOpt = parser.accepts("repo", "Git repository directory.").withRequiredArg().required().ofType(File.class);
    final static OptionSpec<Void> gitFastForwardLocalGitRepositoryOpt = parser.accepts("forward", "Before synchronizing, fast forward logal git repository.");
    final static OptionSpec<Void> gitFetchRemoteGitRepositoryOpt = parser.accepts("fetch", "Before synchronizing, fetch remote commits from default remote git repository.");
    final static OptionSpec<Void> gitResetLocalGitRepositoryOpt = parser.accepts("reset", "Before synchronizing, reset (hard) local git repository.");
    final static OptionSpec<Void> gitCleanLocalGitRepositoryOpt = parser.accepts("clean", "Before synchronizing, clean completely local git repository.");

    final static OptionSpec<File> ccClearToolExecOpt = parser.accepts("ct", "CleartTool executable file.").withRequiredArg().required().ofType(File.class);
    final static OptionSpec<File> ccVobViewDirOpt = parser.accepts("view", "Snapshot vob view directory.").withRequiredArg().required().ofType(File.class);
    final static OptionSpec<File> ccCommitStampFileOpt = parser.accepts("commitstamp", "ClearCase sync stamp file relative to vob root directory.").withRequiredArg().ofType(File.class);
    final static OptionSpec<File> ccCounterStampFileOpt = parser.accepts("counterstamp", "ClearCase counter stamp file relative to vob root directory.").withRequiredArg().ofType(File.class);
    final static OptionSpec<Void> ccCreateActivityOpt = parser.accepts("activity", "Before synchronizing, create ClearCase activity.");
    final static OptionSpec<String> ccActivityMessagePatternOpt = parser.accepts("message", "ClearCase activity message pattern.").requiredIf(ccCreateActivityOpt).withRequiredArg().ofType(String.class);
    final static OptionSpec<Long> ccOverriddenSyncCounterOpt = parser.accepts("counter", "Assume given counter and ignore counter stamp file.").withRequiredArg().ofType(Long.class);
    final static OptionSpec<String> ccOverriddenSyncFromCommitOpt = parser.accepts("commit", "Assume given commit and ignore commit stamp file.").withRequiredArg().ofType(String.class);
    final static OptionSpec<Void> ccUpdateVobRootOpt = parser.accepts("update", "Before synchronizing, update ClearCase VOB view directory.");

    static void printHelp(PrintStream ps) throws IOException {
        parser.printHelpOn(ps);
    }

    final OptionSet options;
    final Properties properties;

    SynchronizerCommandLine(String[] argv) {
        options = parser.parse(argv);
        File propertyFile = propertyFileOpt.value(options);

        if (propertyFile != null) {
            try (InputStream is = new FileInputStream(propertyFile)) {
                properties = new Properties();
                properties.load(is);
                is.close();
            } catch (FileNotFoundException e) {
                throw new ValueConversionException("Properties file: failed to open.", e);
            } catch (IOException e) {
                throw new ValueConversionException("Properties file: failed to read.", e);
            }
        } else {
            properties = null;
        }
    }
    
    boolean isCompareOnly() {
        return options.has(compareOpt);
    }

    ClearToolConfigSource getClearToolConfig() {
        final ClearToolConfigPojo config = new ClearToolConfigPojo(ccClearToolExecOpt.value(options), ccVobViewDirOpt.value(options));
        if (options.has(ccActivityMessagePatternOpt)) {
            config.setActivityMessagePattern(options.valueOf(ccActivityMessagePatternOpt));
        }
        if (options.has(ccCommitStampFileOpt)) {
            config.setCommitStampFile(options.valueOf(ccCommitStampFileOpt));
        } else {
            config.setCommitStampFile(new File("sync-commit-stamp.txt"));
        }
        if (options.has(ccCounterStampFileOpt)) {
            config.setCounterStampFile(options.valueOf(ccCounterStampFileOpt));
        } else {
            config.setCounterStampFile(new File("sync-counter-stamp.txt"));
        }
        config.setCreateActivity(options.has(ccCreateActivityOpt));
        if (options.has(ccOverriddenSyncCounterOpt)) {
            config.setOverriddenSyncCounter(options.valueOf(ccOverriddenSyncCounterOpt));
        }
        if (options.has(ccOverriddenSyncFromCommitOpt)) {
            config.setOverriddenSyncFromCommit(options.valueOf(ccOverriddenSyncFromCommitOpt));
        }
        config.setUpdateVobRoot(options.has(ccUpdateVobRootOpt));

        if (properties == null) {
            return config;
        }
        return new ClearToolConfigChain(config, new ClearToolConfigProperties(properties));
    }

    GitConfigSource getGitConfig() {
        final GitConfigPojo config = new GitConfigPojo(gitExecOpt.value(options), gitRepositoryDirOpt.value(options));
        config.setFastForwardLocalGitRepository(options.has(gitFastForwardLocalGitRepositoryOpt));
        config.setCleanLocalGitRepository(options.has(gitCleanLocalGitRepositoryOpt));
        config.setResetLocalGitRepository(options.has(gitResetLocalGitRepositoryOpt));
        config.setFastForwardLocalGitRepository(options.has(gitFastForwardLocalGitRepositoryOpt));
        config.setFetchRemoteGitRepository(options.has(gitFetchRemoteGitRepositoryOpt));

        if (properties == null) {
            return config;
        }
        return new GitConfigChain(config, new GitConfigProperties(properties));
    }

}
