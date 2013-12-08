package org.aldeon.networking.mediums.ip.sender;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.aldeon.networking.common.SendPoint;
import org.aldeon.networking.exceptions.BadRequestException;
import org.aldeon.networking.exceptions.ConnectionBrokenException;
import org.aldeon.networking.exceptions.ServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettySenderHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

    private static final Logger log = LoggerFactory.getLogger(NettySenderHandler.class);

    private SendPoint.OutgoingTransmission task = null;
    private boolean handled = false;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
        if(task == null) {
            log.warn("No task associated with received response");
        } else {

            // Check the response status

            HttpResponseStatus status = msg.getStatus();

            if(status.equals(HttpResponseStatus.OK)) {
                task.onSuccess(msg.content().nioBuffer());
            } else if(status.equals(HttpResponseStatus.BAD_REQUEST)) {
                task.onFailure(new BadRequestException());
            } else if(status.equals(HttpResponseStatus.INTERNAL_SERVER_ERROR)) {
                task.onFailure(new ServerErrorException());
            } else {
                // Other server status
                log.info("Unexpected server status: " + status);
                task.onFailure(new ServerErrorException());
            }
        }
        handled = true;
        ctx.close();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        // TODO: handle response timeouts, not only connection timeouts
        if(!handled) {
            if(task != null) {
                task.onFailure(new ConnectionBrokenException("Connection was closed by the server"));
            }
            handled = true;
        }
    }

    public void setTask(SendPoint.OutgoingTransmission task) {
        this.task = task;
    }
}
