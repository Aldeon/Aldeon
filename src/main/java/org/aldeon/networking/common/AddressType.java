package org.aldeon.networking.common;

public class AddressType {

    private final String singletonName;

    public AddressType(String name) {
        if(name == null) {
            throw  new IllegalArgumentException();
        }
        this.singletonName = name;
    }

    public final String getName() {
        return singletonName;
    }

    @Override
    public final boolean equals(Object obj) {
        if(obj instanceof AddressType) {
            return ((AddressType) obj).getName().equals(getName());
        } else {
            return false;
        }
    }

    @Override
    public final int hashCode() {
        return getName().hashCode();
    }

    @Override
    public String toString() {
        return getName();
    }
}
