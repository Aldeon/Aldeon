package org.aldeon.sync;

import org.aldeon.events.Callback;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.PeerAddress;

/**
 * Connection context related to a specified peer.
 */
public class Slot {

    private SlotState state = SlotState.EMPTY;
    private boolean inProgress = false;
    private PeerAddress peer;
    private AddressType addressType;
    private long clock = Long.MIN_VALUE;
    private long lastUpdated = Long.MIN_VALUE;
    private SlotType slotType;
    private Runnable revoke;
    private Callback<PeerAddress> bountyHandler;

    public Slot(SlotType slotType, AddressType addressType) {
        this.slotType = slotType;
        this.addressType = addressType;
    }

    /**
     * Slot state
     * @return
     */
    public SlotState getSlotState() {
        return state;
    }

    /**
     * Updates the slot state
     * @param state
     */
    public void setSlotState(SlotState state) {
        this.state = state;
    }

    /**
     * If true, a procedure related to changing the state is currently running
     * @return
     */
    public boolean getInProgress() {
        return inProgress;
    }

    /**
     * Changes the inProgress flag
     * @param inProgress
     */
    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    /**
     * Requested peer type
     */
    public AddressType getAddressType() {
        return addressType;
    }

    /**
     * Peer assigned to this slot
     * @return
     */
    public PeerAddress getPeerAddress() {
        return peer;
    }

    /**
     * asdasdasd
     * @param peer
     */
    public void setPeerAddress(PeerAddress peer) {

        this.peer = peer;
    }

    /**
     * Last known clock value returned by the peer
     * @return
     */
    public long getClock() {
        return clock;
    }

    /**
     * Updates the clock value
     * @param clock
     */
    public void setClock(long clock) {
        this.clock = clock;
    }

    /**
     * Sets the point in time when last delta was downloaded
     * @param lastUpdated
     */
    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * Point in time when last delta was downloaded
     * @return
     */
    public long getLastUpdated() {
        return lastUpdated;
    }

    public Callback<PeerAddress> getBountyHandler() {
        return bountyHandler;
    }

    public void setBountyHandler(Callback<PeerAddress> bountyHandler) {
        this.bountyHandler = bountyHandler;
    }

    /**
     * Indicated the desired behavior related to this state (ex. how to cope with timeouts)
     * @return
     */
    public SlotType getSlotType() {
        return slotType;
    }
}
