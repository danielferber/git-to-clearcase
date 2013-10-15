/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.cc;

import br.com.danielferber.gittocc.git.*;
import br.com.danielferber.gittocc.process.LineSplittingWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author X7WS
 */
public class ClearToolCommander {

    final GitProcessBuilder pb;

    public ClearToolCommander(GitProcessBuilder pb) {
        this.pb = pb;
    }

    public void fetch() throws IOException {
        pb.reset("fetch remote").command("fetch").progress().verbose().create().waitFor();
    }

    public void fastForward() throws IOException {
        pb.reset("fast forward").command("merge").parameter("ff-only").progress().verbose().create().waitFor();
    }

    public String getCurrentCommit() throws IOException {
        final GitProcess p = pb.reset("current commit").command("rev-parse").argument("HEAD").create();
        Scanner scanner = p.createOutScanner();
        p.start();
        final String commit = scanner.next();
        return commit;
    }

    public String commitMessagesReport(String fromCommit, String toCommit, String format) throws IOException {
        GitProcess p = pb.reset("report").command("log").parameter("reverse").parameter("topo-order").parameter("no-merges")
                .parameter("pretty", "format:" + format).parameter("date", "short").parameter("no-color").argument(fromCommit + ".." + toCommit).create();
        final StringWriter sb = new StringWriter();
        p.addOutWriter(sb);
        p.start();
        p.waitFor();
        return sb.toString();
    }

    public void changeSet(String fromCommit, String toCommit,
            final List<File> filesAdded,
            final List<File> filesDeleted,
            final List<File> filesModified,
            final List<File> filesMovedFrom,
            final List<File> filesMovedTo,
            final List<File> filesCopiedFrom,
            final List<File> filesCopiedTo) throws IOException {
        GitProcess p = pb.reset("change set").noPage().command("diff")
                .parameter("find-copies").parameter("find-renames")
                .parameter("find-copies-harder").parameter("name-status")
                .parameter("no-color").argument(fromCommit + ".." + toCommit).create();
        p.addOutWriter(new LineSplittingWriter() {
            @Override
            protected void processLine(String line) {
                String[] data = line.split("\t");
                char status = data[0].charAt(0);
                String sourcePath;
                String targetPath;
                String percent;
                switch (status) {
                    case 'M':
                        sourcePath = data[1];
                        filesModified.add(new File(sourcePath));
                        break;
                    case 'D':
                        sourcePath = data[1];
                        filesDeleted.add(new File(sourcePath));
                        break;
                    case 'A':
                        sourcePath = data[1];
                        filesAdded.add(new File(sourcePath));
                        break;
                    case 'R':
                        percent = data[0].substring(1);
                        sourcePath = data[1];
                        targetPath = data[2];
                        filesMovedFrom.add(new File(sourcePath));
                        filesMovedTo.add(new File(targetPath));
                        if (!percent.equals("100")) {
                            filesModified.add(new File(targetPath));
                        }
                        break;
                    case 'C':
                        percent = data[0].substring(1);
                        sourcePath = data[1];
                        targetPath = data[2];
                        filesCopiedFrom.add(new File(sourcePath));
                        filesCopiedTo.add(new File(targetPath));
                        if (!percent.equals("100")) {
                            filesModified.add(new File(targetPath));
                        }
                        break;
                }
            }
        });
        p.start();
        p.waitFor();
    }
}
