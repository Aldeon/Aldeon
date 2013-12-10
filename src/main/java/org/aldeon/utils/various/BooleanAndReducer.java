package org.aldeon.utils.various;

public class BooleanAndReducer implements Reducer<Boolean> {
    @Override
    public Boolean reduce(Boolean a, Boolean b) {
        return a && b;
    }
}
