/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

/**
 *
 * @author Daniel Felix Ferber
 */
class SyncTaskException extends Exception {
	private static final long serialVersionUID = 1L;

	public SyncTaskException(final String message) {
        super(message);
    }

    public SyncTaskException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
