package org.aldeon.common.core;

import org.aldeon.common.events.EventLoop;
import org.aldeon.common.model.Storage;
import org.aldeon.common.net.address.PeerAddress;
import org.aldeon.common.communication.Receiver;
import org.aldeon.common.communication.Sender;
import org.aldeon.common.protocol.Protocol;

import java.util.concurrent.Executor;

public interface Core {
    /**
     * Returns the global message storace class.
     * @return
     */
    Storage getStorage();

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
