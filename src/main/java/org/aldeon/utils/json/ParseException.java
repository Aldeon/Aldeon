package org.aldeon.utils.json;

public class ParseException extends Exception {

    public ParseException(String s, Exception e) {
        super(s,e);
    }

    public ParseException() {
        super();
    }
}
