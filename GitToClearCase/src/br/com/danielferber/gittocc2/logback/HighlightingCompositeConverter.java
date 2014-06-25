/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import static ch.qos.logback.core.pattern.color.ANSIConstants.*;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;

/**
 *
 * @author Daniel Felix Ferber
 */
public class HighlightingCompositeConverter extends ForegroundCompositeConverterBase<ILoggingEvent> {

    @Override
    protected String getForegroundColorCode(ILoggingEvent event) {
        Level level = event.getLevel();
        switch (level.toInt()) {
            case Level.ERROR_INT:
                return BOLD+RED_FG;
            case Level.WARN_INT:
                return YELLOW_FG;
            case Level.INFO_INT:
                return CYAN_FG;
            default:
                return DEFAULT_FG;
        }

    }
}
