package org.aldeon.networking.mediums.ip.sender;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.common.SendPoint;
import org.aldeon.networking.exceptions.UnexpectedAddressClassException;
import org.aldeon.networking.mediums.ip.addresses.IpPeerAddress;
import org.aldeon.networking.mediums.ip.conversion.ByteBufferToFullHttpRequestConverter;
import org.aldeon.utils.conversion.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.concurrent.CancellationException;


public class NettySendPoint implements SendPoint {

    private static final Logger log = LoggerFactory.getLogger(NettySendPoint.class);

    private static Converter<ByteBuffer, FullHttpRequest> encoder = new ByteBufferToFullHttpRequestConverter();

    private Bootstrap bootstrap;
    private EventLoopGroup group;

    public NettySendPoint() {

        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup();

        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                // p.addLast("log",        new LoggingHandler(LogLevel.INFO));
                p.addLast("codec",      new HttpClientCodec());
                p.addLast("aggregator", new HttpObjectAggregator(1024 * 1024));
                p.addLast("handler",    new NettySenderHandler());
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
    public void send(OutgoingTransmission task) throws UnexpectedAddressClassException {

        PeerAddress addr = task.address();

        if(addr instanceof IpPeerAddress) {

            IpPeerAddress address = (IpPeerAddress) addr;

            try {
                FullHttpRequest request = encoder.convert(task.data());

                request.headers().set(HttpHeaders.Names.HOST, address.getHost().getHostAddress());

                final ChannelFuture cf = bootstrap.connect(
                        address.getHost(),
                        address.getPort().getIntValue()
                );

                cf.channel().config().setConnectTimeoutMillis(task.timeout());
                cf.addListener(new ConnectionListener(task, request));

            } catch (Exception e) {
                task.onFailure(e);
            }
        } else {
            throw new UnexpectedAddressClassException();
        }
    }

    private static class ConnectionListener implements ChannelFutureListener {
        private final OutgoingTransmission task;
        private final FullHttpRequest request;

        public ConnectionListener(
                OutgoingTransmission task,
                FullHttpRequest request
        ) {
            this.task = task;
            this.request = request;
        }

        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if(future.isSuccess()) {
                Channel ch = future.channel();
                NettySenderHandler sh = (NettySenderHandler) ch.pipeline().get("handler");
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
