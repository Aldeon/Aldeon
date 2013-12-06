package org.aldeon.communication.netty.sender;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.aldeon.communication.netty.exception.BadRequestException;
import org.aldeon.communication.netty.exception.UnexpectedServerStatusException;
import org.aldeon.communication.netty.exception.InternalServerErrorException;
import org.aldeon.communication.task.OutboundRequestTask;
import org.aldeon.protocol.Response;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.conversion.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SenderHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

    private static final Logger log = LoggerFactory.getLogger(SenderHandler.class);

    private final Converter<FullHttpResponse, Response> decoder;
    private OutboundRequestTask task = null;

    public SenderHandler(Converter<FullHttpResponse, Response> decoder) {
        this.decoder = decoder;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
        if(task == null) {
            log.warn("No task associated with received response");
        } else {

            // Check the response status

            HttpResponseStatus status = msg.getStatus();

            if(status.equals(HttpResponseStatus.OK)) {
                try {
                    final Response response = decoder.convert(msg);
                    task.onSuccess(response);
                } catch (final ConversionException e) {
                    task.onFailure(e);
                }
            } else if(status.equals(HttpResponseStatus.BAD_REQUEST)) {
                task.onFailure(new BadRequestException());
            } else if(status.equals(HttpResponseStatus.INTERNAL_SERVER_ERROR)) {
                task.onFailure(new InternalServerErrorException());
            } else {
                task.onFailure(new UnexpectedServerStatusException(status));
            }
        }
        ctx.close();
    }

    public void setTask(OutboundRequestTask task) {
        this.task = task;
    }
}
