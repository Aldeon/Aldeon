package org.aldeon.dht2.miner;

import org.aldeon.networking.common.PeerAddress;
import org.aldeon.utils.various.Provider;

public class PeerMiner {
    private final Provider<PeerAddress> addressSource;
    private final Provider<Integer> demandSource;

    public PeerMiner(Provider<PeerAddress> addressSource, Provider<Integer> demandSource) {
        this.addressSource = addressSource;
        this.demandSource = demandSource;
    }
}
