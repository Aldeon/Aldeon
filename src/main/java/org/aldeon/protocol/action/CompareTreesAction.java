package org.aldeon.protocol.action;

import org.aldeon.db.Db;
import org.aldeon.events.ACB;
import org.aldeon.events.AsyncCallback;
import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;
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

    public CompareTreesAction(Db db) {
        this.db = db;
    }

    @Override
    public void respond(PeerAddress peer, final CompareTreesRequest request, final AsyncCallback<Response> onResponse) {

        // Fetch message xor
        db.getMessageXorById(request.parent_id, new ACB<Identifier>(onResponse.getExecutor()){
            @Override
            protected void react(Identifier xor) {

                if(xor == null) {
                    // Message not known
                    onResponse.call(new MessageNotFoundResponse());
                } else {
                    // We know this message

                    if(xor.equals(request.parent_xor)) {
                        // If our branches seem identical
                        onResponse.call(new BranchInSyncResponse());
                    } else {
                        // There are differences

                        if(request.force) {
                            sendChildren(request.parent_id, onResponse);
                        } else {
                            // Attempt lucky guess

                            // Let's see if we know the difference
                            Identifier diffXor = xor.xor(request.parent_xor);

                            db.getMessageIdsByXor(diffXor, new ACB<Set<Identifier>>(onResponse.getExecutor()) {
                                @Override
                                protected void react(Set<Identifier> branches) {

                                    Identifier guess = pickBranch(branches, request.parent_id);

                                    if(guess == null) {
                                        sendChildren(request.parent_id, onResponse);
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

    private void sendChildren(Identifier parent, final AsyncCallback<Response> callback) {

        db.getIdsAndXorsByParentId(parent, new ACB<Map<Identifier, Identifier>>(callback.getExecutor()) {
            @Override
            protected void react(Map<Identifier, Identifier> idsAndXors) {
                callback.call(new ChildrenResponse(idsAndXors));
            }
        });
    }
}
