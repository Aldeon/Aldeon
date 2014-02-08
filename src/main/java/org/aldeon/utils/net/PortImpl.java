package org.aldeon.utils.net;

import org.aldeon.networking.common.Port;

public class PortImpl implements Port {

    private final int hashCode;
    private int value;

    public PortImpl(int value) {
        if(value > 65535 || value < 0) {
            throw new IllegalArgumentException("Invalid port value (" + value + "), must be in range <0, 65535>");
        }
        this.value = value;
        this.hashCode = new Integer(value).hashCode();
    }

    @Override
    public int getIntValue() {
        return value;
    }

    @Override
    public String toString() {
        return "" + getIntValue();
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    public boolean equals(Object obj) {
        if(obj instanceof Port) {
            return ((Port) obj).getIntValue() == getIntValue();
        }
        return false;
    }
}
