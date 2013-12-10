package org.aldeon.sync.tasks;

import org.aldeon.networking.common.OutboundRequestTask;
import org.aldeon.db.Db;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetMessageRequest;
import org.aldeon.protocol.response.MessageFoundResponse;
import org.aldeon.protocol.response.MessageNotFoundResponse;
import org.aldeon.utils.helpers.Callbacks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DownloadMessageTask extends BaseOutboundTask<GetMessageRequest> implements OutboundRequestTask {

    private static final Logger log = LoggerFactory.getLogger(DownloadMessageTask.class);

    private final Identifier expectedParent;
    private final Db storage;
    private final Callback<Boolean> onFinished;
    private final boolean checkAncestry;

    public DownloadMessageTask(PeerAddress peer, Identifier id, Identifier parent, boolean checkAncestry, Db storage, Callback<Boolean> onFinished) {
        super(5000, peer);

        setRequest(new GetMessageRequest());
        req().id = id;

        this.expectedParent = parent;
        this.storage = storage;
        this.onFinished = onFinished;
        this.checkAncestry = checkAncestry;

    }

    @Override
    public void onSuccess(Response response) {
        if(response instanceof MessageFoundResponse) {
            onMessageFound((MessageFoundResponse) response);
        } else if(response instanceof MessageNotFoundResponse) {
            onMessageNotFound();
        } else {
            onFailure(new InvalidResponseException("Invalid response type"));
        }
    }

    @Override
    public void onFailure(Throwable cause) {
        log.info("Failed to download message " + req().id, cause);
        onFinished.call(false);
    }

    /////////////////////////////////////

    private void checkRelation(Message msg, final Callback<Boolean> callback) {

        if(expectedParent == null || expectedParent.equals(msg.getParentMessageIdentifier())) {
            callback.call(true);
        } else if(checkAncestry) {
            storage.checkAncestry(msg.getParentMessageIdentifier(), expectedParent, callback);
        } else {
            callback.call(false);
        }
    }

    private void onMessageFound(MessageFoundResponse response) {

        final Message msg = response.message;

        if(req().id.equals(msg.getIdentifier())) {
            checkRelation(msg, new Callback<Boolean>() {
                @Override
                public void call(Boolean matchesCriteria) {
                    if(matchesCriteria) {
                        storage.insertMessage(msg, onFinished);
                    } else {
                        onFailure(new InvalidResponseException("Message does not match the expected parameters"));
                    }
                }
            });
        } else {
            onFailure(new InvalidResponseException("Message does not match the expected parameters"));
        }
    }

    private void onMessageNotFound() {
        log.info("Message " + req().id + " not found on the server");
        onFinished.call(false);
    }
}
