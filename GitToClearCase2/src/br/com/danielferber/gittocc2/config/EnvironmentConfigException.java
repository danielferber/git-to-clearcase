/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.danielferber.gittocc2.config;

/**
 *
 * @author Daniel
 */
public class EnvironmentConfigException extends RuntimeException {

    public EnvironmentConfigException() {
    }

    public EnvironmentConfigException(String message) {
        super(message);
    }

    public EnvironmentConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public EnvironmentConfigException(Throwable cause) {
        super(cause);
    }
}
