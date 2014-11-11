/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.logback;

import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import org.slf4j.Logger;

/**
 *
 * @author X7WS
 */
public class LogbackConfigurationTest {
        public static void main(final String[] argv) {
            Logger logger = LoggerFactory.getLogger(LogbackConfigurationTest.class);
            logger.error("error");
            logger.warn("warn");
            logger.info("info");
            logger.debug("debug");
            logger.trace("trace");
        }
}
