package org.aldeon.dht.crawler;

import org.aldeon.dht.interest.InterestTracker;
import org.aldeon.networking.common.PeerAddress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Picks a new target from a set of closest identifiers
 */
public class SemiRandomTargetPicker implements TargetPicker{

    private final int range;
    private final InterestTracker tracker;

    public SemiRandomTargetPicker(int range, InterestTracker tracker) {
        this.tracker = tracker;
        this.range = range;
    }

    @Override
    public PeerAddress findTarget(Job job) {
        Set<PeerAddress> nearest = tracker.getInterested(job.addressType(), job.topic(), range);
        if(nearest.isEmpty()) {
            return null;
        } else {
            List<PeerAddress> list = new ArrayList<>(nearest);
            Collections.shuffle(list);
            return list.get(0);
        }
    }

}
