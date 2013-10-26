package org.aldeon.netty.receiver;

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
import org.aldeon.common.nio.task.InboundRequestTask;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;
import org.aldeon.utils.various.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

public class ReceiverHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger log = LoggerFactory.getLogger(ReceiverHandler.class);

    private final Converter<FullHttpRequest, Request> decoder;
    private final Converter<Response, FullHttpResponse> encoder;
    private final Executor executor;
    private final Callback<InboundRequestTask> callback;

    public ReceiverHandler(
            Converter<FullHttpRequest, Request> decoder,
            Converter<Response, FullHttpResponse> encoder,
            Executor executor,
            Callback<InboundRequestTask> callback
    ) {
        this.decoder = decoder;
        this.encoder = encoder;
        this.executor = executor;
        this.callback = callback;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        if(callback != null && executor != null) {
            if(msg.getDecoderResult().isSuccess()) {
                try {
                    Request req = decoder.convert(msg);
                    final Task t = new Task(ctx, executor, req, encoder);
                    executor.execute(new Runnable(){
                        @Override
                        public void run() {
                            callback.call(t);
                        }
                    });
                } catch (ConversionException e) {
                    writeBadRequest(ctx);
                }
            } else {
                writeBadRequest(ctx);
            }
        } else {
            log.warn("No callback or executor available for incoming request");
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
        private final Executor executor;
        private boolean responseSent = false;

        public Task(ChannelHandlerContext ctx, Executor executor, Request request, Converter<Response, FullHttpResponse> encoder) {
            this.ctx = ctx;
            this.request = request;
            this.encoder = encoder;
            this.executor = executor;
        }

        @Override
        public Request getRequest() {
            return request;
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

        @Override
        public Executor getExecutor() {
            return executor;
        }
    }
}
