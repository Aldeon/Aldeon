package org.aldeon.utils.net;

import org.aldeon.net.Port;

public class PortImpl implements Port {

    private int value;

    public PortImpl(int value) {
        if(value > 65535) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    @Override
    public int getIntValue() {
        return value;
    }

    @Override
    public String toString() {
        return "" + getIntValue();
    }
}