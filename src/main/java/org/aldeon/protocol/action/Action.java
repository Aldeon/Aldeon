package org.aldeon.protocol.action;

import org.aldeon.common.Observer;
import org.aldeon.protocol.query.Query;
import org.aldeon.protocol.response.Response;

public interface Action<Q extends Query, R extends Response> {
    public R respond(Q query, Observer observer);
}
