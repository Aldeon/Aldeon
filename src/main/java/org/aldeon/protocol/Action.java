package org.aldeon.protocol;


import org.aldeon.events.Callback;

import java.util.concurrent.Executor;

public interface Action<R extends Request> {
    public void respond(R request, Callback<Response> onResponse, Executor executor);
}