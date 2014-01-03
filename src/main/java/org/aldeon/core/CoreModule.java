package org.aldeon.core;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provider;
import org.aldeon.db.Db;
import org.aldeon.db.DbImpl;
import org.aldeon.events.EventLoop;
import org.aldeon.events.MultiMapBasedEventLoop;
import org.aldeon.networking.NetworkService;
import org.aldeon.networking.NetworkingModule;

public class CoreModule extends AbstractModule implements Provider<Core> {

    private static Core coreInstance;
    private static boolean initializing = false;

    @Override
    protected void configure() {
        bind(Core.class).to(AldeonCore.class);
        bind(Db.class).to(DbImpl.class);
        bind(EventLoop.class).to(MultiMapBasedEventLoop.class);
        bind(NetworkService.class).toProvider(NetworkingModule.class);
    }

    @Override
    public Core get() {
        if(coreInstance == null) {
            if(initializing) {
                throw new IllegalStateException("Core is already being initialized");
            } else {
                initializing = true;
                coreInstance = Guice.createInjector(new CoreModule()).getInstance(Core.class);
                initializing = false;
            }
        }
        return coreInstance;
    }

    /**
     * Singleton core getter
     * @return
     */
    public static Core getInstance() {
        return new CoreModule().get();
    }

}
