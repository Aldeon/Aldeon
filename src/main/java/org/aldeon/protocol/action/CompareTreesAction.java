package org.aldeon.protocol.action;

import com.google.inject.Inject;
import org.aldeon.core.Core;
import org.aldeon.db.Db;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.protocol.Action;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.CompareTreesRequest;
import org.aldeon.protocol.response.BranchInSyncResponse;
import org.aldeon.protocol.response.ChildrenResponse;
import org.aldeon.protocol.response.LuckyGuessResponse;
import org.aldeon.protocol.response.MessageNotFoundResponse;

import java.util.Map;
import java.util.Set;

public class CompareTreesAction implements Action<CompareTreesRequest> {

    private final Db db;

    /*
        Possible responses:
            - MessageNotFoundResponse       // if we do not know anything about the message
            - LuckyGuessResponse            // if we do have a possible diff
            - BranchInSyncResponse          // if our xor is the same
            - ChildrenResponse              // children
     */

    @Inject
    public CompareTreesAction(Core core) {
        this.db = core.getStorage();
    }

    @Override
    public void respond(PeerAddress peer, final CompareTreesRequest request, final Callback<Response> onResponse) {

        // Fetch message xor
        db.getMessageXorById(request.branchId, new Callback<Identifier>(){
            @Override
            public void call(Identifier xor) {

                if(xor == null) {
                    // Message not known
                    onResponse.call(new MessageNotFoundResponse());
                } else {
                    // We know this message

                    if(xor.equals(request.branchXor)) {
                        // If our branches seem identical
                        onResponse.call(new BranchInSyncResponse());
                    } else {
                        // There are differences

                        if(request.force) {
                            sendChildren(request.branchId, onResponse);
                        } else {
                            // Attempt lucky guess

                            // Let's see if we know the difference
                            Identifier diffXor = xor.xor(request.branchXor);

                            db.getMessageIdsByXor(diffXor, new Callback<Set<Identifier>>() {
                                @Override
                                public void call(Set<Identifier> branches) {

                                    Identifier guess = pickBranch(branches, request.branchId);

                                    if(guess == null) {
                                        sendChildren(request.branchId, onResponse);
                                    } else {
                                        onResponse.call(new LuckyGuessResponse(guess));
                                    }
                                }
                            });
                        }
                    }
                }

            }
        });

    }

    private Identifier pickBranch(Set<Identifier> branches, Identifier parent) {
        /*
            Here we pick the diff branch from all found matching branches.
            We should make sure the chosen branch is a descendant of the parent branch.
         */

        //TODO: proper implementation of a lucky guess branch picker

        if(branches.isEmpty()) {
            return null;                        // empty set, return null
        } else {
            return branches.iterator().next();  // debug: return first one
        }
    }

    private void sendChildren(Identifier parent, final Callback<Response> callback) {

        db.getIdsAndXorsByParentId(parent, new Callback<Map<Identifier, Identifier>>() {
            @Override
            public void call(Map<Identifier, Identifier> idsAndXors) {
                callback.call(new ChildrenResponse(idsAndXors));
            }
        });
    }
}
