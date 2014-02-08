package org.aldeon.crypt.exception;

public class KeyParseException extends Exception {

    public KeyParseException(String message) {
        super(message);
    }

    public KeyParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
