package org.aldeon.sync;

import com.google.common.collect.Sets;
import org.aldeon.communication.Sender;
import org.aldeon.communication.task.OutboundRequestTask;
import org.aldeon.db.Db;
import org.aldeon.events.ACB;
import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.Request;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.CompareTreesRequest;
import org.aldeon.protocol.response.BranchInSyncResponse;
import org.aldeon.protocol.response.ChildrenResponse;
import org.aldeon.protocol.response.LuckyGuessResponse;
import org.aldeon.protocol.response.MessageNotFoundResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Executor;

public class BranchSyncTask<T extends PeerAddress> implements OutboundRequestTask<T> {

    private static final Logger log = LoggerFactory.getLogger(BranchSyncTask.class);

    public static final int TIMEOUT = 5000;
    private final T peer;
    private final Executor executor;
    private final Db storage;
    private final Sender<T> sender;

    private CompareTreesRequest request;

    public BranchSyncTask(T peer, Identifier branch, Identifier xor, Sender<T> sender, Executor executor, Db storage) {

        this.peer = peer;
        this.executor = executor;
        this.storage = storage;
        this.sender = sender;

        request = new CompareTreesRequest();

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
        offerMessage(request.parent_id);
    }

    /**
     * Called if the server attempts a lucky guess
     * @param response
     */
    private void onLuckyGuess(LuckyGuessResponse response) {
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

                // 1. Process the common messages

                for(final Identifier id: Sets.intersection(storedMap.keySet(), remoteMap.keySet())) {


                    final Identifier xor = storedMap.get(id).xor(remoteMap.get(id));

                    if(xor.isEmpty()) {
                        // Same state, do nothing
                    } else {
                        // There is a difference. Let's see if we have a differing branch
                        storage.getMessageIdByXor(xor, new ACB<Identifier>(executor) {
                            @Override
                            public void react(Identifier val) {

                                if(val == null) {
                                    // we must go deeper
                                    OutboundRequestTask<T> nestedTask = new BranchSyncTask<>(peer, id, xor, sender, executor, storage);
                                    sender.addTask(nestedTask);

                                } else {
                                    // Hey, we have a differing branch. Let's inform the server.
                                    offerMessage(val);
                                }

                            }
                        });
                    }
                }

                // 2. Process the messages we do not know

                for(Identifier id: Sets.difference(remoteMap.keySet(), storedMap.keySet())) {
                    sender.addTask(new DownloadMessageTask<T>(peer, id, request.parent_id, executor, storage));
                }

                // 3. Process the messages the peer does not know

                for(Identifier id: Sets.difference(storedMap.keySet(), remoteMap.keySet())) {
                    offerMessage(id);
                }

            }
        });
    }

    /**
     * Called if the server has the same branch xor
     * @param response
     */
    private void onBranchInSync(BranchInSyncResponse response) {
        // both we and the server know exactly the same messages
    }

    private void offerMessage(Identifier id) {
        // here goes the logic of offering the message to the server
    }
}
