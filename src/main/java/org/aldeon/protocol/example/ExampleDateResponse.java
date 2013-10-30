package org.aldeon.protocol.example;

import org.aldeon.common.protocol.Response;

import java.util.Date;

public class ExampleDateResponse implements Response {
    public String date = new Date().toString();
}
