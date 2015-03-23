/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.change;

import br.com.danielferber.gittocc2.config.ConfigException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public abstract class ChangeConfigImpl implements ChangeConfig {

    private final Path stampAbsoluteRootDir;

    public ChangeConfigImpl(Path stampAbsoluteRootDir) {
        this.stampAbsoluteRootDir = stampAbsoluteRootDir;
    }

    @Override
    public File getCommitStampAbsoluteFile() {
        if (this.getCommitStampFile() == null) {
            return null;
        }
        return stampAbsoluteRootDir.resolve(getCommitStampFile().toPath()).toFile();
    }

    @Override
    public File getCounterStampAbsoluteFile() {
        if (this.getCounterStampFile() == null) {
            return null;
        }
        return stampAbsoluteRootDir.resolve(getCounterStampFile().toPath()).toFile();
    }

    @Override
    public String readCommitStampFromFile() {
        try (Scanner scanner = new Scanner(this.getCommitStampAbsoluteFile())) {
            final String result = scanner.next();
            return result;
        } catch (final FileNotFoundException ex) {
            throw new ConfigException("Commit stamp file not readable.", ex);
        }
    }

    @Override
    public long readCounterStampFromFile() {
        try (Scanner scanner = new Scanner(this.getCounterStampAbsoluteFile())) {
            final long result = scanner.nextLong();
            return result;
        } catch (final FileNotFoundException ex) {
            throw new ConfigException("Counter stamp file not readable.", ex);
        }
    }

    @Override
    public void writeCommitStampFromFile(String commit) {
        try (FileWriter writer = new FileWriter(this.getCommitStampAbsoluteFile())) {
            writer.write(commit + "\n");
        } catch (final IOException ex) {
            throw new ConfigException("Commit stamp file not writeable.", ex);
        }
    }

    @Override
    public void writeCounterStampFromFile(long counter) {
        try (FileWriter writer = new FileWriter(this.getCounterStampAbsoluteFile())) {
            writer.write(Long.toString(counter) + "\n");
        } catch (final IOException ex) {
            throw new ConfigException("Commit stamp file not writeable.", ex);
        }
    }
}
