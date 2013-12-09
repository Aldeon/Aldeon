package org.aldeon.utils.various;

import org.aldeon.utils.various.Reducer;

public class BooleanAndReducer implements Reducer<Boolean> {
    @Override
    public Boolean reduce(Boolean a, Boolean b) {
        return a && b;
    }
}
