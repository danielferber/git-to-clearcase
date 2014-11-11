/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

/**
 *
 * @author X7WS
 */
public class ClearToolException extends RuntimeException {

    public static class NoActivity extends ClearToolException {

        public NoActivity() {
            super("No activity set.");
        }
    }

    public static class UpdateInProgress extends ClearToolException {

        public UpdateInProgress() {
            super("Update in progress.");
        }
    }

    public static class ActivityNotFound extends ClearToolException {

        public ActivityNotFound() {
            super("Activity not found.");
        }
    }

    protected ClearToolException(String string) {
        super(string);
    }
}
