package org.aldeon.db.exception;

public class UnknownParentException extends Exception {
    public UnknownParentException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownParentException(String message) {
        super(message);
    }

    public UnknownParentException() {
        super();
    }
}
