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

public class DownloadMessageTask extends AbstractOutboundTask<GetMessageRequest> {

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

    public DownloadMessageTask(PeerAddress peerAddress, Identifier identifier, Identifier ancestor, boolean checkAncestry, Db db, Callback<Result> onFinished) {
        super(peerAddress);

        this.identifier = identifier;
        this.ancestor = ancestor;
        this.checkAncestry = checkAncestry;
        this.db = db;
        this.onFinished = onFinished;

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
        db.insertMessage(message, new Callback<Boolean>() {
            @Override
            public void call(Boolean messageInserted) {
                if(messageInserted) {
                    onFinished.call(Result.MESSAGE_INSERTED);
                } else {
                    /*
                        TODO: Differentiate between following situations:
                            - message already exists in the database
                            - parent not in the database
                     */
                    onFinished.call(Result.PARENT_UNKNOWN);
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
