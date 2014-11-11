/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.com.danielferber.gittocc2.config.git.GitConfigSource;
import br.com.danielferber.gittocc2.process.CommandLineProcess;
import br.com.danielferber.gittocc2.process.CommandLineProcessBuilder;
import br.com.danielferber.gittocc2.process.LineSplittingWriter;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;

/**
 *
 * @author X7WS
 */
class GitCommander {

    final CommandLineProcessBuilder pb;
    final Meter meter = MeterFactory.getMeter("GitCommander");

    public GitCommander(final GitConfigSource config) {
        this.pb = new CommandLineProcessBuilder(config.getRepositoryDir(), config.getGitExec(), LoggerFactory.getLogger("git"));
    }

    public String getConfig(final String key) {
        final Meter m = meter.sub("getConfig").ctx("key", key).start();
        final CommandLineProcess p = pb.reset("config").command("config").argument(key).create();
        final BufferedReader reader = new BufferedReader(p.createOutReader());
        p.waitFor();
        String value = null;
        try {
            value = reader.readLine();
            m.ctx("value", value).ok();
        } catch (final IOException ex) {
            m.fail(ex);
        }
        return value;
    }

    public void setConfig(final String key, final String value) {
        final Meter m = meter.sub("setConfig").ctx("key", key).ctx("value", value).start();
        pb.reset("config").command("config").argument(key).argument(value).create().waitFor();
        m.ok();
    }

    public void resetLocal() {
        final Meter m = meter.sub("resetLocal").start();
        pb.reset("reset").command("reset").parameter("hard").create().waitFor();
        m.ok();
    }

    public void cleanLocal() {
        final Meter m = meter.sub("cleanLocal").start();
        pb.reset("clean").command("clean").shortParameter("d").shortParameter("x").parameter("force").create().waitFor();
        m.ok();
    }

    public void fetchRemote() {
        final Meter m = meter.sub("fetchRemote").start();
        pb.reset("fetch").command("fetch").parameter("progress").parameter("verbose").create().waitFor();
        m.ok();
    }

    public void fastForward() {
        final Meter m = meter.sub("fastForward").start();
        final CommandLineProcess p = pb.reset("fastForward").command("merge").parameter("ff-only").parameter("progress").parameter("verbose").create();
        p.waitFor();
        final int exitValue = p.exitValue();
        m.ok();
    }

    public String currentCommit() {
        final Meter m = meter.sub("currentCommit").start();
        final CommandLineProcess p = pb.reset("rev-parse").command("rev-parse").argument("HEAD").create();
        final Scanner scanner = p.createOutScanner();
        p.start();
        final String commit = scanner.next();
        m.ctx("commit", commit).ok();
        return commit;
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

    public TreeDiff treeDif(final String fromCommit, final String toCommit) {
        final CommandLineProcess p = pb.reset("treeDiff").command("diff-tree")
                .parameter("find-copies", "30%").parameter("find-copies-harder")
                .parameter("find-renames", "30%").shortParameter("r")
                .shortParameter("t").parameter("raw").argument(fromCommit).argument(toCommit).create();

        final List<File> dirsAdded = new ArrayList<>();
        final List<File> dirsDeleted = new ArrayList<>();

        final List<File> filesAdded = new ArrayList<>();
        final List<File> filesDeleted = new ArrayList<>();
        final List<File> filesModified = new ArrayList<>();

        final List<File> filesMovedFrom = new ArrayList<>();
        final List<File> filesMovedTo = new ArrayList<>();
        final List<File> filesMovedModified = new ArrayList<>();

        final List<File> filesCopiedFrom = new ArrayList<>();
        final List<File> filesCopiedTo = new ArrayList<>();
        final List<File> filesCopiedModified = new ArrayList<>();

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
                        dirsDeleted.add(f1);
                    } else if (status == 'A') {
                        dirsAdded.add(f1);
                    }
                } else if (isFile) {
                    if (status == 'D') {
                        filesDeleted.add(f1);
                    } else if (status == 'A') {
                        filesAdded.add(f1);
                    } else if (status == 'M') {
                        filesModified.add(f1);
                    } else if (status == 'R') {
                        filesMovedFrom.add(f1);
                        filesMovedTo.add(f2);
                        if (modified) {
                            filesMovedModified.add(f2);
                        }
                    } else if (status == 'C') {
                        filesCopiedFrom.add(f1);
                        filesCopiedTo.add(f2);
                        if (modified) {
                            filesCopiedModified.add(f2);
                        }
                    }
                }
            }
        });
        p.start();
        p.waitFor();

        return new TreeDiff(dirsAdded, dirsDeleted, filesAdded, filesDeleted, filesModified, filesMovedFrom, filesMovedTo, filesMovedModified, filesCopiedFrom, filesCopiedTo, filesCopiedModified);
    }
}
