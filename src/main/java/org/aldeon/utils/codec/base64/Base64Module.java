package org.aldeon.utils.codec.base64;


import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import org.aldeon.utils.codec.Codec;

public class Base64Module extends AbstractModule implements Provider<Codec> {

    @Override
    protected void configure() {

    }

    @Override
    public Codec get() {
        return new MiGBase64();
    }
}
