package org.aldeon.communication.netty.sender;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import org.aldeon.communication.task.OutboundRequestTask;
import org.aldeon.net.IpPeerAddress;
import org.aldeon.protocol.Response;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SenderHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

    private static final Logger log = LoggerFactory.getLogger(SenderHandler.class);

    private final Converter<FullHttpResponse, Response> decoder;
    private OutboundRequestTask<? extends IpPeerAddress> task = null;

    public SenderHandler(Converter<FullHttpResponse, Response> decoder) {
        this.decoder = decoder;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
        if(task == null) {
            log.warn("No task associated with received response");
        } else {
            try {
                final Response response = decoder.convert(msg);
                task.getExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        task.onSuccess(response);
                    }
                });
            } catch (final ConversionException e) {
                task.getExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        task.onFailure(e);
                    }
                });
            }
        }
        ctx.close();
    }

    public void setTask(OutboundRequestTask<? extends IpPeerAddress> task) {
        this.task = task;
    }
}
