package org.aldeon.utils.json.adapters;


import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provider;
import org.aldeon.utils.base64.Base64;
import org.aldeon.utils.base64.Base64Module;
import org.aldeon.utils.json.GsonBasedJsonParser;
import org.aldeon.utils.json.JsonParser;

public class JsonModule extends AbstractModule implements Provider<JsonParser> {

    @Override
    protected void configure() {
        bind(JsonParser.class).to(GsonBasedJsonParser.class);
        bind(Base64.class).toProvider(Base64Module.class);
    }

    @Override
    public JsonParser get() {
        return Guice.createInjector(this).getInstance(JsonParser.class);
    }
}
