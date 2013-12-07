package org.aldeon.core;

import org.aldeon.communication.Sender;
import org.aldeon.db.Db;
import org.aldeon.dht.Dht;
import org.aldeon.events.EventLoop;
import org.aldeon.model.Identity;
import org.aldeon.net.AddressType;
import org.aldeon.sync.TopicManager;

import java.util.Set;
import java.util.concurrent.Executor;

public interface Core {

    /**
     * Find the appropriate Ring for a given address type.
     * @param type
     * @return
     */
    Dht getDht(AddressType type);

    /**
     * Returns the global message storace class.
     * @return
     */
    Db getStorage();

    /**
     * Returns the main event loop used to communicate between UI and program logic
     * @return
     */
    EventLoop getEventLoop();

    /**
     * Returns all locally stored identites
     * @return
     */
    Set<Identity> getAllIdentities();

    /**
     *
     */
    void addIdentity(Identity identity);

    /**
     *
     */
    void delIdentity(Identity identity);

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
     * Sender wrapper - analyses the address and forwards the request to appropriate subsender.
     * @return
     */
    Sender getSender();

    /**
     *
     */
    TopicManager getTopicManager();
}
