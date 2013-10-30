/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.cc;

import br.com.danielferber.gittocc.process.LineSplittingWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
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

    void checkout(File file) throws IOException {
        checkout(Collections.singleton(file));
    }

    public void checkout(Collection<File> filesOrDirs) throws IOException {
        Iterator<File> iterator = filesOrDirs.iterator();
        while (iterator.hasNext()) {
            List<File> fileOrDirs = new ArrayList<File>(10);
            pb.reset("checkout").command("checkout").preserveTime().noComment();
            for (int i = 0; i < 10 && iterator.hasNext(); i++) {
                final File fileOrDir = iterator.next();
                pb.argument(fileOrDir.getPath());
                fileOrDirs.add(fileOrDir);
            }
            ClearToolProcess p = pb.create();
            p.waitFor();
            for (File fileOrDir : fileOrDirs) {
                if (fileOrDir.isFile()) {
                    filesCheckedOut.add(fileOrDir);
                } else {
                    dirsCheckedOut.add(fileOrDir);
                }
            }
        }
    }

    public void checkin(Collection<File> filesOrDirs) throws IOException {
        Iterator<File> iterator = filesOrDirs.iterator();
        while (iterator.hasNext()) {
            List<File> fileOrDirs = new ArrayList<File>(10);
            pb.reset("checkin").command("checkin").preserveTime().noComment();
            for (int i = 0; i < 10 && iterator.hasNext(); i++) {
                final File fileOrDir = iterator.next();
                pb.argument(fileOrDir.getPath());
                fileOrDirs.add(fileOrDir);
            }
            ClearToolProcess p = pb.create();
            p.waitFor();
            for (File fileOrDir : fileOrDirs) {
                if (fileOrDir.isFile()) {
                    filesCheckedOut.remove(fileOrDir);
                } else {
                    dirsCheckedOut.remove(fileOrDir);
                }
            }
        }
    }

    void checkinAll() throws IOException {
        checkin(filesCheckedOut);
        checkin(dirsCheckedOut);
    }

    public void remove(Collection<File> filesOrDirs) throws IOException {
        Iterator<File> iterator = filesOrDirs.iterator();
        while (iterator.hasNext()) {
            pb.reset("rmname").command("rmname").force().noComment();
            for (int i = 0; i < 10 && iterator.hasNext(); i++) {
                pb.argument(iterator.next().getPath());
            }
            ClearToolProcess p = pb.create();
            p.waitFor();
        }
    }

    public void moveFile(File source, File target) throws IOException {
        pb.reset("move").command("mv").noComment().argument(source.getPath()).argument(target.getPath()).create().waitFor();
    }

    public void makeElements(Collection<File> dirs, Collection<File> files) throws IOException {
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
            while (!dirsToCheckout.isEmpty()) {
                final File dir = dirsToCheckout.pollFirst();
                pb.reset("checkout").command("checkout").preserveTime().noComment().argument(dir.getPath()).create().waitFor();
                dirsCheckedOut.add(dir);
            }

            while (!filesToCheckout.isEmpty()) {
                final File file = filesToCheckout.pollFirst();
                pb.reset("checkout").command("checkout").preserveTime().noComment().argument(file.getPath()).create().waitFor();
                filesCheckedOut.add(file);
            }

            while (dirsToCheckout.isEmpty() && !dirsToMake.isEmpty()) {
                final File dirToMake = dirsToMake.pollFirst();
                pb.reset("mkdir").command("mkdir").noComment().argument(dirToMake.getPath()).create()
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
                pb.reset("mkfile").command("mkelem").noComment().arguments("-eltype", "file").argument(fileToMake.getPath()).create()
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
}
