package org.aldeon.networking.mediums.ip.receiver;

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
import org.aldeon.events.Callback;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.common.RecvPoint;
import org.aldeon.networking.mediums.ip.addresses.IpPeerAddress;
import org.aldeon.networking.mediums.ip.conversion.FullHttpRequestToByteBufferConverter;
import org.aldeon.utils.conversion.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

public class NewReceiverHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger log = LoggerFactory.getLogger(NewReceiverHandler.class);
    private static final Converter<FullHttpRequest, ByteBuffer> decoder = new FullHttpRequestToByteBufferConverter();

    private final Callback<RecvPoint.IncomingTransmission> callback;

    public NewReceiverHandler(Callback<RecvPoint.IncomingTransmission> callback) {
        this.callback = callback;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        if(callback != null) {
            if(msg.getDecoderResult().isSuccess()) {

                // Try to obtain the remote address
                SocketAddress socketAddress = ctx.channel().remoteAddress();

                if(socketAddress instanceof InetSocketAddress) {
                    InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
                    IpPeerAddress peerAddress = IpPeerAddress.create(inetSocketAddress.getAddress(), inetSocketAddress.getPort());

                    ByteBuffer data = decoder.convert(msg);

                    final Task t = new Task(ctx, data, peerAddress);
                    callback.call(t);

                } else {
                    // This should never happen
                    log.info("Bad remote address socket type");
                    writeServerError(ctx);
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

    private static void writeContent(ChannelHandlerContext ctx, HttpResponseStatus status, ByteBuf content) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/json; charset=UTF-8");
        ctx.write(response);
        ctx.flush();
        ctx.close();
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

    private static class Task implements RecvPoint.IncomingTransmission {

        private final ChannelHandlerContext ctx;
        private final ByteBuffer data;
        private final IpPeerAddress address;
        private boolean responseSent = false;

        public Task(ChannelHandlerContext ctx, ByteBuffer data, IpPeerAddress address) {
            this.ctx = ctx;
            this.data = data;
            this.address = address;
        }

        @Override
        public void respond(ByteBuffer response) {
            if(!responseSent) {
                if(response != null) {
                    writeContent(ctx, HttpResponseStatus.OK, Unpooled.copiedBuffer(response));
                } else {
                    log.warn("Failed to generate a valid response (null)");
                    writeServerError(ctx);
                }
                responseSent = true;
            }
        }

        @Override
        public void badRequest() {
            if(!responseSent) {
                writeBadRequest(ctx);
                responseSent = true;
            }
        }

        @Override
        public void serverError() {
            if(!responseSent) {
                writeServerError(ctx);
                responseSent = true;
            }
        }

        @Override
        public void discard() {
            if(!responseSent) {
                ctx.close();
                responseSent = true;
            }
        }

        @Override
        public PeerAddress address() {
            return address;
        }

        @Override
        public ByteBuffer data() {
            return data.asReadOnlyBuffer();
        }
    }
}
