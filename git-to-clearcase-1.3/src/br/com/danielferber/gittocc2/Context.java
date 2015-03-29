/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.change.ChangeConfig;
import br.com.danielferber.gittocc2.change.ChangeSet;
import br.com.danielferber.gittocc2.config.ConfigException;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author x7ws
 */
public class Context {

    private ChangeSet changeSet;
    private final ChangeConfig changeConfig;

    public Context(ChangeConfig changeConfig) {
        this.changeConfig = changeConfig;
    }

    public void setChangeSet(ChangeSet changeSet) {
        this.changeSet = changeSet;
    }

    public ChangeSet getChangeSet() {
        return changeSet;
    }

    public static class NoCurrentCommitStamp extends RuntimeException {
        
    }

    public String getCurrentCommitStamp() throws NoCurrentCommitStamp {
        String commit = changeConfig.getCommitStampOverride();
        if (commit != null) {
            return commit;
        }
        if (changeConfig.hasCommitStampFile()) {
            return readCommitStampFile();
        }
        throw new NoCurrentCommitStamp();
    }

    public static class NoCurrentCounterStamp extends RuntimeException {
        
    }
    
    public long getCurrentCounterStamp() throws NoCurrentCounterStamp {
        Long counter = changeConfig.getCounterStampOverride();
        if (counter != null) {
            return counter;
        }
        if (changeConfig.hasCounterStampFile()) {
            return readCounterStampFile();
        }
        throw new NoCurrentCounterStamp();
    }

    public static class NoCommitStampFile extends RuntimeException {
        
    }

    public String readCommitStampFile() {
        try (Scanner scanner = new Scanner(changeConfig.getCommitStampAbsoluteFile())) {
            final String result = scanner.next();
            return result;
        } catch (final FileNotFoundException ex) {
            throw new NoCommitStampFile();
        }
    }

    public static class NoCommitCounterFile extends RuntimeException {
        
    }
    
    public long readCounterStampFile() {
        try (Scanner scanner = new Scanner(changeConfig.getCounterStampAbsoluteFile())) {
            final long result = scanner.nextLong();
            return result;
        } catch (final FileNotFoundException ex) {
            throw new NoCommitCounterFile();
        }
    }

    public void writeCommitStampFile(String commit) {
        try (FileWriter writer = new FileWriter(changeConfig.getCommitStampAbsoluteFile())) {
            writer.write(commit + "\n");
        } catch (final IOException ex) {
            throw new ConfigException("Commit stamp file not writeable.", ex);
        }
    }

    public void writeCounterStampFile(long counter) {
        try (FileWriter writer = new FileWriter(changeConfig.getCounterStampAbsoluteFile())) {
            writer.write(Long.toString(counter) + "\n");
        } catch (final IOException ex) {
            throw new ConfigException("Commit stamp file not writeable.", ex);
        }
    }
}
