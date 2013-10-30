package org.aldeon.core;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.aldeon.common.core.Core;
import org.aldeon.common.events.EventLoop;
import org.aldeon.common.model.Storage;
import org.aldeon.db.DbStub;

public class CoreModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Core.class).to(CoreImpl.class);
        bind(Storage.class).to(DbStub.class);
        bind(EventLoop.class).to(EventLoopImpl.class);
    }

    public static Core createCore() {
        Injector injector = Guice.createInjector(new CoreModule());
        Core core = injector.getInstance(Core.class);
        return core;
    }
}
