package org.aldeon.communication.netty.receiver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.aldeon.communication.task.InboundRequestTask;
import org.aldeon.events.Callback;
import org.aldeon.net.IpPeerAddress;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceiverHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger log = LoggerFactory.getLogger(ReceiverHandler.class);

    private final Converter<FullHttpRequest, Request> decoder;
    private final Converter<Response, FullHttpResponse> encoder;
    private final Callback<InboundRequestTask> callback;

    public ReceiverHandler(
            Converter<FullHttpRequest, Request> decoder,
            Converter<Response, FullHttpResponse> encoder,
            Callback<InboundRequestTask> callback
    ) {
        this.decoder = decoder;
        this.encoder = encoder;
        this.callback = callback;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        if(callback != null) {
            if(msg.getDecoderResult().isSuccess()) {
                try {
                    Request req = decoder.convert(msg);
                    //TODO: implement proper address
                    final Task t = new Task(ctx, req, null, encoder);
                    log.info("Dispatched a task to handle request " + req);
                    callback.call(t);

                } catch (ConversionException e) {
                    log.info("Invalid request received, writing (400) BAD REQUEST", e);
                    writeBadRequest(ctx);
                }
            } else {
                log.info("Could not decode incoming data into a valid HttpRequest, writing (400) BAD REQUEST");
                writeBadRequest(ctx);
            }
        } else {
            log.warn("No callback available for incoming request, writing (500) SERVER ERROR");
            writeServerError(ctx);
        }
    }
    private static void writeResponse(ChannelHandlerContext ctx, FullHttpResponse response) {
        ctx.write(response);
        ctx.flush();
        ctx.close();
    }

    private static void writeContent(ChannelHandlerContext ctx, HttpResponseStatus status, ByteBuf content) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
        writeResponse(ctx, response);
    }

    private static void writeEmptyResponse(ChannelHandlerContext ctx, HttpResponseStatus status) {
        writeContent(ctx, status, Unpooled.buffer(0));
    }

    private static void writeBadRequest(ChannelHandlerContext ctx) {
        writeEmptyResponse(ctx, HttpResponseStatus.BAD_REQUEST);
    }

    private static void writeServerError(ChannelHandlerContext ctx) {
        writeEmptyResponse(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
    }

    private static class Task implements InboundRequestTask {

        private final ChannelHandlerContext ctx;
        private final Converter<Response, FullHttpResponse> encoder;
        private final Request request;
        private final IpPeerAddress address;
        private boolean responseSent = false;

        public Task(ChannelHandlerContext ctx, Request request, IpPeerAddress address, Converter<Response, FullHttpResponse> encoder) {
            this.ctx = ctx;
            this.request = request;
            this.encoder = encoder;
            this.address = address;
        }

        @Override
        public Request getRequest() {
            return request;
        }

        @Override
        public IpPeerAddress getAddress() {
            return address;
        }

        @Override
        public void sendResponse(Response response) {
            try {
                writeResponse(ctx, encoder.convert(response));
            } catch (ConversionException e) {
                log.warn("Could not parse the response", e);
                writeServerError(ctx);
            }
            responseSent = true;
        }

        @Override
        public boolean responseSent() {
            return responseSent;
        }

        @Override
        public void discard() {
            ctx.close();
        }
    }
}
