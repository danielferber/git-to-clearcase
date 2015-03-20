package br.com.danielferber.gittocc2.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class MarkerFilter extends AbstractMatcherFilter<ILoggingEvent> {

    Marker markerToMatch;

    @Override
    public void start() {
        if (this.markerToMatch != null) {
            super.start();
        } else {
            addError(String.format("The marker property must be set for [%s]", getName()));
        }
    }

    @Override
    public FilterReply decide(final ILoggingEvent event) {
        final Marker marker = event.getMarker();
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }

        if (marker == null) {
            return onMismatch;
        }

        if (markerToMatch.contains(marker)) {
            return onMatch;
        }
        return onMismatch;
    }

    public void setMarker(final String markerStr) {
        if (markerStr != null) {
            markerToMatch = MarkerFactory.getMarker(markerStr);
        }
    }
}
