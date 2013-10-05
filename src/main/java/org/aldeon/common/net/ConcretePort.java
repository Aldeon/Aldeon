package org.aldeon.common.net;

public class ConcretePort implements Port {

    private int value;

    public ConcretePort(int value) {
        if(value > 65535) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    @Override
    public int getIntValue() {
        return value;
    }
}
