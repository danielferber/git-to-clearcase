package br.com.danielferber.gittocc2.config;

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
public class ConfigFactory {

    private static final OptionParser parser = new OptionParser();
    private static final OptionSpec<File> propertyFileOpt = parser.accepts("properties", "Properties file.").withRequiredArg().ofType(File.class);
    private static final OptionSpec<Void> helpOpt = parser.accepts("help", "Dislay command line instructions.");
    private static final OptionSpec<String> ccActivityNamePatternOpt = parser.accepts("activity", "Before synchronizing, create ClearCase activity using given pattern.").withRequiredArg().ofType(String.class);
    private static final OptionSpec<Boolean> ccValidateExistingCheckoutsOpt = parser.accepts("checkouts", "after synchronizing, validate existing checkouts.").withOptionalArg().ofType(Boolean.class).defaultsTo(Boolean.TRUE);
    private static final OptionSpec<Boolean> ccUpdateVobViewDirOpt = parser.accepts("update", "Before synchronizing, update ClearCase VOB view directory.").withOptionalArg().ofType(Boolean.class).defaultsTo(Boolean.TRUE);
    private static final OptionSpec<File> ccVobViewDirOpt = parser.accepts("view", "VOB view directory.").requiredUnless(propertyFileOpt, helpOpt).withRequiredArg().ofType(File.class);
    private static final OptionSpec<File> ccCommitStampFileOpt = parser.accepts("commitfile", "Last synchronization commit stamp file relative to vob directory.").withRequiredArg().ofType(File.class);
    private static final OptionSpec<File> ccCounterStampFileOpt = parser.accepts("counterfile", "Synchronization counter stamp file relative to vob directory.").withRequiredArg().ofType(File.class);
    private static final OptionSpec<Void> ccNoUpdateCommitStampFileOpt = parser.accepts("nocommit", "After synchronizing, do not update commit stamp file.");
    private static final OptionSpec<Void> ccNoUpdateCounterStampFileOpt = parser.accepts("nocounter", "After synchronizing, do not update counter stamp file.");
    private static final OptionSpec<File> ccClearToolExecOpt = parser.accepts("ct", "CleartTool executable file.").requiredUnless(propertyFileOpt, helpOpt).withRequiredArg().ofType(File.class);

    private static final OptionSpec<File> gitExecOpt = parser.accepts("git", "Git executable file.").requiredUnless(propertyFileOpt, helpOpt).withRequiredArg().ofType(File.class);
    private static final OptionSpec<File> gitRepositoryDirOpt = parser.accepts("repo", "Git repository directory.").requiredUnless(propertyFileOpt, helpOpt).withRequiredArg().ofType(File.class);
    private static final OptionSpec<Boolean> gitFastForwardLocalGitRepositoryOpt = parser.accepts("forward", "Before synchronizing, fast forward logal git repository.").withOptionalArg().ofType(Boolean.class).defaultsTo(Boolean.TRUE);
    private static final OptionSpec<Boolean> gitFetchRemoteGitRepositoryOpt = parser.accepts("fetch", "Before synchronizing, fetch remote commits from default remote git repository.").withOptionalArg().ofType(Boolean.class).defaultsTo(Boolean.TRUE);
    private static final OptionSpec<Boolean> gitResetLocalGitRepositoryOpt = parser.accepts("reset", "Before synchronizing, reset (hard) local git repository.").withOptionalArg().ofType(Boolean.class).defaultsTo(Boolean.TRUE);
    private static final OptionSpec<Boolean> gitCleanLocalGitRepositoryOpt = parser.accepts("clean", "Before synchronizing, clean completely local git repository.").withOptionalArg().ofType(Boolean.class).defaultsTo(Boolean.TRUE);
    private static final OptionSpec<Boolean> gitApplyDefaultGitConfigOpt = parser.accepts("configure", "Before synchronizing, apply default git configuration to repository.").withOptionalArg().ofType(Boolean.class).defaultsTo(Boolean.TRUE);

    private static final OptionSpec<File> compareOpt = parser.accepts("compare", "Compare file by file and ignore git history (slower but safer).").withRequiredArg().ofType(File.class);
    private static final OptionSpec<Long> syncOverriddenCounterOpt = parser.accepts("counter", "Assume given counter and ignore counter stamp file.").withRequiredArg().ofType(Long.class);
    private static final OptionSpec<String> syncOverriddenCommitOpt = parser.accepts("commit", "Assume given commit and ignore commit stamp file.").withRequiredArg().ofType(String.class);

    public static void printHelp(PrintStream ps) throws IOException {
        parser.printHelpOn(ps);
    }

    public static ConfigContainer parse(final String[] argv) {

        final OptionSet options = parser.parse(argv);
        if (options.has(helpOpt)) {
            throw new PrintHelpException();
        }

        Properties properties = createDefaultProperties();

        /* Consider properties file. */
        final File propertyFile = propertyFileOpt.value(options);
        if (propertyFile != null) {
            try (InputStream is = new FileInputStream(propertyFile)) {
                properties = new Properties(properties);
                properties.load(is);
                is.close();
            } catch (final FileNotFoundException e) {
                throw new ValueConversionException("Properties file: failed to open.", e);
            } catch (final IOException e) {
                throw new ValueConversionException("Properties file: failed to read.", e);
            }
        }

        /* Conside command line arguments. */
        properties = new Properties(properties);
        final ConfigContainer config = new ConfigContainer(properties);

        if (options.has(ccActivityNamePatternOpt)) {
            config.getClearCaseActivityBean().setCreateActivity(true);
            config.getClearCaseActivityBean().setActivityNamePattern(options.valueOf(ccActivityNamePatternOpt));
        }

        if (options.has(ccValidateExistingCheckoutsOpt)) {
            config.getClearCaseFinalizeBean().setValidateExistingCheckout(Boolean.TRUE);
        }

        if (options.has(ccUpdateVobViewDirOpt)) {
            config.getClearCasePrepareBean().setUpdateVobViewDir(true);
        }

        if (options.hasArgument(ccCommitStampFileOpt)) {
            config.getClearCaseStampFileBean().setCommitStampFile(options.valueOf(ccCommitStampFileOpt));
        }
        if (options.hasArgument(ccCounterStampFileOpt)) {
            config.getClearCaseStampFileBean().setCounterStampFile(options.valueOf(ccCounterStampFileOpt));
        }
        if (options.hasArgument(ccVobViewDirOpt)) {
            config.getClearToolBean().setVobViewDir(options.valueOf(ccVobViewDirOpt));
        }
        if (options.hasArgument(ccVobViewDirOpt)) {
            config.getClearToolBean().setVobViewDir(options.valueOf(ccVobViewDirOpt));
        }
        if (options.hasArgument(ccNoUpdateCommitStampFileOpt)) {
            config.getClearCaseStampFileBean().setUpdateCommitStampFile(Boolean.FALSE);
        }
        if (options.hasArgument(ccNoUpdateCounterStampFileOpt)) {
            config.getClearCaseStampFileBean().setUpdateCounterStampFile(Boolean.FALSE);
        }

        if (options.hasArgument(ccClearToolExecOpt)) {
            config.getClearToolBean().setClearToolExec(options.valueOf(ccClearToolExecOpt));
        }

        if (options.has(gitCleanLocalGitRepositoryOpt)) {
            config.getGitPrepareBean().setCleanLocalGitRepository(options.valueOf(gitCleanLocalGitRepositoryOpt));
        }
        if (options.has(gitResetLocalGitRepositoryOpt)) {
            config.getGitPrepareBean().setResetLocalGitRepository(options.valueOf(gitResetLocalGitRepositoryOpt));
        }
        if (options.has(gitFastForwardLocalGitRepositoryOpt)) {
            config.getGitPrepareBean().setFastForwardLocalGitRepository(options.valueOf(gitFastForwardLocalGitRepositoryOpt));
        }
        if (options.has(gitFetchRemoteGitRepositoryOpt)) {
            config.getGitPrepareBean().setFetchRemoteGitRepository(options.valueOf(gitFetchRemoteGitRepositoryOpt));
        }
        if (options.has(gitApplyDefaultGitConfigOpt)) {
            config.getGitPrepareBean().setApplyDefaultGitConfig(options.valueOf(gitApplyDefaultGitConfigOpt));
        }

        if (options.hasArgument(gitExecOpt)) {
            config.getGitBean().setGitExec(options.valueOf(gitExecOpt));
        }

        if (options.hasArgument(gitRepositoryDirOpt)) {
            config.getGitBean().setRepositoryDir(options.valueOf(gitRepositoryDirOpt));
        }

        if (options.hasArgument(syncOverriddenCounterOpt)) {
            config.getClearCaseStampFileBean().setOverriddenCounterStamp(options.valueOf(syncOverriddenCounterOpt));
            config.getClearCaseStampFileBean().setUseOverriddenCounterStamp(Boolean.TRUE);
        } else {
            config.getClearCaseStampFileBean().setUseOverriddenCounterStamp(Boolean.FALSE);
        }
        if (options.hasArgument(syncOverriddenCommitOpt)) {
            config.getClearCaseStampFileBean().setOverriddenCommitStamp(options.valueOf(syncOverriddenCommitOpt));
            config.getClearCaseStampFileBean().setUseOverriddenCommitStamp(Boolean.TRUE);
        } else {
            config.getClearCaseStampFileBean().setUseOverriddenCommitStamp(Boolean.FALSE);
        }

        if (options.hasArgument(compareOpt)) {
            ComparisonConfigContainter containter = new ComparisonConfigContainter(properties);
            containter.getComparisonBean().setCompareRoot(options.valueOf(compareOpt));
            return containter;
        }

        SynchronizationConfigContainer containter = new SynchronizationConfigContainer(properties);

        return containter;
    }

    private static Properties createDefaultProperties() {
        Properties properties = new Properties();
        ConfigContainer config = new ConfigContainer(properties);
        config.getGitPrepareBean().setApplyDefaultGitConfig(Boolean.FALSE);
        config.getGitPrepareBean().setCleanLocalGitRepository(Boolean.FALSE);
        config.getGitPrepareBean().setFastForwardLocalGitRepository(Boolean.FALSE);
        config.getGitPrepareBean().setFetchRemoteGitRepository(Boolean.FALSE);
        config.getGitPrepareBean().setResetLocalGitRepository(Boolean.FALSE);
        config.getClearCaseStampFileBean().setCommitStampFile(new File("sync-commit-stamp.txt"));
        config.getClearCaseStampFileBean().setCounterStampFile(new File("sync-counter-stamp.txt"));
        config.getClearCaseActivityBean().setCreateActivity(Boolean.FALSE);
        config.getClearCasePrepareBean().setUpdateVobViewDir(Boolean.FALSE);
        config.getClearCaseFinalizeBean().setValidateExistingCheckout(Boolean.FALSE);
        return properties;
    }

}
