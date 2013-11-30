package org.aldeon.dht;

import org.aldeon.communication.Sender;
import org.aldeon.dht.tasks.ObtainRelevantPeersTask;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.response.RelevantPeersResponse;
import org.aldeon.utils.collections.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.Executor;

/**
 * Digs through dht in order to find peers interested in a particular topic
 */
public class Miner<T extends PeerAddress>{

    private static final Logger log = LoggerFactory.getLogger(Miner.class);

    private final Identifier topic;
    private final Sender<T> sender;
    private final Executor executor;
    private final Dht<T> dht;
    private final Class<T> classOfT;
    private final Provider<Integer> demand;
    private boolean working = false;

    public Miner(Identifier topic, Sender<T> sender, Executor executor, Dht<T> dht, Class<T> classOfT, Provider<Integer> demand) {
        this.topic = topic;
        this.sender = sender;
        this.executor = executor;
        this.dht = dht;
        this.classOfT = classOfT;
        this.demand = demand;
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
        T peer = fetchClosestAddress();

        if(peer != null) {
            sender.addTask(new ObtainRelevantPeersTask<T>(peer, topic, executor, onResponse(), onThrowable()));
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

    private T fetchClosestAddress() {
        Set<T> closest = dht.getNearest(topic, 1);
        return closest.isEmpty() ? null : closest.iterator().next();
    }

    private Callback<RelevantPeersResponse> onResponse() {
        return new Callback<RelevantPeersResponse>() {
            @Override
            public void call(RelevantPeersResponse response) {
                for(PeerAddress address: response.closestIds) {

                    if(classOfT.isInstance(address)) {
                        dht.registerUncertainAddress((T) address, null);
                    } else {
                        log.info("Response contains inappropriate address type");
                    }
                }

                for(PeerAddress address: response.interested) {
                    if(classOfT.isInstance(address)) {
                        dht.registerUncertainAddress((T) address, topic);
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
