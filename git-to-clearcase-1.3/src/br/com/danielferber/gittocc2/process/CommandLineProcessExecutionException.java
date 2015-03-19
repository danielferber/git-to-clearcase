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
public class CommandLineProcessExecutionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CommandLineProcessExecutionException(final String message) {
        super(message);
    }

    public CommandLineProcessExecutionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CommandLineProcessExecutionException(final Throwable cause) {
        super(cause);
    }
    
}
