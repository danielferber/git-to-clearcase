/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.danielferber.gittocc2.git.config;

/**
 *
 * @author Daniel
 */
public class GitConfigException extends RuntimeException {

    public GitConfigException() {
    }

    public GitConfigException(String message) {
        super(message);
    }

    public GitConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public GitConfigException(Throwable cause) {
        super(cause);
    }
}
