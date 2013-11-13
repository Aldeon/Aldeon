package org.aldeon.sync;

import org.aldeon.communication.task.OutboundRequestTask;
import org.aldeon.db.Db;
import org.aldeon.model.Id;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetMessageRequest;
import org.aldeon.protocol.response.MessageFoundResponse;
import org.aldeon.protocol.response.MessageNotFoundResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

public class DownloadMessageTask<T extends PeerAddress> implements OutboundRequestTask<T> {

    private static final Logger log = LoggerFactory.getLogger(DownloadMessageTask.class);

    private static final int TIMEOUT = 5000;
    private final T peer;
    private final GetMessageRequest request;
    private final Executor executor;
    private final Identifier expectedParent;
    private final Db storage;

    public DownloadMessageTask(T peer, Identifier id, Identifier parent, Executor executor, Db storage) {
        this.peer = peer;
        this.executor = executor;
        this.expectedParent = parent;
        this.storage = storage;

        this.request = new GetMessageRequest();
        request.id = id;
    }

    @Override
    public void onSuccess(Response response) {
        if(response instanceof MessageFoundResponse) {
            onMessageFound((MessageFoundResponse) response);
        } else if(response instanceof MessageNotFoundResponse) {
            onMessageNotFound((MessageNotFoundResponse) response);
        } else {
            onFailure(new InvalidResponseException("Invalid response type"));
        }
    }

    @Override
    public void onFailure(Throwable cause) {
        log.info("Failed to download message " + Id.toString(request.id), cause);
    }

    @Override
    public int getTimeoutMillis() {
        return TIMEOUT;
    }

    @Override
    public Request getRequest() {
        return request;
    }

    @Override
    public T getAddress() {
        return peer;
    }

    @Override
    public Executor getExecutor() {
        return executor;
    }

    /////////////////////////////////////

    private void onMessageFound(MessageFoundResponse response) {
        Message msg = response.message;

        if(Id.equal(msg.getMsgIdentifier(), request.id)) {
            if(expectedParent == null || Id.equal(msg.getParentMessageIdentifier(), expectedParent)) {
                storage.insertMessage(msg, getExecutor());
                return;
            }
        }

        onFailure(new InvalidResponseException("Message does not match the expected parameters"));
    }

    private void onMessageNotFound(MessageNotFoundResponse response) {
        log.info("Message " + Id.toString(request.id) + " not found on the server");
    }
}
