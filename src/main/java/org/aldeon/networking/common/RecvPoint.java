package org.aldeon.networking.common;

import org.aldeon.events.Callback;

import java.nio.ByteBuffer;

public interface RecvPoint extends Service {

    void onIncomingTransmission(Callback<IncomingTransmission> callback);

    public static interface IncomingTransmission extends Transmission {
        void respond(ByteBuffer buffer);
        void badRequest();
        void serverError();
        void discard();
    }
}
