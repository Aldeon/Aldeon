package org.aldeon.communication.netty.sender;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.aldeon.communication.Sender;
import org.aldeon.communication.task.OutboundRequestTask;
import org.aldeon.net.AddressType;
import org.aldeon.net.IpPeerAddress;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;
import org.aldeon.utils.conversion.Converter;
import java.util.concurrent.CancellationException;

public class NettySender implements Sender {

    private Bootstrap bootstrap;
    private EventLoopGroup group;
    private Converter<Request, FullHttpRequest> encoder;

    public NettySender(
            Converter<Request, FullHttpRequest> encoder,
            final Converter<FullHttpResponse, Response> decoder
    ) {
        this.encoder = encoder;

        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup();

        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast("log",        new LoggingHandler(LogLevel.INFO));
                p.addLast("codec",      new HttpClientCodec());
                p.addLast("aggregator", new HttpObjectAggregator(1024 * 1024));
                p.addLast("handler",    new SenderHandler(decoder));
            }
        });
    }

    @Override
    public void start() {
        // nothing
    }

    @Override
    public void close() {
        group.shutdownGracefully();
    }

    @Override
    public void addTask(final OutboundRequestTask task) {

        PeerAddress addr = task.getAddress();

        if(getAcceptedType().equals(addr.getType())) {

            IpPeerAddress address = (IpPeerAddress) addr;

            try {
                FullHttpRequest request = encoder.convert(task.getRequest());

                request.headers().set(HttpHeaders.Names.HOST, address.getHost().getHostAddress());

                final ChannelFuture cf = bootstrap.connect(
                        address.getHost(),
                        address.getPort().getIntValue()
                );

                cf.channel().config().setConnectTimeoutMillis(task.getTimeoutMillis());
                cf.addListener(new ConnectionListener(task, request));

            } catch (Exception e) {
                task.onFailure(e);
            }
        } else {
            throw new IllegalArgumentException("NettySender accepts only IpPeerAddress-based address types");
        }
    }

    @Override
    public AddressType getAcceptedType() {
        return AddressType.IPV4;
    }

    private static class ConnectionListener implements ChannelFutureListener {
        private final OutboundRequestTask task;
        private final FullHttpRequest request;

        public ConnectionListener(
                OutboundRequestTask task,
                FullHttpRequest request
        ) {
            this.task = task;
            this.request = request;
        }

        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if(future.isSuccess()) {
                Channel ch = future.channel();
                SenderHandler sh = (SenderHandler) ch.pipeline().get("handler");
                sh.setTask(task);
                ch.writeAndFlush(request);
            } else {

                final Throwable cause = future.isCancelled()
                        ? new CancellationException("Connection cancelled")
                        : future.cause();

                task.onFailure(cause);
            }
        }
    }
}
