package org.aldeon.protocol.example;

import org.aldeon.protocol.Response;

import java.util.Date;

public class ExampleDateResponse extends Response {
    public String date = new Date().toString();
}
