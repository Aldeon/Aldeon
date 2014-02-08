package org.aldeon.dht.crawler.pickers;

import org.aldeon.dht.closeness.ClosenessTracker;
import org.aldeon.dht.crawler.Job;
import org.aldeon.dht.crawler.TargetPicker;
import org.aldeon.networking.common.PeerAddress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RingTargetPicker implements TargetPicker {

    private final ClosenessTracker tracker;
    private final int scatter;

    public RingTargetPicker(ClosenessTracker tracker, int scatter) {
        this.tracker = tracker;
        this.scatter = scatter;
    }

    @Override
    public PeerAddress findTarget(Job job) {
        List<PeerAddress> addresses = new ArrayList<>(tracker.getNearest(job.addressType(), job.topic(), scatter));
        if(addresses.size() > 0) {
            Collections.shuffle(addresses);
            return addresses.get(0);
        } else {
            return null;
        }
    }
}
