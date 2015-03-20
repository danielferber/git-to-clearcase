/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.cc;

import br.com.danielferber.gittocc2.process.CommandLineProcess;
import br.com.danielferber.gittocc2.process.CommandLineProcessBuilder;
import br.com.danielferber.gittocc2.process.LineSplittingWriter;
import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Executes ClearTool.exe commands and keeps track of checkedout files.
 *
 * @author Daniel Felix Ferber
 */
public class ClearToolCommander {

    /**
     * Command line process builder able to create cleartool.exe instances.
     */
    final CommandLineProcessBuilder pb;
    /**
     * All files checked out so far.
     */
    final Set<File> filesCheckedOut = new TreeSet<>();
    /**
     * All directories checked out so far.
     */
    final Set<File> dirsCheckedOut = new TreeSet<>();
    /**
     * Dedicated meter to track ClearTool execution.
     */
    final Meter taskMeter = MeterFactory.getMeter("ClearToolCommander");

    public ClearToolCommander(final ClearToolConfig config) {
        this.pb = new CommandLineProcessBuilder(config.getVobViewDir(), config.getClearToolExec(), LoggerFactory.getLogger("ct"));
    }

    /**
     * Check out a single directory.
     *
     * @throws ClearToolException if one checkout fails.
     */
    public void checkoutDir(File... dir) {
        checkoutDirs(Arrays.asList(dir));
    }

    /**
     * Check out a single file.
     *
     * @throws ClearToolException if one checkout fails.
     */
    public void checkoutFile(File... file) {
        checkoutFiles(Arrays.asList(file));
    }
    /*
     * Messages parsed in order to know if directory checkout was successful.
     */
    static final Pattern checkoutDirUpdateInProgress = Pattern.compile("cleartool: Error: Checkouts are not permitted in a snapshot view while an update is in progress.");
    static final Pattern checkoutDirNoActivity = Pattern.compile("cleartool: Error: To operate on UCM branch, must be set to an activity and a UCM view.");
    static final Pattern checkoutDirSuccess = Pattern.compile("Checked out \"(.*)\" from version \"(.*)\"\\.");
    static final Pattern checkoutDirAlready = Pattern.compile("cleartool: Error: Element \"(.*)\" is already checked out to view \"(.*)\"\\.");

    /**
     * Check out multiples directories.
     *
     * @throws ClearToolException if one checkout fails.
     */
    public void checkoutDirs(final Collection<File> dirs) {
        final Meter m = taskMeter.sub("checkout.dirs").iterations(dirs.size()).start();
        for (final File dir : dirs) {
            if (!dirsCheckedOut.contains(dir)) {
                final CommandLineProcess process = pb.reset("checkout.dir").command("checkout").argument("-ptime").argument("-nc").argument("-nquery").argument(dir.getPath()).create();
                process.addOutWriter(new LineSplittingWriter() {
                    @Override
                    protected void processLine(final String line) {
                        Matcher matcher = checkoutDirSuccess.matcher(line);
                        if (matcher.find()) {
                            dirsCheckedOut.add(dir);
                            taskMeter.getLogger().debug("Checkout (new) (directory): {}", dir);
                        }
                    }
                });
                process.addErrWriter(new LineSplittingWriter() {
                    @Override
                    protected void processLine(final String line) {
                        Matcher matcher = checkoutDirUpdateInProgress.matcher(line);
                        if (matcher.find()) {
                            throw new ClearToolException.UpdateInProgress();
                        }
                        matcher = checkoutDirNoActivity.matcher(line);
                        if (matcher.find()) {
                            throw new ClearToolException.NoActivity();
                        }
                        matcher = checkoutDirAlready.matcher(line);
                        if (matcher.find()) {
                            dirsCheckedOut.add(dir);
                            taskMeter.getLogger().debug("Checkout (reuse) (directory): {}", dir);
                        }
                    }
                });
                process.waitFor();
                Exception exception = process.getException();
                if (exception instanceof ClearToolException) {
                    m.fail(exception);
                    throw (ClearToolException) exception;
                } else if (exception != null) {
                    m.fail(exception);
                    throw new RuntimeException(exception);
                }
                m.inc().progress();
            }
        }
        m.ok();
    }
    /*
     * Messages parsed in order to know if fire checkout was successful.
     */
    static final Pattern checkoutFileUpdateInProgress = Pattern.compile("cleartool: Error: Checkouts are not permitted in a snapshot view while an update is in progress.");
    static final Pattern checkoutFileNoActivity = Pattern.compile("cleartool: Error: To operate on UCM branch, must be set to an activity and a UCM view.");
    static final Pattern checkoutFileSuccess = Pattern.compile("Checked out \"(.*)\" from version \"(.*)\"\\.");
    static final Pattern checkoutFileAlready = Pattern.compile("cleartool: Error: Element \"(.*)\" is already checked out to view \"(.*)\"\\.");

    /**
     * Check out multiples files.
     *
     * @throws ClearToolException if one checkout fails.
     */
    public void checkoutFiles(final Collection<File> files) {
        final Meter m = taskMeter.sub("checkout.files").iterations(files.size()).start();
        for (final File file : files) {
            if (!filesCheckedOut.contains(file)) {
                final CommandLineProcess process = pb.reset("checkout.file").command("checkout").argument("-ptime").argument("-nc").argument("-nquery").argument(file.getPath()).create();
                process.addOutWriter(new LineSplittingWriter() {
                    @Override
                    protected void processLine(final String line) {
                        Matcher matcher = checkoutFileSuccess.matcher(line);
                        if (matcher.find()) {
                            filesCheckedOut.add(file);
                            taskMeter.getLogger().debug("Checkout (new) (file): {}", file);
                        }
                    }
                });
                process.addErrWriter(new LineSplittingWriter() {
                    @Override
                    protected void processLine(final String line) {
                        Matcher matcher = checkoutFileUpdateInProgress.matcher(line);
                        if (matcher.find()) {
                            throw new ClearToolException.UpdateInProgress();
                        }
                        matcher = checkoutFileNoActivity.matcher(line);
                        if (matcher.find()) {
                            throw new ClearToolException.NoActivity();
                        }
                        matcher = checkoutFileAlready.matcher(line);
                        if (matcher.find()) {
                            filesCheckedOut.add(file);
                            taskMeter.getLogger().debug("Checkout (reuse) (file): {}", file);
                        }
                    }
                });
                process.waitFor();
                Exception exception = process.getException();
                if (exception instanceof ClearToolException) {
                    m.fail(exception);
                    throw (ClearToolException) exception;
                } else if (exception != null) {
                    m.fail(exception);
                    throw new RuntimeException(exception);
                }
                m.inc().progress();
            }
        }
        m.ok();
    }

    /*cleartool: Error: Unable to check in \"(.*)\".*/
    static final Pattern checkinFileFailure = Pattern.compile("Checked in \"(.*)\" version \"(.*)\"\\.");
    static final Pattern checkinFileSuccess = Pattern.compile("Checked in \"(.*)\" version \"(.*)\"\\.");
    static final Pattern checkinDirSuccess = Pattern.compile("Checked in \"(.*)\" version \"(.*)\"\\.");

    /**
     * Check in multiples directories.
     */
    public void checkinDir(File... dirs) {
        checkinDirs(Arrays.asList(dirs));
    }

    /**
     * Check in multiples directories.
     */
    public void checkinDirs(final Collection<File> dirs) {
        final Meter m = taskMeter.sub("checkin.dirs").iterations(dirs.size()).start();
        for (final File dir : dirs) {
            if (dirsCheckedOut.contains(dir)) {
                final CommandLineProcess process = pb.reset("checkin.dir").command("checkin").argument("-ptime").argument("-nc").argument(dir.getPath()).create();
                process.addOutWriter(new LineSplittingWriter() {
                    @Override
                    protected void processLine(final String line) {
                        Matcher matcher = checkinDirSuccess.matcher(line);
                        if (matcher.find()) {
                            dirsCheckedOut.remove(dir);
                            taskMeter.getLogger().debug("Checkin (dir): {}", dir);
                        }
                    }
                });
                process.waitFor();
                m.inc().progress();
            }
        }
        m.ok();
    }

    /**
     * Check in multiples files.
     */
    public void checkinFile(File... files) {
        checkinFiles(Arrays.asList(files));
    }

    /**
     * Check in multiples files.
     */
    public void checkinFiles(final Collection<File> files) {
        final Meter m = taskMeter.sub("checkin.files").iterations(files.size()).start();
        for (final File file : files) {
            if (filesCheckedOut.contains(file)) {
                final CommandLineProcess process = pb.reset("checkin.file").command("checkin").argument("-ptime").argument("-nc").argument(file.getPath()).create();
                process.addOutWriter(new LineSplittingWriter() {
                    @Override
                    protected void processLine(final String line) {
                        Matcher matcher = checkinDirSuccess.matcher(line);
                        if (matcher.find()) {
                            filesCheckedOut.remove(file);
                            taskMeter.getLogger().debug("Checkin (file): {}", file);
                        }
                    }
                });
                process.waitFor();
                m.inc().progress();
            }
        }
        m.ok();
    }

    /**
     * Check in all files and directories that were checked out by this
     * commander.
     */
    public void checkinAll() {
        checkinFiles();
        checkinDirs();
    }

    /**
     * @return number of directories to check in.
     */
    public int checkinDirsCount() {
        return dirsCheckedOut.size();
    }

    /**
     * @return true if there are directories to check in.
     */
    public boolean checkinDirsRequired() {
        return !dirsCheckedOut.isEmpty();
    }

    /**
     * Checkin all directories that were checked out by this commander.
     */
    public void checkinDirs() {
        ClearToolCommander.this.checkinDirs(new TreeSet<File>(dirsCheckedOut));
    }

    /**
     * @return number of files to check in.
     */
    public int checkinFilesCount() {
        return filesCheckedOut.size();
    }

    /**
     * @return true if there are files to check in.
     */
    public boolean checkinFilesRequired() {
        return !filesCheckedOut.isEmpty();
    }

    /**
     * Checkin all files that were checked out by this commander.
     */
    public void checkinFiles() {
        checkinFiles(new TreeSet<File>(filesCheckedOut));
    }

    /**
     * Remove multiple files.
     */
    public void removeFile(File... files) {
        removeFiles(Arrays.asList(files));
    }

    /**
     * Remove multiple files.
     */
    public void removeFiles(final Collection<File> files) {
        final Meter m = taskMeter.sub("remove.file").iterations(files.size()).start();
        for (final File file : files) {
            pb.reset("rmname.file").command("rmname").argument("-force").argument("-nc").argument(file.getPath()).create().waitFor();
            m.inc().progress();
        }
        m.ok();
    }

    /**
     * Remove multiple directories.
     */
    public void removeDir(File... dirs) {
        removeDirs(Arrays.asList(dirs));
    }

    /**
     * Remove multiple directories.
     */
    public void removeDirs(final Collection<File> dirs) {
        final Meter m = taskMeter.sub("remove.dirs").iterations(dirs.size()).start();
        for (final File dir : dirs) {
            pb.reset("rmname.dir").command("rmname").argument("-force").argument("-nc").argument(dir.getPath()).create().waitFor();
            m.inc().progress();
        }
        m.ok();
    }

    /**
     * Move a single file.
     */
    public void moveFile(final File source, final File target) {
        final Meter m = taskMeter.sub("move.file").start();
        pb.reset("move.file").command("mv").argument("-nc").argument(source.getPath()).argument(target.getPath()).create().waitFor();
        m.ok();
    }

    /*
    
     * TRACE 08 17:20 ct.makeFile: Created element "Gerenciador\Client\ClienteRCP-Integracao-Jide\src\br\com\petrobras\jconsuelo\clientercp\integracao\jide\topologia\ReferenciaProdutoVirtual.java" (type "file").
 
     TRACE 08 17:20 ct.makeFile: Created branch "JCONSUELO_UCM_DVL" from "Gerenciador\Client\ClienteRCP-Integracao-Jide\src\br\com\petrobras\jconsuelo\clientercp\integracao\jide\topologia\ReferenciaProdutoVirtual.java" version "\main\0".
 
     TRACE 08 17:20 ct.makeFile: Checked out "Gerenciador\Client\ClienteRCP-Integracao-Jide\src\br\com\petrobras\jconsuelo\clientercp\integracao\jide\topologia\ReferenciaProdutoVirtual.java" from version "\main\JCONSUELO_UCM_DVL\0".
 
     TRACE 08 17:20 ct.makeFile:   Attached activity:
 
     TRACE 08 17:20 ct.makeFile:     activity:sprint-12-atualizacao-58-8d55de78e115b5e53867fd69c826ec49e45afd4a@\JCONSUELO_UCM  "sprint-12-atualizacao-58-8d55de78e115b5e53867fd69c826ec49e45afd4a"
 

     */
    /**
     * Helper method to create files and directories. Create parent directories
     * if necessary. Checks out parent directories if necessary.
     */
    private void makeElements(final Collection<File> dirs, final Collection<File> files) {
        final Meter m = taskMeter.sub("make.elements").start();
        final TreeSet<File> dirsToCheckout = new TreeSet<>();
        final TreeSet<File> filesToCheckout = new TreeSet<>();
        final TreeSet<File> dirsToMake = new TreeSet<>();
        final TreeSet<File> filesToMake = new TreeSet<>();
        if (files != null) {
            filesToMake.addAll(files);
        }
        if (dirs != null) {
            dirsToMake.addAll(dirs);
        }
        m.iterations(filesToMake.size() + dirsToMake.size());

        while (!dirsToCheckout.isEmpty() || !filesToCheckout.isEmpty() || !dirsToMake.isEmpty() || !filesToMake.isEmpty()) {
            checkoutDirs(dirsToCheckout);
            dirsToCheckout.clear();
            checkoutFiles(filesToCheckout);
            filesToCheckout.clear();

            while (dirsToCheckout.isEmpty() && !dirsToMake.isEmpty()) {
                final File dirToMake = dirsToMake.pollFirst();
                pb.reset("make.dir").command("mkdir").argument("-nc").argument(dirToMake.getPath()).create()
                        .addOutWriter(new LineSplittingWriter() {
                            @Override
                            protected void processLine(final String line) {
                                final Matcher checkoutMatcher = mkdirCheckoutPattern.matcher(line);
                                if (checkoutMatcher.find()) {
                                    final File dir = new File(checkoutMatcher.group(1));
                                    dirsCheckedOut.add(dir);
                                }
                            }
                        })
                        .addErrWriter(new LineSplittingWriter() {
                            @Override
                            protected void processLine(final String line) {
                                Matcher matcher = mkdirNeedCheckoutPattern.matcher(line);
                                if (matcher.find()) {
                                    /* Parent directory should have been checked out first. */
                                    final File dir = new File(matcher.group(1));
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
                                    final File dir = new File(matcher.group(1));
                                    dirsToMake.add(dir);
                                    dirsToMake.add(dirToMake);
                                }
                                matcher = mkdirParentDirectoryMissingPattern2.matcher(line);
                                if (matcher.find()) {
                                    /* Parent directory does not exist, schedule it for creation. */
                                    final File dir = new File(matcher.group(1));
                                    dirsToMake.add(dir);
                                    dirsToMake.add(dirToMake);
                                }
                            }
                        }).waitFor();
            }

            while (dirsToMake.isEmpty() && dirsToCheckout.isEmpty() && filesToCheckout.isEmpty() && !filesToMake.isEmpty()) {
                final File fileToMake = filesToMake.pollFirst();
                pb.reset("make.file").command("mkelem").argument("-nc").arguments("-eltype", "file").argument(fileToMake.getPath()).create()
                        .addOutWriter(new LineSplittingWriter() {
                            @Override
                            protected void processLine(final String line) {
                                final Matcher matcher = mkfileCheckoutPattern.matcher(line);
                                if (matcher.find()) {
                                    final File dir = new File(matcher.group(1));
                                    filesCheckedOut.add(dir);
                                }
                            }
                        })
                        .addErrWriter(new LineSplittingWriter() {
                            @Override
                            protected void processLine(final String line) {
                                Matcher matcher = mkfileNeedCheckoutPattern.matcher(line);
                                if (matcher.find()) {
                                    /* Parent directory should have been checked out first. */
                                    final File dir = new File(matcher.group(1));
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
                                    final File dir = new File(matcher.group(1));
                                    dirsToMake.add(dir);
                                    filesToMake.add(fileToMake);
                                }
                                matcher = mkfileParentDirectoryMissingPattern2.matcher(line);
                                if (matcher.find()) {
                                    /* Parent directory does not exist, schedule it for creation. */
                                    final File dir = new File(matcher.group(1));
                                    dirsToMake.add(dir);
                                    filesToMake.add(fileToMake);
                                }
                            }
                        }).waitFor();
                m.inc().progress();
            }
        }

        m.ok();
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

    /**
     * Create multiple directories.
     */
    public void makeDirs(final List<File> dirsAdded) {
        makeElements(dirsAdded, null);
    }

    /**
     * Create multiple files.
     */
    public void makeFiles(final List<File> filesAdded) {
        makeElements(null, filesAdded);
    }

    /**
     * Update multiple files or directories.
     * @param elements Files or directories to update.
     */
    public void update(final File... elements) {
        update(Arrays.asList(elements));
    }

    /* Update: "Done loading "\JCONSUELO\Fontes\atualizacao-contador.txt" (1 objects, copied 0 KB)." */
    /**
     * Update multiple files or directories.
     * @param elements Files or directories to update.
     */
    public void update(final Collection<File> elements) {
        final Meter m = taskMeter.sub("update").iterations(elements.size()).start();
        for (final File file : elements) {
            pb.reset("update").command("update").argument("-force").argument(file.getPath()).create().waitFor();
            m.inc().progress();
        }
        m.ok();
    }

    private static final Pattern currentActivityPattern = Pattern.compile("(.*)  (.*)  (.*)   \"(.*)\"");

    private static class LsActivityItem {

        String date;
        String name;
        String headline;
        boolean found = false;
    }

    public String currentActivity() {
        final Meter m = taskMeter.sub("activity.current").start();
        final LsActivityItem result = new LsActivityItem();
        pb.reset("lsactivity").command("lsactivity").argument("-cact").create().addOutWriter(new LineSplittingWriter() {
            @Override
            protected void processLine(java.lang.String line) throws IOException {
                Matcher matcher = currentActivityPattern.matcher(line);
                if (matcher.find()) {
                    result.found = true;
                    result.date = matcher.group(1);
                    result.name = matcher.group(2);
                    result.headline = matcher.group(3);
                }
            }
        }).waitFor();
        m.ok();
        if (result.found) {
            return result.name;
        }
        return null;
    }

    /*
     * cleartool: Error: Invalid name: "sprint 12-atualizacao-58 (8d55de78e115b5e53867fd69c826ec49e45afd4a)".
     * cleartool: Error: Unable to create activity.
     * Created activity "sprint-12-atualizacao-58-8d55de78e115b5e53867fd69c826ec49e45afd4a".
     * Set activity "sprint-12-atualizacao-58-8d55de78e115b5e53867fd69c826ec49e45afd4a" in view "X7WS_JCONSUELO_DVL_X7WS_MI00308113".
     */
    public void createActivity(final String name) {
        final Meter m = taskMeter.sub("activity.create").start();
        pb.reset("mkactivity").command("mkactivity").arguments("-force", name).create().addOutWriter(new LineSplittingWriter() {
            @Override
            protected void processLine(java.lang.String line) throws IOException {
                System.out.println(line);
            }
        }).addErrWriter(new LineSplittingWriter() {
            @Override
            protected void processLine(java.lang.String line) throws IOException {
                System.out.println(line);
            }
        }).waitFor();
        m.ok();
    }
    static final Pattern setActivityNotFoundPattern = Pattern.compile("cleartool: Error: Unable to find activity \"(.*)\"\\.");

    public void setActivity(final String name, boolean allowCreate) {
        final Meter m = taskMeter.sub("activity.set").start();
        final CommandLineProcess process = pb.reset("setactivity").command("setactivity").arguments(name).create();
        process.addOutWriter(new LineSplittingWriter() {
            @Override
            protected void processLine(java.lang.String line) throws IOException {
                // ignore
            }
        }).addErrWriter(new LineSplittingWriter() {
            @Override
            protected void processLine(java.lang.String line) throws IOException {
                Matcher matcher = setActivityNotFoundPattern.matcher(line);
                if (matcher.find()) {
                    throw new ClearToolException.ActivityNotFound();
                }
            }
        }).waitFor();
        Exception exception = process.getException();
        if (exception instanceof ClearToolException) {
            m.fail(exception);
            throw (ClearToolException) exception;
        } else if (exception != null) {
            m.fail(exception);
            throw new RuntimeException(exception);
        }
        m.ok();
    }

    /*
     * FIND CHECKOUTs.
     */
    static final Pattern checkoutEntry = Pattern.compile("([^\\s]+)\\s+([^\\s]+)\\s+checkout version\\s+\"(.*)\"\\s+from\\s+(.*)");

    public static class LsCheckoutElement {

        public LsCheckoutElement(String date, String user, File file) {
            this.date = date;
            this.user = user;
            this.file = file;
        }

        public final String date;
        public final String user;
        public final File file;
    }

    /**
     * Find checkouts for files and directories.
     *
     * @param dir diretory to look at recursively.
     * @return List of checked out element
     */
    public Collection<LsCheckoutElement> listCheckouts(final File dir) {
        final Collection<LsCheckoutElement> list = new ArrayList<>();
        taskMeter.sub("checkout.ls").run(new Runnable() {
            @Override
            public void run() {
                final CommandLineProcess process = pb.reset("lscheckout.all").command("lscheckout").argument("-all").create();
                process.addOutWriter(new LineSplittingWriter() {
                    @Override
                    protected void processLine(final String line) {
                        Matcher matcher = checkoutEntry.matcher(line);
                        if (matcher.find()) {
                            list.add(new LsCheckoutElement(matcher.group(1), matcher.group(2), new File(matcher.group(3))));
                        }
                    }
                }).waitFor();
            }
        });
        return list;
    }

    /**
     * Load checkouts for files and directories into commander.
     *
     * @param dir diretory to look at recursively.
     */
    public void loadCheckouts(final File dir) {
        taskMeter.sub("checkout.load").ctx("dir", dir).run(() -> {
            final CommandLineProcess process = pb.reset("lscheckout.all").command("lscheckout").argument("-all").create();
            process.addOutWriter(new LineSplittingWriter() {
                @Override
                protected void processLine(final String line) {
                    Matcher matcher = checkoutEntry.matcher(line);
                    if (matcher.find()) {
                        File item = new File(matcher.group(3));
                        if (item.isDirectory()) {
                            dirsCheckedOut.add(item);
                        } else {
                            filesCheckedOut.add(item);
                        }
                    }
                }
            }).waitFor();
        });
    }
}
