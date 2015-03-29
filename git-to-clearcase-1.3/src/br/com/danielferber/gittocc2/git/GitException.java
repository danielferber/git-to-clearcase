/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.git;

import br.com.danielferber.gittocc2.cc.CclException;

/**
 *
 * @author X7WS
 */
public class GitException extends RuntimeException {

    public static class NonNullExitValue extends CclException {

        private final int exitValue;

        public NonNullExitValue(int exitValue) {
            super("Git command returned non null exit value: " + exitValue);
            this.exitValue = exitValue;
        }
    }

    protected GitException(String string) {
        super(string);
    }
}
