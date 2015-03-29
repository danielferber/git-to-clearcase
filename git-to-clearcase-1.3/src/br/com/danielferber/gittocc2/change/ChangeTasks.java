/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.change;

import br.com.danielferber.gittocc2.Context;
import br.com.danielferber.gittocc2.cc.CcCommander;
import br.com.danielferber.gittocc2.cc.CcConfig;
import br.com.danielferber.gittocc2.cc.CclException;
import br.com.danielferber.gittocc2.config.ConfigException;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import java.util.Date;
import java.util.HashMap;
import org.apache.commons.lang3.text.StrSubstitutor;

/**
 *
 * @author Daniel Felix Ferber
 */
public class ChangeTasks {

    private final Context context;

    private final ChangeConfig config;
    private final CcConfig ctConfig;
    private CcCommander ctCommander;

    public ChangeTasks(Context context, ChangeConfig config, CcConfig ctConfig) {
        this.context = context;
        this.config = config;
        this.ctConfig = ctConfig;
    }

    private CcCommander extractClearToolCommander() throws ConfigException {
        if (ctCommander == null) {
            ctCommander = new CcCommander(ctConfig);
        }
        return ctCommander;
    }

    public class WriteStampTask implements Runnable {

        final CcCommander commander = extractClearToolCommander();

        public WriteStampTask() {
            // Fails for inconsistent config.
            if (config.hasCommitStampFile()) {
                config.getCommitStampAbsoluteFile();
            }
            if (config.hasCounterStampFile()) {
                config.getCounterStampAbsoluteFile();
            }
        }

        @Override
        public void run() {
            final Meter m = Meter.getCurrentInstance();
            if (config.hasCommitStampFile()) {
                commander.checkoutFile(config.getCommitStampAbsoluteFile());
                final String commit = context.getCurrentCommitStamp();
                context.writeCommitStampFile(commit);
                m.ctx("commit", commit).ctx("commitStampFile", config.getCommitStampAbsoluteFile());
            }
            if (config.hasCounterStampFile()) {
                commander.checkoutFile(config.getCounterStampAbsoluteFile());
                final long counter = context.getCurrentCounterStamp() + 1;
                context.writeCounterStampFile(counter);
                m.ctx("counter", counter).ctx("counterStampFile", config.getCommitStampAbsoluteFile());
            }
        }
    }

    public class UpdateStampTask implements Runnable {

        final CcCommander commander = extractClearToolCommander();

        public UpdateStampTask() {
            // Fails for inconsistent config.
            config.getCommitStampAbsoluteFile();
            config.getCounterStampAbsoluteFile();
        }

        @Override
        public void run() {
            commander.update(config.getCommitStampAbsoluteFile());
            commander.update(config.getCounterStampAbsoluteFile());
        }
    }

    public class DefineActivityStampTask implements Runnable {

        final CcCommander commander = extractClearToolCommander();

        @Override
        public void run() {
            final HashMap<String, Object> map = new HashMap<>();
            map.put("commit", context.getCurrentCommitStamp());
            map.put("date", new Date());
            map.put("count", context.getCurrentCounterStamp());
            final StrSubstitutor sub = new StrSubstitutor(map);
            final String activityName = sub.replace(config.getActiviyName());
            try {
                ctCommander.setActivity(activityName);
            } catch (CclException.ActivityNotFound e) {
                ctCommander.createActivity(activityName);
            }
            Meter.getCurrentInstance().ctx("activity", activityName);
        }
    }
}
