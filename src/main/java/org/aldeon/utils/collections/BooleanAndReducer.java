package org.aldeon.utils.collections;

public class BooleanAndReducer implements Reducer<Boolean> {
    @Override
    public Boolean reduce(Boolean a, Boolean b) {
        return a && b;
    }
}
