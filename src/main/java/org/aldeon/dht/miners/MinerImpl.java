package org.aldeon.dht.miners;

import org.aldeon.communication.Sender;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.response.RelevantPeersResponse;
import org.aldeon.utils.collections.Pair;
import org.aldeon.utils.collections.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Iteratively looks for peers interested in a particular topic
 * in order to satisfy a given demand.
 */
public class MinerImpl implements Miner {

    private static final Logger log = LoggerFactory.getLogger(MinerImpl.class);

    private final Sender sender;
    private final Runnable loopRunnable;
    private final Identifier topic;
    private final Callback<RelevantPeersResponse> onResponse;
    private final Callback<Throwable> onThrowable;
    private boolean working = false;

    public MinerImpl(
            Sender sender,
            final Identifier topic,
            final Provider<Integer> demand,
            final Provider<PeerAddress> targetProvider,
            final Callback<Pair<PeerAddress, Identifier>> onFound)
    {
        this.sender = sender;
        this.topic = topic;

        this.loopRunnable = new Runnable() {
            @Override
            public void run() {
                if(demand.get() > 0) {
                    sendRequest(targetProvider.get());
                } else {
                    working = false;
                }
            }
        };

        this.onResponse = new Callback<RelevantPeersResponse>() {
            @Override
            public void call(RelevantPeersResponse response) {

                for(PeerAddress address: response.closestIds) {
                    onFound.call(new Pair<>(address, (Identifier) null));
                }
                for(PeerAddress address: response.interested) {
                    onFound.call(new Pair<>(address, topic));
                }

                loop();
            }
        };

        this.onThrowable = new Callback<Throwable>() {
            @Override
            public void call(Throwable cause) {
                log.warn("Failed to retrieve a RelevantPeersResponse", cause);
                loop();
            }
        };
    }

    @Override
    public void work() {
        if(!isWorking()) {
            loop();
        }
    }

    private void loop() {
        loopRunnable.run();
    }

    @Override
    public boolean isWorking() {
        return working;
    }

    private void sendRequest(PeerAddress peer) {
        sender.addTask(new ObtainRelevantPeersTask(peer, topic, onResponse, onThrowable));
    }
}
