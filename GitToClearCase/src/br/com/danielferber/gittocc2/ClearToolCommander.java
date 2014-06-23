/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.config.clearcase.ClearToolConfigSource;
import br.com.danielferber.gittocc2.io.process.CommandLineProcess;
import br.com.danielferber.gittocc2.io.process.CommandLineProcessBuilder;
import br.com.danielferber.gittocc2.io.process.LineSplittingWriter;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import java.io.File;
import java.util.Arrays;
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
class ClearToolCommander {

    final CommandLineProcessBuilder pb;
    final Set<File> filesCheckedOut = new TreeSet<File>();
    final Set<File> dirsCheckedOut = new TreeSet<File>();

    public ClearToolCommander(ClearToolConfigSource config) {
        this.pb = new CommandLineProcessBuilder(config.getVobViewDir(), config.getClearToolExec(), LoggerFactory.getLogger("ct"));
    }

    public void createActivity(String headline) {
        pb.reset("mkactivity").command("mkactivity").arguments("-headline", headline, "-force").create().waitFor();
    }

    public void checkoutDir(File dir) {
        checkoutDirs(Collections.singleton(dir));
    }

    public void checkoutFile(File file) {
        checkoutFiles(Collections.singleton(file));
    }

    public void checkoutDirs(Collection<File> dirs) {
        for (File dir : dirs) {
            if (!dirsCheckedOut.contains(dir)) {
                final CommandLineProcess p = pb.reset("checkoutDir").command("checkout").argument("-ptime").argument("-nc").argument("-nquery")
                        .argument(dir.getPath()).create();
                p.waitFor();
                dirsCheckedOut.add(dir);
            }
        }
    }

    public void checkoutFiles(Collection<File> files) {
        for (File file : files) {
            if (!filesCheckedOut.contains(file)) {
                CommandLineProcess p = pb.reset("checkoutFile").command("checkout").argument("-ptime").argument("-nc").argument("-nquery")
                        .argument(file.getPath()).create();
                p.waitFor();
                filesCheckedOut.add(file);
            }
        }
    }

    public void checkinDirs(Collection<File> dirs) {
        for (File dir : dirs) {
            if (dirsCheckedOut.contains(dir)) {
                pb.reset("checkinDir").command("checkin").argument("-ptime").argument("-nc")
                        .argument(dir.getPath()).create().waitFor();
                dirsCheckedOut.remove(dir);
            }
        }
    }

    public void checkinFiles(Collection<File> files) {
        for (File file : files) {
            if (filesCheckedOut.contains(file)) {
                pb.reset("checkinFile").command("checkin").argument("-ptime").argument("-nc")
                        .argument(file.getPath()).create().waitFor();
                filesCheckedOut.remove(file);
            }
        }
    }

    public void checkinAll() {
        checkinFiles();
        checkinDirs();
    }

    public int checkinDirsCount() {
        return dirsCheckedOut.size();
    }

    public boolean checkinDirsRequired() {
        return !dirsCheckedOut.isEmpty();
    }

    public void checkinDirs() {
        ClearToolCommander.this.checkinDirs(new TreeSet(dirsCheckedOut));
    }

    public int checkinFilesCount() {
        return filesCheckedOut.size();
    }

    public boolean checkinFilesRequired() {
        return !filesCheckedOut.isEmpty();
    }

    public void checkinFiles() {
        checkinFiles(new TreeSet(filesCheckedOut));
    }

    public void removeFiles(Collection<File> files) {
        for (File file : files) {
            pb.reset("rmnameFile").command("rmname").argument("-force").argument("-nc").argument(file.getPath()).create().waitFor();
        }
    }

    public void removeDirs(Collection<File> dirs) {
        for (File dir : dirs) {
            pb.reset("rmnameDir").command("rmname").argument("-force").argument("-nc").argument(dir.getPath()).create().waitFor();
        }
    }

    public void moveFile(File source, File target) {
        pb.reset("moveFile").command("mv").argument("-nc").argument(source.getPath()).argument(target.getPath()).create().waitFor();
    }

    private void safeMakeElements(Collection<File> dirs, Collection<File> files) {
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
            dirsToCheckout.clear();
            checkoutFiles(filesToCheckout);
            filesToCheckout.clear();

            while (dirsToCheckout.isEmpty() && !dirsToMake.isEmpty()) {
                final File dirToMake = dirsToMake.pollFirst();
                pb.reset("makeDir").command("mkdir").argument("-nc").argument(dirToMake.getPath()).create()
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
                        matcher = mkdirAlreadyExistPattern.matcher(line);
                        if (matcher.find()) {
                            /* Directory already exists. Instead of creating it, schedule it for checkout. */
                            dirsToCheckout.add(dirToMake);
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

            while (dirsToMake.isEmpty() && dirsToCheckout.isEmpty() && filesToCheckout.isEmpty() && !filesToMake.isEmpty()) {
                final File fileToMake = filesToMake.pollFirst();
                pb.reset("makeFile").command("mkelem").argument("-nc").arguments("-eltype", "file").argument(fileToMake.getPath()).create()
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
                            filesToCheckout.add(fileToMake);
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
    static final Pattern mkdirAlreadyExistPattern = Pattern.compile("cleartool: Error: Entry named \"(.*)\" already exists\\.");

    public void makeDirs(List<File> dirsAdded) {
        safeMakeElements(dirsAdded, null);
    }

    public void makeFiles(List<File> filesAdded) {
        safeMakeElements(null, filesAdded);
    }

    public void updateFiles(File ...files) {
        updateFiles(Arrays.asList(files));
    }
    
    public void updateFiles(Collection<File> files) {
        for (File file : files) {
            CommandLineProcess p = pb.reset("updateFile").command("update").argument("-force").argument(file.getPath()).create();
            p.waitFor();
        }
    }

    void updateVobViewDir() {
        CommandLineProcess p = pb.reset("updateVob").command("update").argument("-force").create();
        p.waitFor();
    }
}
