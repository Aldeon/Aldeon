package org.aldeon.db.exception;

public class UnknownIdentifierException extends Exception {
    public UnknownIdentifierException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownIdentifierException(String message) {
        super(message);
    }

    public UnknownIdentifierException() {
        super();
    }
}
