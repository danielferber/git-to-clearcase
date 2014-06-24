/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;

/**
 *
 * @author X7WS
 */
public class LoggerNameFilter extends AbstractMatcherFilter<ILoggingEvent> {

    private String loggerName;

    @Override
    public void start() {
        if (this.loggerName != null) {
            super.start();
        } else {
            addError(String.format("The loggerName property must be set for [%s]", getName()));
        }
    }

    @Override
    public FilterReply decide(ILoggingEvent event) {

        if (event.getLoggerName().startsWith(loggerName)) {
            return onMatch;
        } else {
            return onMismatch;
        }
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public String getLoggerName() {
        return loggerName;
    }
}
