package org.aldeon.protocol.action;

import org.aldeon.core.Core;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.protocol.Action;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.CompareTreesRequest;
import org.aldeon.protocol.response.ChildrenResponse;
import org.aldeon.protocol.response.LuckyGuessResponse;

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

        //weź nasz xor wiadomości
        //core.getStorage().
        core.getStorage().getMessageXorByIdentifier(request.parent_id, new Callback<Identifier>() {
            @Override
            public void call(Identifier val) {

                Identifier guess_xor = null;
                //Identifier guess_xor =  arithmetic.xor(val, request.parent_xor)
                //pozbyc sie identifiable itp
                //zrobic jedna klase ktora bedzie zawierala jakis ciag bajtow

                //first - attempt a lucky guess
                core.getStorage().getMessageIdentifierByXor(guess_xor, new Callback<Identifier>() {
                    @Override
                    public void call(Identifier val) {
                        if (val != null) {
                            onResponse.call(new LuckyGuessResponse(val));
                        } else { //if there is no XOR match (lucky guess failed) fall back to comparing trees layer by layer

                            core.getStorage().getMessagesByParent(request.parent_id, new Callback<Set<Identifier>>() {

                                @Override
                                public void call(Set<Identifier> val) {
                                    onResponse.call(new ChildrenResponse(val));
                                }
                            }, executor);
                        }
                    }
                }, executor);
            }
        }, executor );
    }
}
