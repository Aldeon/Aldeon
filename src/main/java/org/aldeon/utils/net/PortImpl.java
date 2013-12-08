package org.aldeon.utils.net;

import org.aldeon.networking.common.Port;

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
