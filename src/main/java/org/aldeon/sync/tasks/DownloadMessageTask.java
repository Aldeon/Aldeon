package org.aldeon.sync.tasks;

import org.aldeon.communication.task.OutboundRequestTask;
import org.aldeon.db.Db;
import org.aldeon.events.ACB;
import org.aldeon.events.Callback;
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
    private final Callback<Boolean> onFinished;
    private final boolean checkAncestry;

    public DownloadMessageTask(T peer, Identifier id, Identifier parent, boolean checkAncestry, Executor executor, Db storage, Callback<Boolean> onFinished) {
        this.peer = peer;
        this.executor = executor;
        this.expectedParent = parent;
        this.storage = storage;
        this.onFinished = onFinished;
        this.checkAncestry = checkAncestry;

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
        log.info("Failed to download message " + request.id, cause);
        onFinished.call(false);
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

    private void checkRelation(Message msg, final Callback<Boolean> callback) {

        if(expectedParent == null || expectedParent.equals(msg.getParentMessageIdentifier())) {
            callback.call(true);
        } else if(checkAncestry) {
            storage.checkAncestry(msg.getParentMessageIdentifier(), expectedParent, new ACB<Boolean>(executor) {
                @Override
                protected void react(Boolean val) {
                    callback.call(val);
                }
            });
        } else {
            callback.call(false);
        }
    }

    private void onMessageFound(MessageFoundResponse response) {

        final Message msg = response.message;

        if(request.id.equals(msg.getIdentifier())) {
            checkRelation(msg, new Callback<Boolean>() {
                @Override
                public void call(Boolean matchesCriteria) {
                    if(matchesCriteria) {
                        storage.insertMessage(msg, getExecutor());
                        onFinished.call(true);
                    } else {
                        onFailure(new InvalidResponseException("Message does not match the expected parameters"));
                    }
                }
            });
        } else {
            onFailure(new InvalidResponseException("Message does not match the expected parameters"));
        }
    }

    private void onMessageNotFound(MessageNotFoundResponse response) {
        log.info("Message " + request.id + " not found on the server");
        onFinished.call(false);
    }
}
