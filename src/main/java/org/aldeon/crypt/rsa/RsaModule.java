package org.aldeon.crypt.rsa;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provider;
import org.aldeon.crypt.KeyGen;

public class RsaModule extends AbstractModule implements Provider<KeyGen> {
    @Override
    protected void configure() {
        bind(KeyGen.class).to(RsaKeyGen.class);
    }

    @Override
    public KeyGen get() {
        return Guice.createInjector(this).getInstance(KeyGen.class);
    }
}
