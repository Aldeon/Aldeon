package org.aldeon.dht.crawler;

import org.aldeon.dht.Dht;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.Sender;

/**
 * Constantly communicates with other peers in order to
 * broaden our knowledge about the network structure.
 *
 * Tasks:
 *  - find peers with ids closest to our address hash (hashes).
 *  - fulfill non-zero demands for peers interested in particular topics
 *  - remove unresponsive peers from our dht structures
 */
public class Crawler {

    private final Dht dht;
    private final Sender sender;

    public Crawler(Dht dht, Sender sender) {
        this.dht = dht;
        this.sender = sender;
    }

    public void notifyDemandChanged(AddressType addressType, Identifier topic) {

    }

}
