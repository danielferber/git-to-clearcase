/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.git;

import br.com.danielferber.gittocc2.change.ChangeSet;
import br.com.danielferber.gittocc2.process.CommandLineProcess;
import br.com.danielferber.gittocc2.process.CommandLineProcessBuilder;
import br.com.danielferber.gittocc2.process.LineSplittingWriter;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author X7WS
 */
public class GitCommander {

    final CommandLineProcessBuilder pb;
    final Meter meter = MeterFactory.getMeter("commander.git");

    public GitCommander(final GitConfig config) {
        this.pb = new CommandLineProcessBuilder(config.getRepositoryDir(), config.getGitExec(), LoggerFactory.getLogger("git"));
    }

    private static CommandLineProcess waitForNullExitValue(final CommandLineProcess p) throws GitException.NonNullExitValue {
        final int exitValue = p.waitFor().exitValue();
        if (exitValue != 0) {
            throw new GitException.NonNullExitValue(exitValue);
        }
        return p;
    }

    public static enum ConfigScope {

        Default, Global, System
    }

    public String configGet(final String key, ConfigScope scope) {
        return meter.sub("config.get").ctx("key", key).ctx("scope", scope).safeCall(() -> {
            final CommandLineProcess p = pb.reset("config").command("config").argument(key).create();
            final BufferedReader reader = p.createBufferedOutReader();
            waitForNullExitValue(p);
            return reader.readLine();
        });
    }

    public void configSet(final String key, final String value, ConfigScope scope) {
        meter.sub("config.set").ctx("key", key).ctx("value", value).ctx("scope", scope).run(() -> {
            CommandLineProcess p = pb.reset("config").command("config")
                .parameter(scope == ConfigScope.Global, "global")
                .parameter(scope == ConfigScope.System, "system")
                .argument(key).argument(value).create();
            waitForNullExitValue(p);
        });
    }

    public void configAdd(final String key, final String value, ConfigScope scope) {
        meter.sub("config.add").ctx("key", key).ctx("value", value).ctx("scope", scope).run(() -> {
            CommandLineProcess p = pb.reset("config").command("config")
                .parameter(scope == ConfigScope.Global, "global")
                .parameter(scope == ConfigScope.System, "system")
                .argument(key).argument(value).create();
            waitForNullExitValue(p);
        });
    }

    public void configUnset(final String key, ConfigScope scope) {
        meter.sub("config.unset").ctx("key", key).ctx("scope", scope).run(() -> {
            CommandLineProcess p = pb.reset("config").command("unset")
                .parameter(scope == ConfigScope.Global, "global")
                .parameter(scope == ConfigScope.System, "system")
                .argument(key).create();
            waitForNullExitValue(p);
        });
    }

    public static enum ResetMode {

        Soft, Mixed, Merge, Hard, Keep
    }

    public void reset(ResetMode mode) {
        meter.sub("reset").ctx("mode", mode).run(() -> {
            CommandLineProcess p = pb.reset("reset").command("reset")
                .parameter(mode == ResetMode.Soft, "soft")
                .parameter(mode == ResetMode.Mixed, "mixed")
                .parameter(mode == ResetMode.Merge, "merge")
                .parameter(mode == ResetMode.Hard, "hard")
                .parameter(mode == ResetMode.Keep, "keep")
                .create();
            waitForNullExitValue(p);
        });
    }

    public void clean(boolean directories, boolean ignored) {
        meter.sub("clean").ctx("directories", directories).ctx("ignored", ignored).run(() -> {
            CommandLineProcess p = pb.reset("clean").command("clean")
                .shortParameter(directories, "d")
                .shortParameter(ignored, "x")
                .parameter("force").create();
            waitForNullExitValue(p);
        });
    }

    public void fetch() {
        meter.sub("fetch").run(() -> {
            CommandLineProcess p = pb.reset("fetch").command("fetch").parameter("progress").parameter("verbose").create();
            waitForNullExitValue(p);
        });
    }

    public void fastForward() {
        meter.sub("fastForward").run(() -> {
            CommandLineProcess p = pb.reset("merge.fastForward").command("merge").parameter("ff-only").parameter("progress").parameter("verbose").create();
            waitForNullExitValue(p);
        });
    }

    public String readCommit() {
        return meter.sub("readCommit").safeCall(() -> {
            final CommandLineProcess p = pb.reset("rev-parse").command("rev-parse").argument("HEAD").create();
            final Scanner scanner = p.createOutScanner();
            waitForNullExitValue(p);
            return scanner.next();
        });
    }

    public String commitMessagesReport(final String fromCommit, final String toCommit, final String format) {
        final CommandLineProcess p = pb.reset("commitReport").command("log").parameter("reverse").parameter("topo-order").parameter("no-merges")
            .parameter("pretty", "format:" + format).parameter("date", "short").parameter("no-color").argument(fromCommit + ".." + toCommit).create();
        final StringWriter sb = new StringWriter();
        p.addOutWriter(sb);
        p.start();
        p.waitFor();
        return sb.toString();
    }

    public ChangeSet diffTree(final String fromCommit, final String toCommit) {
        return meter.sub("diffTree").ctx("from", fromCommit).ctx("to", toCommit).safeCall(() -> {
            final CommandLineProcess p = pb.reset("diff-tree").command("diff-tree")
                .parameter("find-copies", "30%").parameter("find-copies-harder")
                .parameter("find-renames", "30%").shortParameter("r")
                .shortParameter("t").parameter("raw").argument(fromCommit).argument(toCommit).create();

            final List<File> dirsAdded = new ArrayList<>();
            final List<File> dirsDeleted = new ArrayList<>();

            final List<File> filesAdded = new ArrayList<>();
            final List<File> filesAddedSource = new ArrayList<>();
            final List<File> filesDeleted = new ArrayList<>();

            final List<File> filesModified = new ArrayList<>();
            final List<File> filesModifiedSource = new ArrayList<>();

            final List<File> filesMovedFrom = new ArrayList<>();
            final List<File> filesMovedTo = new ArrayList<>();
            final List<File> filesMovedModified = new ArrayList<>();
            final List<File> filesMovedSource = new ArrayList<>();

            final List<File> filesCopiedFrom = new ArrayList<>();
            final List<File> filesCopiedTo = new ArrayList<>();
            final List<File> filesCopiedModified = new ArrayList<>();
            final List<File> filesCopiedSource = new ArrayList<>();

            p.addOutWriter(new LineSplittingWriter() {
                @Override
                protected void processLine(final String line) {
                    final String[] data = line.split("\t");
                    final String srcMode = data[0].substring(1, 3);
                    final String dstMode = data[0].substring(8, 10);
                    final boolean isDir = srcMode.equals("04") || dstMode.equals("04");
                    final boolean isFile = srcMode.equals("10") || dstMode.equals("10");
                    final String statusStr = data[0].substring(97);
                    final char status = statusStr.charAt(0);
                    final boolean modified = statusStr.length() > 1 && !statusStr.substring(1).equals("100");
                    final File f1 = new File(data[1]);
                    final File f2 = data.length == 3 ? new File(data[2]) : null;

                    if (isDir) {
                        if (status == 'D') {
                            /* Directory was recustively deleted. */
                            dirsDeleted.add(f1);
                        } else if (status == 'A') {
                            /* Directory was added. */
                            dirsAdded.add(f1);
                        } else if (status == 'R') {
                            /* Renamed directory. It is handled as a deleted and added one. Could be enhanced in future. */
                            dirsDeleted.add(f1);
                            dirsAdded.add(f2);
                        } else if (status == 'C') {
                            /* Copied directory. It is handled as a new one. I am not aware that Clearcase supports copying. */
                            dirsAdded.add(f2);
                        }
                    } else if (isFile) {
                        if (status == 'D') {
                            /* File was deleted. */
                            filesDeleted.add(f1);
                        } else if (status == 'A') {
                            /* File was added. */
                            filesAdded.add(f1);
                            filesAddedSource.add(f1.getAbsoluteFile());
                        } else if (status == 'M') {
                            /* File was modified. */
                            filesModified.add(f1);
                            filesModifiedSource.add(f1.getAbsoluteFile());
                        } else if (status == 'R') {
                            filesMovedFrom.add(f1);
                            filesMovedTo.add(f2);
                            if (modified) {
                                filesMovedModified.add(f2);
                                filesMovedSource.add(f2.getAbsoluteFile());
                            }
                        } else if (status == 'C') {
                            filesCopiedFrom.add(f1);
                            filesCopiedTo.add(f2);
                            if (modified) {
                                filesCopiedModified.add(f2);
                                filesCopiedSource.add(f2.getAbsoluteFile());
                            }
                        }
                    }
                }
            });
            waitForNullExitValue(p);
            return new ChangeSet(dirsAdded, dirsDeleted, filesAdded, filesAddedSource, filesDeleted, filesModified, filesModifiedSource, filesMovedFrom, filesMovedTo, filesMovedModified, filesMovedSource, filesCopiedFrom, filesCopiedTo, filesCopiedModified, filesCopiedSource);
        });
    }
}