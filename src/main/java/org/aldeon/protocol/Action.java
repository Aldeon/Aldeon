package org.aldeon.protocol;


import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;

public interface Action<Q extends Request, R extends Response> {
    public R respond(Q query);
}