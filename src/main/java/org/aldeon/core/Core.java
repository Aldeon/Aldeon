package org.aldeon.core;

import org.aldeon.db.Db;
import org.aldeon.dht.Dht;
import org.aldeon.events.EventLoop;
import org.aldeon.networking.common.Receiver;
import org.aldeon.networking.common.Sender;
import org.aldeon.sync.TopicManager;
import java.util.concurrent.Executor;

public interface Core {

    /**
     * Returns the Dht that accepts all used address types.
     * @return
     */
    Dht getDht();

    /**
     * Returns the global message storage class.
     * @return
     */
    Db getStorage();

    /**
     * Returns the main event loop used to communicate between UI and program logic
     * @return
     */
    EventLoop getEventLoop();

    /**
     * Executor. Should be used to handle client-related tasks
     * @return
     */
    Executor serverSideExecutor();

    /**
     * Executor. Should be used to handle server-related tasks.
     * @return
     */
    Executor clientSideExecutor();


    /**
     * Manages user identities
     * @return
     */
    UserManager getUserManager();

    /**
     * Sender wrapper - analyses the address and forwards the request to appropriate sender on the fly.
     * @return
     */
    Sender getSender();

    /**
     * Receiver wrapper
     * @return
     */
    Receiver getReceiver();

    /**
     *
     */
    TopicManager getTopicManager();
}
