package org.aldeon.networking.wrappers;

import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.OutboundRequestTask;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.common.SendPoint;
import org.aldeon.networking.common.Sender;
import org.aldeon.networking.exceptions.UnexpectedAddressClassException;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class SendPointBasedSender implements Sender {

    private static final Logger log = LoggerFactory.getLogger(SendPointBasedSender.class);

    private final SendPoint point;
    private final Converter<Request, ByteBuffer> encoder;
    private final Converter<ByteBuffer, Response> decoder;
    private final Set<AddressType> acceptedTypes;


    public SendPointBasedSender(SendPoint point, Set<AddressType> acceptedTypes, Converter<Request, ByteBuffer> encoder, Converter<ByteBuffer, Response> decoder) {
        this.point = point;
        this.acceptedTypes = acceptedTypes;
        this.encoder = encoder;
        this.decoder = decoder;
    }

    @Override
    public void addTask(final OutboundRequestTask task) {
        log.info("Sending request: " + task.getRequest());
        try {
            final ByteBuffer buf = encoder.convert(task.getRequest());
            point.send(new SendPoint.OutgoingTransmission() {

                private AtomicInteger callCount = new AtomicInteger(0);

                @Override
                public void onSuccess(ByteBuffer data) {
                    markAsDone();
                    try {
                        Response response = decoder.convert(data);
                        log.info("Received response " + response);
                        task.onSuccess(response);
                    } catch (ConversionException e) {
                        task.onFailure(e);
                    }
                }

                @Override
                public void onFailure(Throwable cause) {
                    markAsDone();
                    //log.warn("Sender failure", cause);
                    log.warn("Sender failure: " + cause);
                    task.onFailure(cause);
                }

                @Override
                public int timeout() {
                    return task.getTimeoutMillis();
                }

                @Override
                public PeerAddress address() {
                    return task.getAddress();
                }

                @Override
                public ByteBuffer data() {
                    return buf;
                }

                private void markAsDone() {
                    if(callCount.incrementAndGet() > 1) {
                        throw new IllegalStateException("Task result called more than once");
                    }
                }
            });
        } catch (UnexpectedAddressClassException e) {
            task.onFailure(e);
        } catch (ConversionException e) {
            task.onFailure(e);
        }
    }

    @Override
    public Set<AddressType> acceptedTypes() {
        return acceptedTypes;
    }

    @Override
    public void start() {
        point.start();
    }

    @Override
    public void close() {
        point.close();
    }
}
