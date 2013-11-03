package org.aldeon.protocol.action;

import org.aldeon.events.Callback;
import org.aldeon.protocol.Action;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetPeersInterestedRequest;
import org.aldeon.protocol.response.MessageNotFoundResponse;

import java.util.concurrent.Executor;

/**
 *
 */
public class GetPeersInterestedAction implements Action<GetPeersInterestedRequest> {

    @Override
    public void respond(GetPeersInterestedRequest request, Callback<Response> onResponse, Executor executor) {

    }
}

