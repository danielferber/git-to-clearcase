/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

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

    public MeterCallable(String name) {
        this.meter = MeterFactory.getMeter(name);
    }

    public MeterCallable(String name, String message) {
        this.meter = MeterFactory.getMeter(name).m(message);
    }

    public MeterCallable(final Meter outerMeter, String name) {
        this.meter = outerMeter.sub(name);
    }

    public MeterCallable(final Meter outerMeter, String name, String message) {
        this.meter = outerMeter.sub(name).m(message);
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
