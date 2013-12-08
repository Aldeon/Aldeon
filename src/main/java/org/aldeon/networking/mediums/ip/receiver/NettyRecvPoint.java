package org.aldeon.networking.mediums.ip.receiver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.aldeon.events.Callback;
import org.aldeon.networking.common.RecvPoint;
import org.aldeon.networking.mediums.ip.addresses.IpPeerAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class NettyRecvPoint implements RecvPoint {

    private static final Logger log = LoggerFactory.getLogger(NettyRecvPoint.class);

    private ServerBootstrap server;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;
    private Callback<IncomingTransmission> callback = null;
    private IpPeerAddress connectionAddress;

    public NettyRecvPoint(IpPeerAddress connectionAddress) {

        this.connectionAddress = connectionAddress;

        bossGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup();

        server = new ServerBootstrap();
        server.group(bossGroup, workGroup);
        server.channel(NioServerSocketChannel.class);
        server.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();

                // logging
                p.addLast("log",        new LoggingHandler(LogLevel.INFO));

                // inbound traffic
                p.addLast("decoder",    new HttpRequestDecoder());
                p.addLast("aggregator", new HttpObjectAggregator(1024 * 1024));

                //TODO: addTask timeout mechanism

                // outbound traffic
                p.addLast("encoder",    new HttpResponseEncoder());

                // request handling
                p.addLast("handler",    new NewReceiverHandler(callback));

                log.info("Created a pipeline for incoming request");
            }
        });
    }

    @Override
    public void start() {
        log.info("Starting netty server on " + connectionAddress.getHost() + ":" + connectionAddress.getPort());
        server.bind(
                connectionAddress.getHost(),
                connectionAddress.getPort().getIntValue()
        );
    }

    @Override
    public void close() {
        log.info("Stopping netty server...");
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
        try {
            bossGroup.awaitTermination(5, TimeUnit.SECONDS);
            workGroup.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.warn("Interrupted while waiting for group termination", e);
        }

        if(bossGroup.isShutdown() && workGroup.isShutdown()) {
            log.info("Stopped");
        } else {
            log.warn("Netty server takes too long to terminate...");
        }
    }

    @Override
    public void onIncomingTransmission(Callback<IncomingTransmission> callback) {
        this.callback = callback;
    }
}
