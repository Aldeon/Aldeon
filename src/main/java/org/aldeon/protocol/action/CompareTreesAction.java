package org.aldeon.protocol.action;

import org.aldeon.events.Callback;
import org.aldeon.protocol.Action;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.CompareTreesRequest;
import org.aldeon.protocol.response.MessageNotFoundResponse;

import java.util.concurrent.Executor;

/**
 *
 */
public class CompareTreesAction implements Action<CompareTreesRequest> {
    @Override
    public void respond(CompareTreesRequest request, Callback<Response> onResponse, Executor executor) {
        onResponse.call(new MessageNotFoundResponse());
    }
}
