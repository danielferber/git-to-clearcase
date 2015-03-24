/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.change;

import br.com.danielferber.gittocc2.config.ConfigProperties;
import java.io.File;
import java.nio.file.Path;
import java.util.Properties;

public class ChangeConfigProperties extends ChangeConfigImpl {

    public static final String PROP_COMMIT_STAMP_FILE = "stamp.commit.file";
    public static final String PROP_COUNTER_STAMP_FILE = "stamp.counter.file";
    public static final String PROP_COMMIT_STAMP_OVERRIDE = "stamp.commit.override";
    public static final String PROP_COUNTER_STAMP_OVERRIDE = "stamp.counter.override";

    private final ConfigProperties properties;
    private final String prefix;

    public ChangeConfigProperties(Path stampAbsoluteRootDir,  final Properties properties) {
        this(stampAbsoluteRootDir, properties, "");
    }

    public ChangeConfigProperties(Path stampAbsoluteRootDir, final Properties properties, final String prefix) {
        super(stampAbsoluteRootDir);
        this.properties = new ConfigProperties(properties);
        this.prefix = prefix;
    }

    @Override
    public File getCommitStampFile() {
        return properties.getFile(prefix + PROP_COMMIT_STAMP_FILE);
    }

    @Override
    public File getCounterStampFile() {
        return properties.getFile(prefix + PROP_COUNTER_STAMP_FILE);
    }

    @Override
    public String getCommitStampOverride() {
        return properties.getString(prefix + PROP_COMMIT_STAMP_OVERRIDE);
    }

    @Override
    public Long getCounterStampOverride() {
        return properties.getLong(prefix + PROP_COUNTER_STAMP_OVERRIDE);
    }

    public ChangeConfigProperties setCommitStampFile(final File file) {
        properties.setFile(prefix + PROP_COMMIT_STAMP_FILE, file);
        return this;
    }

    public ChangeConfigProperties setCounterStampFile(final File file) {
        properties.setFile(prefix + PROP_COUNTER_STAMP_FILE, file);
        return this;
    }

    public ChangeConfigProperties setSyncCounterOverride(final Long value) {
        properties.setLong(prefix + PROP_COUNTER_STAMP_OVERRIDE, value);
        return this;
    }

    public ChangeConfigProperties setCommitStampOverride(final String value) {
        properties.setString(prefix + PROP_COMMIT_STAMP_OVERRIDE, value);
        return this;
    }
}
