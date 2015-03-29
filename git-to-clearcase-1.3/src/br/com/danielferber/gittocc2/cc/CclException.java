/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.cc;

/**
 *
 * @author X7WS
 */
public class CclException extends RuntimeException {

    public static class NoActivity extends CclException {

        public NoActivity() {
            super("No activity set.");
        }
    }

    public static class UpdateInProgress extends CclException {

        public UpdateInProgress() {
            super("Update in progress.");
        }
    }

    public static class ActivityNotFound extends CclException {

        public ActivityNotFound() {
            super("Activity not found.");
        }
    }

    protected CclException(String string) {
        super(string);
    }
}
