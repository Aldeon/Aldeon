package org.aldeon.communication.netty.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

public class InternalServerErrorException extends UnexpectedServerStatusException {
    public InternalServerErrorException() {
        super(HttpResponseStatus.INTERNAL_SERVER_ERROR);
    }
}
