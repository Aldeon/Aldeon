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
    public boolean equals(Object obj) {
        if(obj instanceof AddressType) {
            return ((AddressType) obj).getName() == getName();
        } else {
            return false;
        }
    }
}
