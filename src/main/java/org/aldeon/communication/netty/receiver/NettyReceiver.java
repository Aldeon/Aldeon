package org.aldeon.communication.netty.receiver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.aldeon.communication.Receiver;
import org.aldeon.communication.task.InboundRequestTask;
import org.aldeon.events.Callback;
import org.aldeon.net.AddressTranslation;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;
import org.aldeon.utils.conversion.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Netty server implementation. Handles incoming transmissions, parses
 * into http requests and maintains a todolist of requests to respond to.
 */
public class NettyReceiver implements Receiver {

    private static final Logger log = LoggerFactory.getLogger(NettyReceiver.class);

    private AddressTranslation addressTranslation;
    private ServerBootstrap server;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;
    private Callback<InboundRequestTask> callback = null;

    public NettyReceiver(
            AddressTranslation addressTranslation,
            final Converter<Response, FullHttpResponse> encoder,
            final Converter<FullHttpRequest, Request> decoder
    ) {

        this.addressTranslation = addressTranslation;

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
                p.addLast("handler",    new ReceiverHandler(decoder, encoder, callback));

                log.info("Created a pipeline for incoming request");
            }
        });
    }

    public void start() {
        log.info("Starting netty server on " + addressTranslation.getInternalAddress() + ":" + addressTranslation.getInternalPort());
        server.bind(
                addressTranslation.getInternalAddress(),
                addressTranslation.getInternalPort().getIntValue()
        );
    }

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
    public void setCallback(Callback<InboundRequestTask> callback) {
        this.callback = callback;
    }
}
