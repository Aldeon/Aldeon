package org.aldeon.crypt.exception;

public class DecryptionFailedException extends Exception {
    public DecryptionFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
