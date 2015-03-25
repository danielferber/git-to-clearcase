/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.change.ChangeConfig;
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

    public String getCurrentCommitStamp() {
        String commit = changeConfig.getCommitStampOverride();
        return commit != null ? commit : readCommitStampFromFile();
    }

    public long getCounterCommitStamp() {
        Long counter = changeConfig.getCounterStampOverride();
        return counter != null ? counter : readCounterStampFromFile();
    }

    public String readCommitStampFromFile() {
        try (Scanner scanner = new Scanner(changeConfig.getCommitStampAbsoluteFile())) {
            final String result = scanner.next();
            return result;
        } catch (final FileNotFoundException ex) {
            throw new ConfigException("Commit stamp file not readable.", ex);
        }
    }

    public long readCounterStampFromFile() {
        try (Scanner scanner = new Scanner(changeConfig.getCounterStampAbsoluteFile())) {
            final long result = scanner.nextLong();
            return result;
        } catch (final FileNotFoundException ex) {
            throw new ConfigException("Counter stamp file not readable.", ex);
        }
    }

    public void writeCommitStampFromFile(String commit) {
        try (FileWriter writer = new FileWriter(changeConfig.getCommitStampAbsoluteFile())) {
            writer.write(commit + "\n");
        } catch (final IOException ex) {
            throw new ConfigException("Commit stamp file not writeable.", ex);
        }
    }

    public void writeCounterStampFromFile(long counter) {
        try (FileWriter writer = new FileWriter(changeConfig.getCounterStampAbsoluteFile())) {
            writer.write(Long.toString(counter) + "\n");
        } catch (final IOException ex) {
            throw new ConfigException("Commit stamp file not writeable.", ex);
        }
    }
}
