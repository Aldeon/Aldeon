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
import org.aldeon.net.AddressTranslation;
import org.aldeon.net.IpPeerAddress;
import org.aldeon.communication.task.InboundRequestTask;
import org.aldeon.communication.Receiver;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;
import org.aldeon.utils.conversion.Converter;
import org.aldeon.events.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * Netty server implementation. Handles incoming transmissions, parses
 * into http requests and maintains a todolist of requests to respond to.
 */
public class NettyReceiver implements Receiver<IpPeerAddress>{

    private static final Logger log = LoggerFactory.getLogger(NettyReceiver.class);

    private AddressTranslation addressTranslation;
    private ServerBootstrap server;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;
    private Callback<InboundRequestTask<IpPeerAddress>> callback = null;
    private Executor executor;

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
                p.addLast("handler",    new ReceiverHandler(decoder, encoder, executor, callback));

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
        log.info("Stopping netty server");
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }

    @Override
    public void setCallback(Callback<InboundRequestTask<IpPeerAddress>> callback, Executor executor) {
        this.callback = callback;
        this.executor = executor;
    }
}
