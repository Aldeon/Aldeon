package org.aldeon.dht.miners;

import org.aldeon.model.Identifier;
import org.aldeon.utils.various.Provider;

public interface MinerProvider {
    Miner getMiner(Identifier topic, Provider<Integer> demand);
}
