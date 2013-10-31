package org.aldeon.utils.conversion;

/**
 * Provides a way of converting one type into another.
 * @param <P> source type
 * @param <Q> destination type
 */
public interface Converter<P,Q> {
    /**
     * Conversion function
     * @param val
     * @return
     * @throws ConversionException thrown if conversion fails
     */
    Q convert(P val) throws ConversionException;
}
