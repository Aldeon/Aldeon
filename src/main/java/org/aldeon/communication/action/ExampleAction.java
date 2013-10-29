package org.aldeon.communication.action;

import org.aldeon.common.Observer;
import org.aldeon.protocol.query.ExampleQuery;
import org.aldeon.protocol.response.ExampleResponse;

public class ExampleAction implements Action<ExampleQuery, ExampleResponse> {

    @Override
    public ExampleResponse respond(ExampleQuery query, Observer observer) {
        ExampleResponse response = new ExampleResponse();
        response.status = observer.getSomeTestExampleInt();
        return response;
    }
}
