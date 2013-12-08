package org.aldeon.networking.wrappers;

import org.aldeon.communication.Receiver;
import org.aldeon.communication.task.InboundRequestTask;
import org.aldeon.events.Callback;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.common.RecvPoint;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class RecvPointBasedReceiver implements Receiver {

    private static final Logger log = LoggerFactory.getLogger(RecvPointBasedReceiver.class);

    private final RecvPoint point;
    private Callback<InboundRequestTask> callback;

    public RecvPointBasedReceiver(RecvPoint recvPoint, final Converter<ByteBuffer, Request> decoder, final Converter<Response, ByteBuffer> encoder) {

        this.point = recvPoint;

        this.point.onIncomingTransmission(new Callback<RecvPoint.IncomingTransmission>() {
            @Override
            public void call(final RecvPoint.IncomingTransmission transmission) {
                try {
                    final Request request = decoder.convert(transmission.data());

                    getCallback().call(new InboundRequestTask() {

                        private boolean responseSent = false;

                        @Override
                        public void sendResponse(Response response) {
                            if(!responseSent) {
                                if(response == null) {
                                    log.error("Tried to send a null response");
                                    transmission.serverError();
                                } else {
                                    try {
                                        ByteBuffer responseBuffer = encoder.convert(response);
                                        transmission.respond(responseBuffer);
                                    } catch (ConversionException e) {
                                        log.error("Failed to convert a response into ByteBuffer", e);
                                        transmission.serverError();
                                    }
                                }
                                responseSent = true;
                            }
                        }

                        @Override
                        public boolean responseSent() {
                            return responseSent;
                        }

                        @Override
                        public void discard() {
                            if(!responseSent) {
                                transmission.discard();
                                responseSent = true;
                            }
                        }

                        @Override
                        public Request getRequest() {
                            return request;
                        }

                        @Override
                        public PeerAddress getAddress() {
                            return transmission.address();
                        }
                    });

                } catch (ConversionException e) {
                    log.info("Failed to parse incoming request", e);
                    transmission.badRequest();
                }
            }
        });
    }

    protected Callback<InboundRequestTask> getCallback() {
        return callback;
    }

    @Override
    public void setCallback(Callback<InboundRequestTask> callback) {
        this.callback = callback;
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
