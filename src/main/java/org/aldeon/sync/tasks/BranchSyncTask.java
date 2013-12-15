package org.aldeon.sync.tasks;

import com.google.common.collect.Sets;
import org.aldeon.db.Db;
import org.aldeon.events.BranchingCallbackAggregator;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.common.Sender;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.CompareTreesRequest;
import org.aldeon.protocol.response.BranchInSyncResponse;
import org.aldeon.protocol.response.ChildrenResponse;
import org.aldeon.protocol.response.LuckyGuessResponse;
import org.aldeon.protocol.response.MessageNotFoundResponse;
import org.aldeon.utils.various.Reducer;

import java.util.Map;
import java.util.Set;

/**
 * Synchronizes a selected branch with a selected server.
 * Keep in mind that, by using this class, you assume that
 * the branch root is already in storage. To put it in
 * another words, xor should generally not be empty (unless
 * the branch really xors to empty(), which is not impossible).
 */
public class BranchSyncTask extends AbstractOutboundTask<CompareTreesRequest> {

    private final Sender sender;
    private final Db db;
    private final Callback<SyncResult> onFinished;
    private final Reducer<SyncResult> reducer;

    public BranchSyncTask(PeerAddress peerAddress, Identifier identifier, Identifier xor, boolean avoidLuckyGuess, Sender sender, Db db, Callback<SyncResult> onFinished) {
        super(peerAddress);

        setRequest(new CompareTreesRequest());
        getRequest().branchId = identifier;
        getRequest().branchXor = xor;
        getRequest().force = avoidLuckyGuess;

        this.sender = sender;
        this.db = db;
        this.onFinished = onFinished;
        this.reducer = new SyncResult.SyncResultReducer();
    }

    @Override
    public void onSuccess(Response response) {
        if(response instanceof ChildrenResponse) {
            onChildren((ChildrenResponse) response);
        } else if(response instanceof LuckyGuessResponse && !getRequest().force) {
            onLuckyGuess((LuckyGuessResponse) response);
        } else if(response instanceof BranchInSyncResponse) {
            onBranchInSync((BranchInSyncResponse) response);
        } else if(response instanceof MessageNotFoundResponse) {
            onMessageNotFound((MessageNotFoundResponse) response);
        } else {
            onFinished.call(SyncResult.requestFailed());
        }
    }

    @Override
    public void onFailure(Throwable cause) {
        onFinished.call(SyncResult.requestFailed());
    }

    private void onMessageNotFound(MessageNotFoundResponse response) {
        onFinished.call(SyncResult.accidentalError());
    }

    private void onBranchInSync(BranchInSyncResponse response) {
        onFinished.call(SyncResult.noChanges());
    }

    private void suggestMessage(Identifier id, Callback<SyncResult> onOperationCompleted) {
        // TODO: send SuggestMessageRequest
        onOperationCompleted.call(SyncResult.messageSuggested());
    }

    private Callback<SyncResult> aggregatedCallback(final SyncResult syncResult, final Callback<SyncResult> callback) {
        return new Callback<SyncResult>() {
            @Override
            public void call(SyncResult val) {
                callback.call(reducer.reduce(val, syncResult));
            }
        };
    }

    private void syncBranchWhenRootPresent(final Identifier branch, final boolean avoidLuckyGuess, final Callback<SyncResult> onOperationCompleted) {
        db.getMessageXorById(branch, new Callback<Identifier>() {
            @Override
            public void call(Identifier xor) {
                if(xor == null) {
                    // we must have removed the message before actually synchronizing the branch
                    onOperationCompleted.call(SyncResult.noChanges());
                } else {
                    syncBranchWhenXorKnown(branch, xor, avoidLuckyGuess, onOperationCompleted);
                }
            }
        });
    }

    private void syncBranchWhenXorKnown(Identifier branch, Identifier xor, boolean avoidLuckyGuess, Callback<SyncResult> onOperationCompleted) {
        sender.addTask(new BranchSyncTask(getAddress(), branch, xor, avoidLuckyGuess, sender, db, onOperationCompleted));
    }

    private void downloadAndThenSync(final Identifier id, Identifier parent, final Callback<SyncResult> onOperationCompleted) {
        sender.addTask(new DownloadMessageTask(getAddress(), id, parent, false, db, new Callback<DownloadMessageTask.Result>() {
            @Override
            public void call(DownloadMessageTask.Result downloadResult) {
                switch (downloadResult) {
                    case MESSAGE_INSERTED:
                        // Everything went ok, continue synchronization
                        syncBranchWhenRootPresent(id, false, aggregatedCallback(SyncResult.messageDownloaded(), onOperationCompleted));
                        break;

                    case MESSAGE_EXISTS:
                        // We must have received the message in other thread - continue
                        syncBranchWhenRootPresent(id, false, onOperationCompleted);
                        break;

                    case MESSAGE_NOT_ON_SERVER:
                        // Server must have removed the message after sending us the identifier, so
                        // there is no point in synchronizing this branch. This is server's fault,
                        // but this is nothing particularly bad.
                        onOperationCompleted.call(SyncResult.accidentalError());
                        break;

                    case PARENT_UNKNOWN:
                        // We must have removed the parent after making the check but before inserting
                        // the child the database - we should ignore the branch.
                        onOperationCompleted.call(SyncResult.noChanges());
                        break;

                    case COMMUNICATION_ERROR:
                        // Ouch - this should be noted
                        onOperationCompleted.call(SyncResult.requestFailed());
                        break;
                }
            }
        }));
    }

    private void suggestAndThenSync(final Identifier branch, final Callback<SyncResult> onOperationCompleted) {
        suggestMessage(branch, new Callback<SyncResult>() {
            @Override
            public void call(final SyncResult suggestionCompleted) {
                syncBranchWhenRootPresent(branch, false, aggregatedCallback(suggestionCompleted, onOperationCompleted));
            }
        });
    }

    private void selectBranchWithGivenAncestor(Set<Identifier> branches, Identifier ancestor, Callback<Identifier> onOperationCompleted) {
        // TODO: select appropriate branch to suggest to server
        onOperationCompleted.call(null);
    }

    private void compareXorAndSyncIfNecessary(final Identifier branch, final Identifier ourXor, Identifier hisXor, final Callback<SyncResult> onOperationCompleted) {
        Identifier difference = ourXor.xor(hisXor);
        db.getMessageIdsByXor(difference, new Callback<Set<Identifier>>() {
            @Override
            public void call(Set<Identifier> branches) {
                selectBranchWithGivenAncestor(branches, getRequest().branchId, new Callback<Identifier>() {
                    @Override
                    public void call(Identifier pickedBranch) {
                        if(pickedBranch == null) {
                            syncBranchWhenXorKnown(branch, ourXor, false, onOperationCompleted);
                        } else {
                            suggestAndThenSync(branch, onOperationCompleted);
                        }
                    }
                });
            }
        });
    }

    private void onChildren(final ChildrenResponse response) {
        db.getIdsAndXorsByParentId(getRequest().branchId, new Callback<Map<Identifier, Identifier>>() {
            @Override
            public void call(Map<Identifier, Identifier> val) {

                Map<Identifier, Identifier> localChildren = val;
                Map<Identifier, Identifier> peersChildren = response.children;

                BranchingCallbackAggregator<SyncResult> aggregator = new BranchingCallbackAggregator<>(reducer, onFinished);

                // For each message we have and the server does not
                for(Identifier id: Sets.difference(localChildren.keySet(), peersChildren.keySet())) {
                    suggestMessage(id, aggregator.childCallback());
                }

                // For each message the server has and we do not
                for(Identifier id: Sets.difference(peersChildren.keySet(), localChildren.keySet())) {
                    downloadAndThenSync(id, getRequest().branchId, aggregator.childCallback());
                }

                // For each message we both have
                for(Identifier id: Sets.intersection(localChildren.keySet(), peersChildren.keySet())) {
                    compareXorAndSyncIfNecessary(id, localChildren.get(id), peersChildren.get(id), aggregator.childCallback());
                }

                aggregator.start(SyncResult.noChanges());
            }
        });
    }

    private void onLuckyGuess(final LuckyGuessResponse response) {
        sender.addTask(new DownloadMessageTask(getAddress(), response.id, getRequest().branchId, true, db, new Callback<DownloadMessageTask.Result>() {
            @Override
            public void call(DownloadMessageTask.Result downloadResult) {
                switch (downloadResult) {

                    // --------- First outcome

                    case MESSAGE_INSERTED:
                        // Lucky guess actually worked, let's synchronize it.
                        // However, after we are done synchronizing this branch, we need to
                        // fall back to synchronizing the original branch.

                        final SyncResult messageDownloadedResult = SyncResult.messageDownloaded();

                        syncBranchWhenRootPresent(response.id, false, new Callback<SyncResult>() {
                            @Override
                            public void call(SyncResult guessedBranchSyncResult) {
                                // We just downloaded the message - it must be
                                SyncResult afterLuckyGuess = reducer.reduce(guessedBranchSyncResult, messageDownloadedResult);

                                // once again, synchronize the original branch
                                syncBranchWhenRootPresent(getRequest().branchId, false, aggregatedCallback(afterLuckyGuess, onFinished));
                            }
                        });
                        break;

                    case MESSAGE_EXISTS:
                        // We must have received the message in other thread - continue
                        syncBranchWhenRootPresent(response.id, false, new Callback<SyncResult>() {
                            @Override
                            public void call(SyncResult guessedBranchSyncResult) {
                                // once again, synchronize the original branch
                                syncBranchWhenRootPresent(getRequest().branchId, false, aggregatedCallback(guessedBranchSyncResult, onFinished));
                            }
                        });
                        break;

                    // --------- Second outcome

                    case MESSAGE_NOT_ON_SERVER:
                        // Server must have removed the message after sending us the identifier, so
                        // there is no point in synchronizing this branch. This is server's fault,
                        // but this is nothing particularly bad.

                    case PARENT_UNKNOWN:
                        // We must have removed the parent after making the check but before inserting
                        // the child the database - we should ignore the branch.
                        syncBranchWhenXorKnown(getRequest().branchId, getRequest().branchXor, true, onFinished);
                        break;

                    // --------- Third outcome

                    case COMMUNICATION_ERROR:
                        // Ouch - this should be noted
                        onFinished.call(SyncResult.requestFailed());
                        break;
                }
            }
        }));
    }
}
