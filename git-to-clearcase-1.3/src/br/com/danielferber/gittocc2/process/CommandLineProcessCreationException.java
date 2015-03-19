/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.process;

/**
 *
 * @author x7ws
 */
public class CommandLineProcessCreationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CommandLineProcessCreationException(final String message) {
        super(message);
    }

    public CommandLineProcessCreationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CommandLineProcessCreationException(final Throwable cause) {
        super(cause);
    }
    
}
