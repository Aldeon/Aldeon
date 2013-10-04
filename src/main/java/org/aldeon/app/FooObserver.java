package org.aldeon.app;

import org.aldeon.common.Observer;

import java.util.Random;

public class FooObserver implements Observer {
    @Override
    public int getSomeTestStatusInt() {
        return 42;
    }
}
