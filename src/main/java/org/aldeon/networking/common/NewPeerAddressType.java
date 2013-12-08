package org.aldeon.networking.common;

public class NewPeerAddressType {

    private final String singletonName;

    public NewPeerAddressType(String name) {
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
        if(obj instanceof NewPeerAddressType) {
            return ((NewPeerAddressType) obj).getName() == getName();
        } else {
            return false;
        }
    }
}
