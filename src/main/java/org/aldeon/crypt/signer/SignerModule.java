package org.aldeon.crypt.signer;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import org.aldeon.crypt.Hash;
import org.aldeon.crypt.Signer;
import org.aldeon.utils.various.Provider;


public class SignerModule extends AbstractModule implements Provider<Signer> {

    @Override
    protected void configure() {
        bind(Signer.class).to(HashBasedSigner.class);
        bind(Hash.class).to(Sha256.class);
    }

    @Override
    public Signer get() {
        return Guice.createInjector(this).getInstance(Signer.class);
    }
}
