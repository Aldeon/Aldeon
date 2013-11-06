package org.aldeon.protocol.action;

import org.aldeon.core.Core;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.protocol.Action;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.CompareTreesRequest;
import org.aldeon.protocol.response.ChildrenResponse;
import org.aldeon.protocol.response.LuckyGuessResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
    public void respond(final CompareTreesRequest request, final Callback<Response> onResponse, final Executor executor) {

        if (request.force == false) { //if client is not forcing us to do layer-by-layer cmp

            //get xor value for this parent msg from database
            core.getStorage().getMessageXorByIdentifier(request.parent_id, new Callback<Identifier>() {
                @Override
                public void call(Identifier val) {
                    Identifier guess_xor = null;
                    //Identifier guess_xor =  arithmetic.xor(val, request.parent_xor)

                        //attempt a lucky guess
                        core.getStorage().getMessageIdentifierByXor(guess_xor, new Callback<Identifier>() {
                            @Override
                            public void call(Identifier val) {
                                if (val != null) { //msg found = lucky guess successful
                                    onResponse.call(new LuckyGuessResponse(val));
                                } else { //there is no XOR match (lucky guess failed) fall back to comparing trees layer by layer

                                    core.getStorage().getIdsAndXorsByParent(request.parent_id, new Callback<Map<Identifier, Identifier>>() {
                                        @Override
                                        public void call(Map<Identifier, Identifier> identifierMap) {
                                            onResponse.call(new ChildrenResponse((HashMap<Identifier, Identifier>)identifierMap));
                                            }
                                    }, executor);
                                }
                            }
                        }, executor);

                }
            }, executor );
        } else {    //if client is forcing us to do layer-by-layer cmp
            core.getStorage().getIdsAndXorsByParent(request.parent_id, new Callback<Map<Identifier, Identifier>>() {
                @Override
                public void call(Map<Identifier, Identifier> identifierMap) {
                    onResponse.call(new ChildrenResponse((HashMap<Identifier, Identifier>)identifierMap));
                    }
            }, executor);
        }
    }

}
