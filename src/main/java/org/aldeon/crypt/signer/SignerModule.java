package org.aldeon.crypt.signer;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.TypeLiteral;
import org.aldeon.utils.collections.Provider;
import org.aldeon.utils.math.ByteBufferArithmetic;

import java.nio.ByteBuffer;
import java.util.Comparator;


public class SignerModule extends AbstractModule implements Provider<Signer> {

    @Override
    protected void configure() {
        bind(Signer.class).to(HashBasedSigner.class);
        bind(Hash.class).to(Sha256.class);
        bind(new TypeLiteral<Comparator<ByteBuffer>>(){}).to(ByteBufferArithmetic.class);
    }

    @Override
    public Signer get() {
        return Guice.createInjector(this).getInstance(Signer.class);
    }
}
