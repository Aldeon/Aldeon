package org.aldeon.dht;

import org.aldeon.communication.Sender;
import org.aldeon.dht.tasks.ObtainRelevantPeersTask;
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
 * Digs through dht in order to find peers interested in a particular topic
 */
public class Miner<T extends PeerAddress>{

    private static final Logger log = LoggerFactory.getLogger(Miner.class);

    private final Identifier topic;
    private final Sender<T> sender;
    private final Executor executor;
    private final Class<T> classOfT;
    private final Provider<Integer> demand;
    private final Provider<T> targetProvider;
    private final Callback<Pair<Identifier, T>> onFound;
    private boolean working = false;

    public Miner(Identifier topic, Sender<T> sender, Executor executor, Class<T> classOfT, Provider<Integer> demand, Callback<Pair<Identifier, T>> onFound, Provider<T> targetProvider) {
        this.topic = topic;
        this.sender = sender;
        this.executor = executor;
        this.classOfT = classOfT;
        this.demand = demand;
        this.onFound = onFound;
        this.targetProvider = targetProvider;
    }

    public boolean isWorking() {
        return working;
    }

    public void work() {
        if(working == false) {
            working = true;
            executor.execute(digTask());
        }
    }

    private void dig() {

        // Decide who to ask
        T peer = targetProvider.get();

        if(peer != null) {
            sender.addTask(new ObtainRelevantPeersTask<>(peer, topic, executor, onResponse(), onThrowable()));
        } else {
            working = false;
        }
    }

    private Runnable digTask() {
        return new Runnable() {
            @Override
            public void run() {
                // Some form of asynchronous timeout would be useful here
                // check if we should keep on working
                if(demand.get() > 0) {
                    dig();
                } else {
                    working = false;
                }
            }
        };
    }

    private Callback<RelevantPeersResponse> onResponse() {
        return new Callback<RelevantPeersResponse>() {
            @Override
            public void call(RelevantPeersResponse response) {
                for(PeerAddress address: response.closestIds) {

                    if(classOfT.isInstance(address)) {
                        onFound.call(new Pair<>((Identifier) null, (T) address));
                    } else {
                        log.info("Response contains inappropriate address type");
                    }
                }

                for(PeerAddress address: response.interested) {
                    if(classOfT.isInstance(address)) {
                        onFound.call(new Pair<>(topic, (T) address));
                    } else {
                        log.info("Response contains inappropriate address type");
                    }
                }
                executor.execute(digTask());
            }
        };
    }

    private Callback<Throwable> onThrowable() {
        return new Callback<Throwable>() {
            @Override
            public void call(Throwable e) {
                log.info("Failed to handle the ObtainRelevantPeersTask response", e);
                executor.execute(digTask());
            }
        };
    }

}
