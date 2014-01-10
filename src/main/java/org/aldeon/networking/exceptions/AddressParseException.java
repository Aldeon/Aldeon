package org.aldeon.networking.exceptions;

public class AddressParseException extends Exception {
    public AddressParseException(String s) {
        super(s);
    }

    public AddressParseException(String s, Exception e) {
        super(s, e);
    }
}
