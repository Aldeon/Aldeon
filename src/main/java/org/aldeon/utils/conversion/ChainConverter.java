package org.aldeon.utils.conversion;

public class ChainConverter<P,Q,R> implements Converter<P,Q> {

    private Converter<P,R> a;
    private Converter<R,Q> b;

    public ChainConverter(Converter<P, R> a, Converter<R, Q> b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Q convert(P val) throws ConversionException {
        return b.convert(a.convert(val));
    }
}
