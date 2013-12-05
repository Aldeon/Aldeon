package org.aldeon.sync.tasks;

import org.aldeon.communication.Sender;
import org.aldeon.communication.task.OutboundRequestTask;
import org.aldeon.db.Db;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.exception.UnexpectedResponseTypeException;
import org.aldeon.protocol.request.GetDiffRequest;
import org.aldeon.protocol.response.DiffResponse;
import org.aldeon.utils.collections.DependencyDispatcher;
import org.aldeon.utils.collections.DependencyDispatcherModule;

import java.util.Collections;
import java.util.Map;

public class GetDiffTask extends BaseOutboundTask<GetDiffRequest> implements OutboundRequestTask {

    private final Callback<Long> onFinished;
    private final Db storage;
    private final Sender sender;

    public GetDiffTask(PeerAddress peer, Identifier topic, long clock, Db storage, Sender sender, Callback<Long> onFinished) {
        super(5000, peer);

        this.onFinished = onFinished;
        this.storage = storage;
        this.sender = sender;

        setRequest(new GetDiffRequest());
        req().clock = clock;
        req().topic = topic;
    }

    @Override
    public void onSuccess(Response response) {

        if(response instanceof DiffResponse) {
            onDiff((DiffResponse) response);
        } else {
            onFailure(new UnexpectedResponseTypeException());
        }
    }

    private void onDiff(final DiffResponse response) {

        DependencyDispatcher<Identifier> dispatcher = DependencyDispatcherModule.create(response.ids);

        work(Collections.unmodifiableMap(response.ids), dispatcher, new Callback<Boolean>() {

            public boolean notCalled = true;

            @Override
            public synchronized void call(Boolean success) {

                // Make sure the callback is called once
                if(notCalled) {
                    notCalled = false;

                    // Check if we did download all the messages
                    if(success) {
                        onFinished.call(response.clock);
                    } else {
                        onFinished.call(null);
                    }
                }
            }
        });
    }

    private void work(final Map<Identifier, Identifier> parents, final DependencyDispatcher<Identifier> dispatcher, final Callback<Boolean> onDownloadsCompleted) {

        while(true) {
            final Identifier id = dispatcher.next();
            if(id == null) {
                break;
            }

            final Identifier parent = parents.get(id);

            // We have an identifier. It represents a message that we may possibly want.
            // Check if we already have its parent

            storage.getMessageById(parent, new Callback<Message>() {
                @Override
                public void call(final Message message) {

                    if(message == null) {
                        // We do not know about the parent of id. No need to download the message.
                        dispatcher.remove(id);
                    } else {
                        // We have a parent of this message, so we can download it.

                        sender.addTask(new DownloadMessageTask(getAddress(), id, parent, false, storage, new Callback<Boolean>() {
                            @Override
                            public void call(Boolean success) {
                                // DownloadMessageTask ended. Did we download the message?
                                if(success) {

                                    // Great, message downloaded.

                                    // Unlock dependant identifiers
                                    dispatcher.remove(id);

                                    // We may proceed
                                    work(parents, dispatcher, onDownloadsCompleted);

                                } else {
                                    /*
                                        Failed to download the message.

                                        This is very bad - subsequent downloads will probably fail.
                                        We need to abort downloading the delta and resynchronize.
                                     */
                                    onDownloadsCompleted.call(false);
                                }
                            }
                        }));
                    }
                }
            });
        }

        // Check if all messages have been downloaded.
        if(dispatcher.isFinished()) {
            onDownloadsCompleted.call(true);
        }

        // TODO: check if dispatcher contains a loop. If it does, the peer gave us an invalid request.
    }

    @Override
    public void onFailure(Throwable cause) {
        onFinished.call(null);
    }
}
