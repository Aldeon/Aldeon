package org.aldeon.networking.common;

import org.aldeon.core.services.Service;
import org.aldeon.networking.exceptions.UnexpectedAddressClassException;

import java.nio.ByteBuffer;

public interface SendPoint extends Service {
    void send(OutgoingTransmission transmission) throws UnexpectedAddressClassException;

    public static interface OutgoingTransmission extends Transmission {
        void onSuccess(ByteBuffer data);
        void onFailure(Throwable cause);
        int timeout();
    }
}
