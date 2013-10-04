package org.aldeon.protocol;

import org.aldeon.common.Observer;
import org.aldeon.protocol.query.Query;
import org.aldeon.protocol.response.Response;

public interface Protocol {
    Response respond(Query query, Observer observer);
}
