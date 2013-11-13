package org.aldeon.protocol.action;

import org.aldeon.core.Core;
import org.aldeon.events.ACB;
import org.aldeon.events.AsyncCallback;
import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.Action;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.CompareTreesRequest;
import org.aldeon.protocol.response.ChildrenResponse;
import org.aldeon.protocol.response.LuckyGuessResponse;

import java.util.Map;
import java.util.concurrent.Executor;

/**
 *
 */
public class CompareTreesAction implements Action<CompareTreesRequest> {
    private final Core core;

    public CompareTreesAction(Core core) {
        this.core = core;
    }

    @Override
    public void respond(PeerAddress peer, final CompareTreesRequest request, final AsyncCallback<Response> onResponse) {

        final Executor e = onResponse.getExecutor();

        if (request.force == false) { //if client is not forcing us to do layer-by-layer cmp

            //get xor value for this parent msg from database
            core.getStorage().getMessageXorById(request.parent_id, new ACB<Identifier>(e) {
                @Override
                public void react(Identifier val) {
                    Identifier guess_xor = null;
                    //Identifier guess_xor =  arithmetic.xor(val, request.parent_xor)

                    /*
                    TODO: Take the request.force flag into account before attempting a lucky guess
                    */

                    //attempt a lucky guess
                    core.getStorage().getMessageIdByXor(guess_xor, new ACB<Identifier>(e) {
                        @Override
                        public void react(Identifier val) {
                            if (val != null) { //msg found = lucky guess successful
                                onResponse.call(new LuckyGuessResponse(val));
                            } else { //there is no XOR match (lucky guess failed) fall back to comparing trees layer by layer
                                core.getStorage().getIdsAndXorsByParentId(request.parent_id, new ACB<Map<Identifier, Identifier>>(e) {
                                    @Override
                                    public void react(Map<Identifier, Identifier> identifierMap) {
                                        onResponse.call(new ChildrenResponse(identifierMap));
                                    }
                                });
                            }
                        }
                    });
                }
            });
        } else {    //if client is forcing us to do layer-by-layer cmp
            core.getStorage().getIdsAndXorsByParentId(request.parent_id, new ACB<Map<Identifier, Identifier>>(e) {
                @Override
                public void react(Map<Identifier, Identifier> identifierMap) {
                    onResponse.call(new ChildrenResponse(identifierMap));
                }
            });
        }
    }

}
