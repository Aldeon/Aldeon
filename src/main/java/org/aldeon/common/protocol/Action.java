package org.aldeon.common.protocol;


public interface Action<Q extends Request, R extends Response> {
    public R respond(Q query);
}