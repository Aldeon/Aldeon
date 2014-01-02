package org.aldeon.utils.various;

public class SystemTimeProvider implements Provider<Long> {
    @Override
    public Long get() {
        return System.currentTimeMillis();
    }
}
