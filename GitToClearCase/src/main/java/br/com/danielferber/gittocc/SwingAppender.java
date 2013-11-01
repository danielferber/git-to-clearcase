/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import javax.swing.ListModel;

/**
 *
 * @author X7WS
 */
public class SwingAppender extends AppenderBase<ILoggingEvent> {
    
    public static interface Handler {
        void handle(ILoggingEvent e);
    }
    
    final static List<Handler> handlers = Collections.synchronizedList(new ArrayList<Handler>());
    
    public static void add(Handler h)  {
        handlers.add(h);
    }
    
    @Override
    protected void append(ILoggingEvent e) {
        for (Handler handler : handlers) {
            handler.handle(e);
        }
    }
}
