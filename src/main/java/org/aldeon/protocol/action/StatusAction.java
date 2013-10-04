package org.aldeon.protocol.action;

import org.aldeon.common.Observer;
import org.aldeon.protocol.query.StatusQuery;
import org.aldeon.protocol.response.StatusResponse;

public class StatusAction implements Action<StatusQuery, StatusResponse> {

    @Override
    public StatusResponse respond(StatusQuery query, Observer observer) {
        StatusResponse response = new StatusResponse();
        response.status = observer.getSomeTestStatusInt();
        return response;
    }
}
