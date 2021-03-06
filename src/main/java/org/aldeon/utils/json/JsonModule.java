package org.aldeon.utils.json;


import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provider;
import org.aldeon.crypt.KeyGen;
import org.aldeon.crypt.rsa.RsaModule;
import org.aldeon.utils.codec.Codec;
import org.aldeon.utils.codec.base64.Base64Module;
import org.aldeon.utils.json.GsonBasedJsonParser;
import org.aldeon.utils.json.JsonParser;

public class JsonModule extends AbstractModule implements Provider<JsonParser> {

    @Override
    protected void configure() {
        bind(JsonParser.class).to(GsonBasedJsonParser.class);
        bind(Codec.class).toProvider(Base64Module.class);
        bind(KeyGen.class).toProvider(RsaModule.class);
    }

    @Override
    public JsonParser get() {
        return Guice.createInjector(this).getInstance(JsonParser.class);
    }
}
