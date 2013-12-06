package org.aldeon.communication.netty.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

public class BadRequestException extends UnexpectedServerStatusException {
    public BadRequestException() {
        super(HttpResponseStatus.BAD_REQUEST);
    }
}
