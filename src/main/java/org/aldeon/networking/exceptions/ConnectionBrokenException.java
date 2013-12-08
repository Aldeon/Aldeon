package org.aldeon.networking.exceptions;

public class ConnectionBrokenException extends Exception {
    public ConnectionBrokenException(String message) {
        super(message);
    }

    public ConnectionBrokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
