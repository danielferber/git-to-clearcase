/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.cc;

import br.com.danielferber.gittocc.process.LineSplittingWriter;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author X7WS
 */
public class ClearToolCommander {

    final ClearToolProcessBuilder pb;
    final Set<File> filesCheckedOut = new TreeSet<File>();
    final Set<File> dirsCheckedOut = new TreeSet<File>();

    public ClearToolCommander(ClearToolProcessBuilder pb) {
        this.pb = pb;
    }

    public void createActivity(String headline) throws IOException {
        pb.reset("mkactivity").command("mkactivity").arguments("-headline", headline, "-force").create().waitFor();
    }

    public void checkoutDir(File dir) throws IOException {
        checkoutDirs(Collections.singleton(dir));
    }

    public void checkoutFile(File file) throws IOException {
        checkoutFiles(Collections.singleton(file));
    }

    public void checkoutDirs(Collection<File> dirs) throws IOException {
        for (File dir : dirs) {
            if (! dirsCheckedOut.contains(dir)) {
                pb.reset("checkoutDir").command("checkout").preserveTime().noComment()
                        .argument(dir.getPath()).create().waitFor();
                dirsCheckedOut.add(dir);   
            }
        }
    }

    public void checkoutFiles(Collection<File> files) throws IOException {
        for (File file : files) {
            if (! filesCheckedOut.contains(file)) {
                pb.reset("checkoutFile").command("checkout").preserveTime().noComment()
                        .argument(file.getPath()).create().waitFor();
                filesCheckedOut.add(file);
            }
        }
    }

    public void checkinDirs(Collection<File> dirs) throws IOException {
        for (File dir : dirs) {
            if (dirsCheckedOut.contains(dir)) {
                pb.reset("checkinDir").command("checkin").preserveTime().noComment()
                        .argument(dir.getPath()).create().waitFor();
                dirsCheckedOut.remove(dir);
            }
        }
    }

    public void checkinFiles(Collection<File> files) throws IOException {
        for (File file : files) {
            if (filesCheckedOut.contains(file)) {
                pb.reset("checkinFile").command("checkin").preserveTime().noComment()
                        .argument(file.getPath()).create().waitFor();
                filesCheckedOut.remove(file);
            }
        }
    }

    public void checkinAll() throws IOException {
        checkinFiles(new TreeSet(filesCheckedOut));
        checkinDirs(new TreeSet(dirsCheckedOut));
    }

    public void checkinDirs() throws IOException {
        checkinDirs(new TreeSet(dirsCheckedOut));
    }

    public void checkinFiles() throws IOException {
        checkinFiles(new TreeSet(filesCheckedOut));
    }

    public void removeFiles(Collection<File> files) throws IOException {
        for (File file : files) {
            pb.reset("rmnameFile").command("rmname").force().noComment().argument(file.getPath()).create().waitFor();
        }
    }

    public void removeDirs(Collection<File> dirs) throws IOException {
        for (File dir : dirs) {
            pb.reset("rmnameDir").command("rmname").force().noComment().argument(dir.getPath()).create().waitFor();
        }
    }

    public void moveFile(File source, File target) throws IOException {
        pb.reset("moveFile").command("mv").noComment().argument(source.getPath()).argument(target.getPath()).create().waitFor();
    }

    private void safeMakeElements(Collection<File> dirs, Collection<File> files) throws IOException {
        final TreeSet<File> dirsToCheckout = new TreeSet<File>();
        final TreeSet<File> filesToCheckout = new TreeSet<File>();
        final TreeSet<File> dirsToMake = new TreeSet<File>();
        final TreeSet<File> filesToMake = new TreeSet<File>();
        if (files != null) {
            filesToMake.addAll(files);
        }
        if (dirs != null) {
            dirsToMake.addAll(dirs);
        }

        outer:
        while (!dirsToCheckout.isEmpty() || !filesToCheckout.isEmpty() || !dirsToMake.isEmpty() || !filesToMake.isEmpty()) {
            checkoutDirs(dirsToCheckout);
            checkoutFiles(filesToCheckout);

            while (dirsToCheckout.isEmpty() && !dirsToMake.isEmpty()) {
                final File dirToMake = dirsToMake.pollFirst();
                pb.reset("makeDir").command("mkdir").noComment().argument(dirToMake.getPath()).create()
                        .addOutWriter(new LineSplittingWriter() {
                    @Override
                    protected void processLine(String line) {
                        Matcher checkoutMatcher = mkdirCheckoutPattern.matcher(line);
                        if (checkoutMatcher.find()) {
                            File dir = new File(checkoutMatcher.group(1));
                            dirsCheckedOut.add(dir);
                        }
                    }
                })
                        .addErrWriter(new LineSplittingWriter() {
                    @Override
                    protected void processLine(String line) {
                        Matcher matcher = mkdirNeedCheckoutPattern.matcher(line);
                        if (matcher.find()) {
                            /* Parent directory should have been checked out first. */
                            File dir = new File(matcher.group(1));
                            dirsToCheckout.add(dir);
                            dirsToMake.add(dirToMake);
                        }
                        matcher = mkdirParentDirectoryMissingPattern1.matcher(line);
                        if (matcher.find()) {
                            /* Parent directory does not exist, schedule it for creation. */
                            File dir = new File(matcher.group(1));
                            dirsToMake.add(dir);
                            dirsToMake.add(dirToMake);
                        }
                        matcher = mkdirParentDirectoryMissingPattern2.matcher(line);
                        if (matcher.find()) {
                            /* Parent directory does not exist, schedule it for creation. */
                            File dir = new File(matcher.group(1));
                            dirsToMake.add(dir);
                            dirsToMake.add(dirToMake);
                        }
                    }
                }).waitFor();
            }

            while (dirsToMake.isEmpty() && dirsToCheckout.isEmpty() && !filesToMake.isEmpty()) {
                final File fileToMake = filesToMake.pollFirst();
                pb.reset("makeFile").command("mkelem").noComment().arguments("-eltype", "file").argument(fileToMake.getPath()).create()
                        .addOutWriter(new LineSplittingWriter() {
                    @Override
                    protected void processLine(String line) {
                        Matcher matcher = mkfileCheckoutPattern.matcher(line);
                        if (matcher.find()) {
                            File dir = new File(matcher.group(1));
                            filesCheckedOut.add(dir);
                        }
                    }
                })
                        .addErrWriter(new LineSplittingWriter() {
                    @Override
                    protected void processLine(String line) {
                        Matcher matcher = mkfileNeedCheckoutPattern.matcher(line);
                        if (matcher.find()) {
                            /* Parent directory should have been checked out first. */
                            File dir = new File(matcher.group(1));
                            dirsToCheckout.add(dir);
                            filesToMake.add(fileToMake);
                        }
                        matcher = mkfileAlreadyExistPattern.matcher(line);
                        if (matcher.find()) {
                            /* File already exists. Instead of creating it, schedule it for checkout. */
                            File dir = new File(matcher.group(1));
                            filesToCheckout.add(dir);
                        }
                        matcher = mkfileParentDirectoryMissingPattern1.matcher(line);
                        if (matcher.find()) {
                            /* Parent directory does not exist, schedule it for creation. */
                            File dir = new File(matcher.group(1));
                            dirsToMake.add(dir);
                            filesToMake.add(fileToMake);
                        }
                        matcher = mkfileParentDirectoryMissingPattern2.matcher(line);
                        if (matcher.find()) {
                            /* Parent directory does not exist, schedule it for creation. */
                            File dir = new File(matcher.group(1));
                            dirsToMake.add(dir);
                            filesToMake.add(fileToMake);
                        }
                    }
                }).waitFor();
            }
        }
    }
    static final Pattern mkfileParentDirectoryMissingPattern1 = Pattern.compile("cleartool: Error: Not a vob object: \"(.*)\"\\.");
    static final Pattern mkfileParentDirectoryMissingPattern2 = Pattern.compile("cleartool: Error: Unable to access \"(.*)\": No such file or directory\\.");
    static final Pattern mkfileAlreadyExistPattern = Pattern.compile("cleartool: Error: Entry named \"(.*)\" already exists\\.");
    static final Pattern mkfileCheckoutPattern = Pattern.compile("Checked out \"(.*)\" from version \"(.*)\".");
    static final Pattern mkfileNeedCheckoutPattern = Pattern.compile("cleartool: Error: Can\'t modify directory \"(.*)\" because it is not checked out\\.");
    static final Pattern mkdirParentDirectoryMissingPattern1 = Pattern.compile("cleartool: Error: Not a vob object: \"(.*)\"\\.");
    static final Pattern mkdirParentDirectoryMissingPattern2 = Pattern.compile("cleartool: Error: Unable to access \"(.*)\": No such file or directory\\.");
    static final Pattern mkdirCheckoutPattern = Pattern.compile("Checked out \"(.*)\" from version \"(.*)\".");
    static final Pattern mkdirNeedCheckoutPattern = Pattern.compile("cleartool: Error: Can\'t modify directory \"(.*)\" because it is not checked out\\.");

    public void makeDirs(List<File> dirsAdded) throws IOException {
        safeMakeElements(dirsAdded, null);
    }

    public void makeFiles(List<File> filesAdded) throws IOException {
        safeMakeElements(null, filesAdded);
    }
}
