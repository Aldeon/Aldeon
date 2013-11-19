package org.aldeon.sync;

import org.aldeon.net.PeerAddress;

/**
 * Connection context related to a specified peer.
 */
public interface Slot<T extends PeerAddress> {

    /**
     * Slot state
     * @return
     */
    SlotState getSlotState();

    /**
     * Updates the slot state
     * @param state
     */
    void setSlotState(SlotState state);

    /**
     * If true, a procedure related to changing the state is currently running
     * @return
     */
    boolean getInProgress();

    /**
     * Changes the inProgress flag
     * @param inProgress
     */
    void setInProgress(boolean inProgress);

    /**
     * Peer assigned to this slot
     * @return
     */
    T getPeerAddress();

    /**
     * asdasdasd
     * @param address
     */
    void setPeerAddress(T address);

    /**
     * Last known clock value returned by the peer
     * @return
     */
    long getClock();

    /**
     * Updates the clock value
     * @param clock
     */
    void setClock(long clock);

    /**
     * Indicated the desired behavior related to this state (ex. how to cope with timeouts)
     * @return
     */
    SlotType getSlotType();

    /**
     * Points to a desired peer type
     * @return
     */
    Class<T> getAddressType();
}
