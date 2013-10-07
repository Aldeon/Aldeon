package org.aldeon.jetty.resolver;

import org.aldeon.common.Observer;

public interface QueryResolver {
    public String queryResponse(String query, Observer observer) throws InvalidQueryException;
    public String errorResponse();
}
