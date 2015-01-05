/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.task;

import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;
import java.util.concurrent.Callable;
import org.slf4j.Logger;

/**
 *
 * @author Daniel Felix Ferber
 */
public abstract class MeterCallable<T> implements Callable<T> {

    private final Meter meter;

    public MeterCallable(String message) {
        this.meter = MeterFactory.getMeter(extractMeterName()).m(message);
    }

    public MeterCallable(final Meter outerMeter, String message) {
        this.meter = outerMeter.sub(extractMeterName()).m(message);
    }

    public MeterCallable(final Meter outerMeter, String meterName, String message) {
        this.meter = outerMeter.sub(meterName).m(message);
    }

    private String extractMeterName() {
        final String name = this.getClass().getSimpleName();
        if (name.endsWith("Task")) {
            return name.substring(1, name.length() - 4);
        }
        return name;
    }

    protected Meter getMeter() {
        return meter;
    }

    protected Logger getLogger() {
        return meter.getLogger();
    }

    @Override
    public final T call() throws Exception {
        meter.start();
        final T value;
        try {
            value = meteredCall();
            meter.ok();
        } catch (final Exception e) {
            meter.fail(e);
            throw e;
        }
        return value;
    }

    protected abstract T meteredCall() throws Exception;
}
