/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.config;

import br.com.danielferber.gittocc2.task.SynchronizationConfig;
import java.io.PrintStream;
import java.util.Properties;

/**
 *
 * @author Daniel Felix Ferber
 */
public class SynchronizationConfigContainer extends ConfigContainer {

    public static final String PROP_OVERRIDDEN_COUNTER_STAMP = "sync.commit.stamp";
    public static final String PROP_OVERRIDDEN_COMMIT_STAMP = "sync.counter.stamp";
    public static final String PROP_USE_OVERRIDDEN_COUNTER_STAMP = "sync.commit.override";
    public static final String PROP_USE_OVERRIDDEN_COMMIT_STAMP = "sync.counter.override";

    private final SynchronizationBean synchronizationBean = new SynchronizationBean();

    public SynchronizationConfigContainer(Properties properties) {
        super(properties);
    }

    @Override
    public void validateAll() throws ConfigException {
        super.validateAll();
        SynchronizationConfig.validate(synchronizationBean);
    }

    @Override
    public void printConfig(PrintStream ps) {
        ps.println("Synchronization strategy:");
        SynchronizationConfig.printConfig(ps, synchronizationBean);
        super.printConfig(ps);
    }

    public SynchronizationBean getSynchronizationBean() {
        return synchronizationBean;
    }

    public class SynchronizationBean implements SynchronizationConfig {

        @Override
        public Boolean getUseOverriddenCommitStamp() {
            return properties.getBoolean(PROP_USE_OVERRIDDEN_COMMIT_STAMP);
        }

        public SynchronizationBean setUseOverriddenCommitStamp(final Boolean value) {
            properties.setBoolean(PROP_USE_OVERRIDDEN_COMMIT_STAMP, value);
            return this;
        }

        @Override
        public Boolean getUseOverriddenCounterStamp() {
            return properties.getBoolean(PROP_USE_OVERRIDDEN_COUNTER_STAMP);
        }

        public SynchronizationBean setUseOverriddenCounterStamp(final Boolean value) {
            properties.setBoolean(PROP_USE_OVERRIDDEN_COUNTER_STAMP, value);
            return this;
        }

        @Override
        public Long getOverriddenCounterStamp() {
            return properties.getLong(PROP_OVERRIDDEN_COUNTER_STAMP);
        }

        public SynchronizationBean setOverriddenCounterStamp(final Long value) {
            properties.setLong(PROP_OVERRIDDEN_COUNTER_STAMP, value);
            return this;
        }

        @Override
        public String getOverriddenCommitStamp() {
            return properties.getString(PROP_OVERRIDDEN_COMMIT_STAMP);
        }

        public SynchronizationBean setOverriddenCommitStamp(final String value) {
            properties.setString(PROP_OVERRIDDEN_COMMIT_STAMP, value);
            return this;
        }
    }
}
