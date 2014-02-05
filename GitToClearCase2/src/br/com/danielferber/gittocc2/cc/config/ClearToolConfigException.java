/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.danielferber.gittocc2.cc.config;

/**
 *
 * @author Daniel
 */
public class ClearToolConfigException extends RuntimeException {

    public ClearToolConfigException() {
    }

    public ClearToolConfigException(String message) {
        super(message);
    }

    public ClearToolConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClearToolConfigException(Throwable cause) {
        super(cause);
    }
}
