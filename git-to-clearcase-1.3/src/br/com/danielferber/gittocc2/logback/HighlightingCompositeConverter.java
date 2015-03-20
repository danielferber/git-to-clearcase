package br.com.danielferber.gittocc2.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import static ch.qos.logback.core.pattern.color.ANSIConstants.BLACK_FG;
import static ch.qos.logback.core.pattern.color.ANSIConstants.BOLD;
import static ch.qos.logback.core.pattern.color.ANSIConstants.CYAN_FG;
import static ch.qos.logback.core.pattern.color.ANSIConstants.DEFAULT_FG;
import static ch.qos.logback.core.pattern.color.ANSIConstants.RED_FG;
import static ch.qos.logback.core.pattern.color.ANSIConstants.YELLOW_FG;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;

/**
 *
 * @author Daniel Felix Ferber
 */
public class HighlightingCompositeConverter extends ForegroundCompositeConverterBase<ILoggingEvent> {

    @Override
    protected String getForegroundColorCode(final ILoggingEvent event) {
        final Level level = event.getLevel();
        switch (level.toInt()) {
            case Level.ERROR_INT:
                return BOLD+RED_FG;
            case Level.WARN_INT:
                return YELLOW_FG;
            case Level.INFO_INT:
                return CYAN_FG;
            case Level.TRACE_INT:
                return BOLD+BLACK_FG; // actually displays as gray.
            default:
                return DEFAULT_FG;
        }

    }
}
