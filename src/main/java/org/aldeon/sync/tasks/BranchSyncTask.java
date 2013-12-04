package org.aldeon.sync.tasks;

import com.google.common.collect.Sets;
import org.aldeon.communication.Sender;
import org.aldeon.communication.task.OutboundRequestTask;
import org.aldeon.db.Db;
import org.aldeon.events.ACB;
import org.aldeon.events.BranchingCallbackAggregator;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.CompareTreesRequest;
import org.aldeon.protocol.response.BranchInSyncResponse;
import org.aldeon.protocol.response.ChildrenResponse;
import org.aldeon.protocol.response.LuckyGuessResponse;
import org.aldeon.protocol.response.MessageNotFoundResponse;
import org.aldeon.utils.collections.BooleanAndReducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

public class BranchSyncTask<T extends PeerAddress> implements OutboundRequestTask<T> {

    private static final Logger log = LoggerFactory.getLogger(BranchSyncTask.class);

    public static final int TIMEOUT = 5000;
    private final T peer;
    private final Executor executor;
    private final Db storage;
    private final Sender<T> sender;
    private final Callback<Boolean> onFinished;

    private CompareTreesRequest request;

    public BranchSyncTask(T peer, Identifier branch, boolean force, Identifier xor, Sender<T> sender, Executor executor, Db storage, Callback<Boolean> onFinished) {

        this.peer = peer;
        this.executor = executor;
        this.storage = storage;
        this.sender = sender;
        this.onFinished = onFinished;

        request = new CompareTreesRequest();
        request.force = force;

        request.parent_id = branch;
        request.parent_xor = xor;
    }

    @Override
    public void onSuccess(Response response) {

        if(response instanceof MessageNotFoundResponse) {
            onMessageNotFound((MessageNotFoundResponse) response);
        } else if(response instanceof LuckyGuessResponse) {
            onLuckyGuess((LuckyGuessResponse) response);
        } else if(response instanceof ChildrenResponse) {
            onChildren((ChildrenResponse) response);
        } else if(response instanceof BranchInSyncResponse) {
            onBranchInSync((BranchInSyncResponse) response);
        } else {
            onFailure(new InvalidResponseException("Invalid response type"));
        }
    }

    @Override
    public void onFailure(Throwable cause) {
        log.info("Failed to synchronize with a peer (cause: " + cause.getMessage() + ")", cause);
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

    //////////////////////////////////////////////////////////////////////////////////

    /**
     * Called if the server has no knowledge about this branch
     * @param response
     */
    private void onMessageNotFound(MessageNotFoundResponse response) {
        offerMessage(request.parent_id, onFinished);
    }

    /**
     * Called if the server attempts a lucky guess
     * @param response
     */
    private void onLuckyGuess(final LuckyGuessResponse response) {
        /*
            1. Do we have the suggested message?
            IF WE DO:
                1.1. Resend the request with force flag
            ELSE
                1.2. Attempt downloading the message
                1.3. Check if the received message is actually a child of the branch we are syncing
                IF TRUE:
                    1.3.1. Put the message in storage
                    1.3.2. Resync the branch to make sure we have everything
                ELSE:
                    1.3.3. Resend the request with force flag

         */

        storage.getMessageById(response.id, new ACB<Message>(executor) {
            @Override
            protected void react(Message val) {

                // Check if we already have the suggested message
                if(val == null) {
                    // Message unknown

                    // Attempt downloading the message, then repeat the sync.
                    // Repeating guarantees we do not lose any message.
                    sender.addTask(new DownloadMessageTask<>(peer, response.id, request.parent_id, true, executor, storage, new Callback<Boolean>() {
                        @Override
                        public void call(Boolean messageSaved) {

                            // (messageSaved == true) => message was saved, no need to set force flag
                            // (messageSaved != true) => lucky guess failed, set force flag

                            sender.addTask(new BranchSyncTask<>(peer, request.parent_id, !messageSaved, request.parent_xor, sender, executor, storage, onFinished));
                        }
                    }));
                } else {
                    // Message known, resync with force flag
                    sender.addTask(new BranchSyncTask<>(peer, request.parent_id, true, request.parent_xor, sender, executor, storage, onFinished));
                }
            }
        });
    }

    /**
     * Called if the server returns the list of child identifiers
     * @param response
     */
    private void onChildren(final ChildrenResponse response) {
        storage.getIdsAndXorsByParentId(request.parent_id, new ACB<Map<Identifier, Identifier>>(executor) {
            @Override
            public void react(Map<Identifier, Identifier> map) {

                // 0. These are our collections we need to analyse

                Map<Identifier, Identifier> storedMap = map;
                Map<Identifier, Identifier> remoteMap = response.children;
                final BranchingCallbackAggregator<Boolean> aggregator = new BranchingCallbackAggregator<>(new BooleanAndReducer(), onFinished);

                // 1. Process the common messages

                for(final Identifier id: Sets.intersection(storedMap.keySet(), remoteMap.keySet())) {

                    final Identifier xor = storedMap.get(id).xor(remoteMap.get(id));

                    if(xor.isEmpty()) {
                        // Same state, do nothing
                    } else {
                        // There is a difference.

                        // Create callback to be triggered when query returns
                        final Callback<Boolean> queryFetched = aggregator.childCallback();

                        // Let's see if we have a differing branch
                        storage.getMessageIdsByXor(xor, new ACB<Set<Identifier>>(executor){
                            @Override
                            protected void react(Set<Identifier> matchingBranches) {

                                /*
                                    Choose which one is the branch we are looking for
                                 */

                                // TODO: implement the branch picking procedure
                                Identifier differingBranch = null;

                                if(differingBranch == null) {
                                    // we must go deeper
                                    OutboundRequestTask<T> nestedTask = new BranchSyncTask<>(peer, id, false, xor, sender, executor, storage, aggregator.childCallback());
                                    sender.addTask(nestedTask);

                                } else {
                                    // Hey, we have a differing branch. Let's inform the server.
                                    offerMessage(differingBranch, aggregator.childCallback());
                                }

                                queryFetched.call(true);
                            }
                        });
                    }
                }

                // 2. Process the messages we do not know

                for(Identifier id: Sets.difference(remoteMap.keySet(), storedMap.keySet())) {
                    sender.addTask(new DownloadMessageTask<>(peer, id, request.parent_id, false, executor, storage, aggregator.childCallback()));
                }

                // 3. Process the messages the peer does not know

                for(Identifier id: Sets.difference(storedMap.keySet(), remoteMap.keySet())) {
                    offerMessage(id, aggregator.childCallback());
                }

                // All callbacks dispatched, enable aggregator fallback
                aggregator.start();
            }
        });
    }

    /**
     * Called if the server has the same branch xor
     * @param response
     */
    private void onBranchInSync(BranchInSyncResponse response) {
        // both we and the server know exactly the same messages
        onFinished.call(true);
    }

    private void offerMessage(Identifier id, Callback<Boolean> onOfferCompleted) {
        // here goes the logic of offering the message to the server
        onOfferCompleted.call(true);
    }
}
