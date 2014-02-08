package org.aldeon.sync.tasks;

import org.aldeon.db.Db;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetMessageRequest;
import org.aldeon.protocol.response.MessageFoundResponse;
import org.aldeon.protocol.response.MessageNotFoundResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DownloadMessageTask extends AbstractOutboundTask<GetMessageRequest> {

    private static final Logger log = LoggerFactory.getLogger(DownloadMessageTask.class);

    private final Callback<Result> onFinished;

    public static enum Result {
        MESSAGE_INSERTED,
        MESSAGE_EXISTS,
        CHECK_FAILED,
        PARENT_UNKNOWN,
        COMMUNICATION_ERROR,
        MESSAGE_NOT_ON_SERVER
    }

    private final Identifier identifier;
    private final Identifier ancestor;
    private final boolean checkAncestry;
    private final Db db;

    public DownloadMessageTask(PeerAddress peerAddress, final Identifier identifier, Identifier ancestor, boolean checkAncestry, Db db, final Callback<Result> onFinished) {
        super(peerAddress);

        if(checkAncestry) {
            log.info("Downloading message " + identifier + " (ancestor: " + ancestor + ")");
        } else {
            log.info("Downloading message " + identifier + " (parent: " + ancestor + ")");
        }

        this.identifier = identifier;
        this.ancestor = ancestor;
        this.checkAncestry = checkAncestry;
        this.db = db;
        this.onFinished = new Callback<Result>() {
            @Override
            public void call(Result val) {
                log.info("DownloadMessageTask(" + identifier + ") finished with status " + val);
                onFinished.call(val);
            }
        };

        setRequest(new GetMessageRequest());
        getRequest().id = identifier;
    }

    @Override
    public void onSuccess(Response response) {
        if(response instanceof MessageFoundResponse) {
            onMessageFound((MessageFoundResponse) response);
        } else if(response instanceof MessageNotFoundResponse) {
            onMessageNotFound((MessageNotFoundResponse) response);
        } else {
            onFinished.call(Result.COMMUNICATION_ERROR);
        }
    }

    @Override
    public void onFailure(Throwable cause) {
        log.info("Failed to download the message (cause: " + cause + ")");
        onFinished.call(Result.COMMUNICATION_ERROR);
    }

    private void checkAncestry(Identifier actualParent, Callback<Boolean> onOperationCompleted) {
        if(ancestor.equals(actualParent)) {
            onOperationCompleted.call(true);
        } else {
            if(checkAncestry) {
                db.checkAncestry(actualParent, ancestor, onOperationCompleted);
            } else {
                onOperationCompleted.call(false);
            }
        }
    }

    private void tryToInsert(Message message) {
        db.insertMessage(message, new Callback<Db.InsertResult>() {
            @Override
            public void call(Db.InsertResult result) {

                if(result == Db.InsertResult.INSERTED) {
                    onFinished.call(Result.MESSAGE_INSERTED);
                } else if(result == Db.InsertResult.NO_PARENT) {
                    onFinished.call(Result.PARENT_UNKNOWN);
                } else if(result == Db.InsertResult.ALREADY_EXISTS) {
                    onFinished.call(Result.MESSAGE_EXISTS);
                } else {
                    throw new IllegalStateException("Invalid insert result");
                }

            }
        });
    }

    private void onMessageFound(final MessageFoundResponse response) {
        checkAncestry(response.message.getParentMessageIdentifier(), new Callback<Boolean>() {
            @Override
            public void call(Boolean checksPassed) {
                if(checksPassed) {
                    tryToInsert(response.message);
                } else {
                    onFinished.call(Result.CHECK_FAILED);
                }
            }
        });
    }

    private void onMessageNotFound(MessageNotFoundResponse response) {
        onFinished.call(Result.MESSAGE_NOT_ON_SERVER);
    }
}
