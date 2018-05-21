package com.heaven7.utils;

/**
 * the load exception
 *
 * @author heaven7
 */
public class LoadException extends RuntimeException {
    public LoadException() {
    }

    public LoadException(String message) {
        super(message);
    }

    public LoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadException(Throwable cause) {
        super(cause);
    }
}