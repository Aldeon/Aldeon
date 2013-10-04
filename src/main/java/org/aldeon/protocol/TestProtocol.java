package org.aldeon.protocol;

import org.aldeon.common.Observer;
import org.aldeon.protocol.action.Action;
import org.aldeon.protocol.action.StatusAction;
import org.aldeon.protocol.query.Query;
import org.aldeon.protocol.query.StatusQuery;
import org.aldeon.protocol.response.Response;
import org.aldeon.protocol.response.StatusResponse;

public class TestProtocol implements Protocol {

    private Action<StatusQuery, StatusResponse> statusAction;

    public TestProtocol() {
        statusAction = new StatusAction();
    }

    @Override
    public Response respond(Query query, Observer observer) {

        if(query instanceof StatusQuery)
            return statusAction.respond((StatusQuery) query, observer);

        return null;
    }
}
