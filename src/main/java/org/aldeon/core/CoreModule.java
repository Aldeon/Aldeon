package org.aldeon.core;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.aldeon.communication.netty.NettyModule;
import org.aldeon.core.events.TopicAddedEvent;
import org.aldeon.db.Db;
import org.aldeon.db.DbImpl;
import org.aldeon.events.AsyncCallback;
import org.aldeon.events.Callback;
import org.aldeon.events.CallbackAndExecutor;
import org.aldeon.events.EventLoop;
import org.aldeon.events.EventLoopImpl;
import org.aldeon.nat.utils.NoAddressTranslation;
import org.aldeon.net.AddressTranslation;
import org.aldeon.net.IpPeerAddress;
import org.aldeon.utils.net.PortImpl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executor;

public class CoreModule extends AbstractModule {

    private static Core core;

    @Override
    protected void configure() {
        bind(Core.class).to(CoreImpl.class);
        bind(Db.class).to(DbImpl.class);
        bind(EventLoop.class).to(EventLoopImpl.class);
    }

    /**
     * Creates a default core instance.
     * @return
     */
    private static Core createCore() {
        Injector injector = Guice.createInjector(new CoreModule());
        Core core = injector.getInstance(Core.class);

        setupNetworking(core);
        setupCallbacks(core);

        return core;
    }

    /**
     * Perform the networking-related tasks
     * @param core
     */
    private static void setupNetworking(Core core) {
        try {
            // Decide how to translate addresses (DEBUG: UPnP disabled for now)
            AddressTranslation translation = null;

            translation = new NoAddressTranslation(new PortImpl(8080), InetAddress.getByName("0.0.0.0"));

            // Register all senders and receivers we have implemented
            core.registerSender(IpPeerAddress.class, NettyModule.createSender());
            core.registerReceiver(IpPeerAddress.class, NettyModule.createReceiver(translation));

        } catch (UnknownHostException e) {
            e.printStackTrace();// This should never happen
        }
    }

    /**
     * Register the callbacks
     * @param core
     */
    private static void setupCallbacks(Core core) {

        // For client-related tasks we use a clientSideExecutor
        Executor cse = core.clientSideExecutor();

        /*
            Here go all the callbacks we need
         */

        // topic added
        core.getEventLoop().assign(TopicAddedEvent.class, async(new TopicAddedCallback(core), cse));
        // user added
        // topic removed
        // settings updated
        // lots of other shizzle-wizzle
    }

    /**
     * Singleton core getter
     * @return
     */
    public static Core getInstance() {
        if(core == null) {
            setInstance(createCore());
        }
        return core;
    }

    /**
     * Singleton core setter
     * @param core
     */
    public static void setInstance(Core core) {
        CoreModule.core = core;
    }

    // --- helper methods -------------------------------------

    private static <T> AsyncCallback<T> async(Callback<T> callback, Executor executor) {
        return new CallbackAndExecutor<T>(callback, executor);
    }
}
