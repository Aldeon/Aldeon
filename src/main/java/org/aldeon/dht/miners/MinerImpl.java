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

import java.util.concurrent.Executor;

/**
 * Iteratively looks for peers interested in a particular topic
 * in order to satisfy a given demand.
 */
public class MinerImpl<T extends PeerAddress> implements Miner {

    private static final Logger log = LoggerFactory.getLogger(MinerImpl.class);

    private final Sender<T> sender;
    private final Executor executor;
    private final Runnable loopRunnable;
    private final Identifier topic;
    private final Callback<RelevantPeersResponse> onResponse;
    private final Callback<Throwable> onThrowable;
    private boolean working = false;

    public MinerImpl(
            Sender<T> sender,
            Executor executor,
            final Identifier topic,
            final Class<T> expectedType,
            final Provider<Integer> demand,
            final Provider<T> targetProvider,
            final Callback<Pair<T, Identifier>> onFound)
    {
        this.sender = sender;
        this.executor = executor;
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
                for(PeerAddress addr: response.closestIds) {
                    if(expectedType.isInstance(addr)) {
                        onFound.call(new Pair<>((T) addr, (Identifier) null));
                    } else {
                        log.warn("Invalid address type received.");
                    }
                }
                for(PeerAddress addr: response.interested) {
                    if(expectedType.isInstance(addr)) {
                        onFound.call(new Pair<>((T) addr, topic));
                    } else {
                        log.warn("Invalid address type received.");
                    }
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
        executor.execute(loopRunnable);
    }

    @Override
    public boolean isWorking() {
        return working;
    }

    private void sendRequest(T peer) {
        sender.addTask(new ObtainRelevantPeersTask<>(peer, topic, executor, onResponse, onThrowable));
    }
}
