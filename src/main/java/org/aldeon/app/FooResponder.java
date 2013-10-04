package org.aldeon.app;

import org.aldeon.common.Responder;

import java.util.Random;

public class FooResponder implements Responder {
    @Override
    public String getHello() {
        return "qweqwe" + new Random().nextInt();
    }
}
