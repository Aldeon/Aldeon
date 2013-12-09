package org.aldeon.sync.tasks;

import com.google.common.collect.Sets;
import org.aldeon.networking.common.Sender;
import org.aldeon.networking.common.OutboundRequestTask;
import org.aldeon.db.Db;
import org.aldeon.events.BranchingCallbackAggregator;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.CompareTreesRequest;
import org.aldeon.protocol.response.BranchInSyncResponse;
import org.aldeon.protocol.response.ChildrenResponse;
import org.aldeon.protocol.response.LuckyGuessResponse;
import org.aldeon.protocol.response.MessageNotFoundResponse;
import org.aldeon.utils.various.BooleanAndReducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

public class BranchSyncTask extends BaseOutboundTask<CompareTreesRequest> implements OutboundRequestTask {

    private static final Logger log = LoggerFactory.getLogger(BranchSyncTask.class);

    private final Db storage;
    private final Sender sender;
    private final Callback<Boolean> onFinished;

    public BranchSyncTask(PeerAddress peer, Identifier branch, boolean force, Identifier xor, Sender sender, Db storage, Callback<Boolean> onFinished) {
        super(5000, peer);

        this.storage = storage;
        this.sender = sender;
        this.onFinished = onFinished;

        setRequest(new CompareTreesRequest());

        req().force = force;
        req().parent_id = branch;
        req().parent_xor = xor;
    }

    @Override
    public void onSuccess(Response response) {

        if(response instanceof MessageNotFoundResponse) {
            onMessageNotFound();
        } else if(response instanceof LuckyGuessResponse) {
            onLuckyGuess((LuckyGuessResponse) response);
        } else if(response instanceof ChildrenResponse) {
            onChildren((ChildrenResponse) response);
        } else if(response instanceof BranchInSyncResponse) {
            onBranchInSync();
        } else {
            onFailure(new InvalidResponseException("Invalid response type"));
        }
    }

    @Override
    public void onFailure(Throwable cause) {
        log.info("Failed to synchronize with a peer (cause: " + cause.getMessage() + ")", cause);
        onFinished.call(false);
    }

    //////////////////////////////////////////////////////////////////////////////////

    /**
     * Called if the server has no knowledge about this branch
     */
    private void onMessageNotFound() {
        offerMessage(req().parent_id, onFinished);
    }

    /**
     * Called if the server attempts a lucky guess
     * @param response response
     */
    private void onLuckyGuess(final LuckyGuessResponse response) {

        storage.getMessageById(response.id, new Callback<Message>() {
            @Override
            public void call(Message val) {

                // Check if we already have the suggested message
                if(val == null) {
                    // Message unknown

                    // Attempt downloading the message, then repeat the sync.
                    // Repeating guarantees we do not lose any message.
                    sender.addTask(new DownloadMessageTask(getAddress(), response.id, req().parent_id, true, storage, new Callback<Boolean>() {
                        @Override
                        public void call(Boolean messageSaved) {

                            // (messageSaved == true) => message was saved, no need to set force flag
                            // (messageSaved != true) => lucky guess failed, set force flag

                            sender.addTask(new BranchSyncTask(getAddress(), req().parent_id, !messageSaved, req().parent_xor, sender, storage, onFinished));
                        }
                    }));
                } else {
                    // Message known, resync with force flag
                    sender.addTask(new BranchSyncTask(getAddress(), req().parent_id, true, req().parent_xor, sender, storage, onFinished));
                }
            }
        });
    }

    /**
     * Called if the server returns the list of child identifiers
     * @param response response
     */
    private void onChildren(final ChildrenResponse response) {
        storage.getIdsAndXorsByParentId(req().parent_id, new Callback<Map<Identifier, Identifier>>() {
            @Override
            public void call(Map<Identifier, Identifier> storedMap) {

                // 0. These are our collections we need to analyse

                Map<Identifier, Identifier> remoteMap = response.children;
                final BranchingCallbackAggregator<Boolean> aggregator = new BranchingCallbackAggregator<>(new BooleanAndReducer(), onFinished);

                // 1. Process the common messages

                for(final Identifier id: Sets.intersection(storedMap.keySet(), remoteMap.keySet())) {

                    final Identifier xor = storedMap.get(id).xor(remoteMap.get(id));

                    if(!xor.isEmpty()) {
                        // There is a difference.

                        // Create callback to be triggered when query returns
                        final Callback<Boolean> queryFetched = aggregator.childCallback();

                        // Let's see if we have a differing branch
                        storage.getMessageIdsByXor(xor, new Callback<Set<Identifier>>(){
                            @Override
                            public void call(Set<Identifier> matchingBranches) {

                                /*
                                    Choose which one is the branch we are looking for
                                 */

                                // TODO: implement the branch picking procedure
                                Identifier differingBranch = null;

                                if(differingBranch == null) {
                                    // we must go deeper
                                    sender.addTask(new BranchSyncTask(getAddress(), id, false, xor, sender, storage, aggregator.childCallback()));

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
                    sender.addTask(new DownloadMessageTask(getAddress(), id, req().parent_id, false, storage, aggregator.childCallback()));
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
     */
    private void onBranchInSync() {
        // both we and the server know exactly the same messages
        onFinished.call(true);
    }

    private void offerMessage(Identifier id, Callback<Boolean> onOfferCompleted) {
        // here goes the logic of offering the message to the server
        System.out.println("Offer message " + id);
        onOfferCompleted.call(true);
    }
}
