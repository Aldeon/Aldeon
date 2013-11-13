package org.aldeon.core;

import org.aldeon.communication.Receiver;
import org.aldeon.communication.Sender;
import org.aldeon.db.Db;
import org.aldeon.dht.Dht;
import org.aldeon.dht.InterestTracker;
import org.aldeon.events.EventLoop;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.Protocol;

import java.util.concurrent.Executor;

public interface Core {

    /**
     * Find the appropriate Dht for a given address type.
     * @param addressType
     * @param <T>
     * @return
     */
    <T extends PeerAddress> Dht<T> getDht(Class<T> addressType);

    /**
     * TODO: make comment
     * @return
     */
    InterestTracker getInterestTracker();

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
     * Executor. Should be used to handle client-related tasks
     * @return
     */
    Executor serverSideExecutor();

    /**
     * Executor. Should be used to handle server-related tasks.
     * @return
     */
    Executor clientSideExecutor();

    <T extends PeerAddress> void registerSender(Class<T> addressType, Sender<T> sender);
    <T extends PeerAddress> void registerReceiver(Class<T> addressType, Receiver<T> receiver);

    <T extends PeerAddress> Sender<T> getSender(Class<T> addressType);
    <T extends PeerAddress> Receiver<T> getReceiver(Class<T> addressType);

    /**
     * Initializes all the senders.
     */
    void initSenders();

    /**
     * Initializes all the receivers, sets them to return answers according to a specified protocol.
     * @param protocol
     */
    void initReceivers(Protocol protocol);
}
