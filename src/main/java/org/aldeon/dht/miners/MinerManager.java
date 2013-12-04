package org.aldeon.dht.miners;

import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;

import java.util.HashMap;
import java.util.Map;

/**
 * Makes sure that every non-zero demand is associated with active miners
 */
public class MinerManager{

    private final DemandWatcher watcher;
    private final Map<Identifier, Miner> miners;
    private final MinerProvider provider;

    public MinerManager(DemandWatcher watcher, MinerProvider provider) {
        this.watcher = watcher;
        this.miners = new HashMap<>();
        this.provider = provider;

        watcher.onUpdate(new Callback<Identifier>() {
            @Override
            public void call(Identifier val) {
                awake(val);
            }
        });
    }

    private void awake(Identifier topic) {
        Miner miner = getMiner(topic);
        if(! miner.isWorking()) {
            miner.work();
        }
    }

    private Miner getMiner(Identifier topic) {
        Miner miner = miners.get(topic);

        if(miner == null) {
            miner = provider.getMiner(topic, watcher.getDemand(topic));
            miners.put(topic, miner);
        }

        return miner;
    }
}
