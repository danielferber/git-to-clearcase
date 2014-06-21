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
