package org.aldeon.sync;

public class InvalidResponseException extends Exception {

    public InvalidResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidResponseException(String message) {
        this(message, null);
    }
}
