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

    public SyncTaskException(String message) {
        super(message);
    }

    public SyncTaskException(String message, Throwable cause) {
        super(message, cause);
    }

}
