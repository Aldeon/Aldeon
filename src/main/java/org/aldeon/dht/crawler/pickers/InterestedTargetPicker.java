package org.aldeon.dht.crawler.pickers;

import org.aldeon.dht.crawler.Job;
import org.aldeon.dht.crawler.TargetPicker;
import org.aldeon.dht.interest.InterestTracker;
import org.aldeon.networking.common.PeerAddress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Picks a new target from a set of peers (supposedly) interested in a topic
 */
public class InterestedTargetPicker implements TargetPicker {

    private final int range;
    private final InterestTracker tracker;

    public InterestedTargetPicker(InterestTracker tracker, int range) {
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
