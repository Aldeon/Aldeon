package org.aldeon.utils.base64;


import com.google.inject.AbstractModule;
import com.google.inject.Provider;

public class Base64Module extends AbstractModule implements Provider<Base64> {

    @Override
    protected void configure() {

    }

    @Override
    public Base64 get() {
        return new Base64CharReplacer(new MiGBase64());
    }
}
