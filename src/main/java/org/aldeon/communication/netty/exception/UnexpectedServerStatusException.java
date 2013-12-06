package org.aldeon.communication.netty.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

public class UnexpectedServerStatusException extends Exception {
    private final HttpResponseStatus status;

    public UnexpectedServerStatusException(HttpResponseStatus status) {
        this.status = status;
    }

    public HttpResponseStatus getStatus() {
        return status;
    }
}
