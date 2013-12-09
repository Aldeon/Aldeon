package org.aldeon.networking.wrappers;

import org.aldeon.events.Callback;
import org.aldeon.networking.common.InboundRequestTask;
import org.aldeon.networking.common.Receiver;

import java.util.Set;

public class ReceiverDisptcher implements Receiver {

    private final Set<Receiver> receivers;

    public ReceiverDisptcher(Set<Receiver> receivers) {
        this.receivers = receivers;
    }

    @Override
    public void setCallback(Callback<InboundRequestTask> callback) {
        for(Receiver receiver: receivers) {
            receiver.setCallback(callback);
        }
    }

    @Override
    public void start() {
        for(Receiver receiver: receivers) {
            receiver.start();
        }
    }

    @Override
    public void close() {
        for(Receiver receiver: receivers) {
            receiver.close();
        }
    }
}
