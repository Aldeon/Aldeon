package org.aldeon.networking.common;

import org.aldeon.communication.Service;
import org.aldeon.events.Callback;

import java.nio.ByteBuffer;

public interface SendPoint extends Service {
    void send(NewPeerAddress address, ByteBuffer data, Callback<ByteBuffer> onSuccess, Callback<Throwable> onFailure);

    public static interface OutgoingTransmission extends Transmission {
        Callback<ByteBuffer> onSuccess();
        Callback<Throwable> onFailure();
    }
}
