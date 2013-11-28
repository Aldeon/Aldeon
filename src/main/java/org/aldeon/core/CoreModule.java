package org.aldeon.core;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.aldeon.communication.netty.NettyModule;
import org.aldeon.db.Db;
import org.aldeon.db.DbImpl;
import org.aldeon.dbstub.DbStubModule;
import org.aldeon.events.EventLoop;
import org.aldeon.events.EventLoopImpl;
import org.aldeon.nat.utils.NoAddressTranslation;
import org.aldeon.net.AddressTranslation;
import org.aldeon.net.IpPeerAddress;
import org.aldeon.utils.net.PortImpl;

import java.net.InetAddress;
import java.net.UnknownHostException;

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

        return core;
    }

    /**
     * Perform the networking-related tasks
     * @param core
     */
    private static void setupNetworking(Core core) {
        try {
            // Decide how to translate addresses (DEBUG: UPnP disabled for now)
            AddressTranslation translation;

            translation = new NoAddressTranslation(new PortImpl(8080), InetAddress.getByName("0.0.0.0"));

            // Register all senders and receivers we have implemented
            core.registerSender(IpPeerAddress.class, NettyModule.createSender());
            core.registerReceiver(IpPeerAddress.class, NettyModule.createReceiver(translation));

        } catch (UnknownHostException e) {
            e.printStackTrace();// This should never happen
        }
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
}
