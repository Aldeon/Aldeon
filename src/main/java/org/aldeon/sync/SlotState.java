package org.aldeon.sync;

/**
 * Represents the state of a synchronisation process
 * between a client and a server.
 */
public enum SlotState {

    /**
     * No peer is assigned to the slot
     */
    EMPTY,

    /**
     * Peer is found, but synchronisation did not yet complete
     */
    SYNC_IN_PROGRESS,

    /**
     * Client did synchronise with a peer, but the next delta is required
     */
    IN_SYNC_TIMEOUT,

    /**
     * Client did synchronise and no delta is necessary at this time
     */
    IN_SYNC_ON_TIME
}
