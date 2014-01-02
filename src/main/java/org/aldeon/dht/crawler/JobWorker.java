package org.aldeon.dht.crawler;

import org.aldeon.dht.Dht;
import org.aldeon.events.Callback;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.common.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobWorker {

    private static final Logger log = LoggerFactory.getLogger(JobWorker.class);

    private final Dht dht;
    private final Sender sender;
    private final TargetPicker picker;

    public JobWorker(Dht dht, Sender sender, TargetPicker picker) {
        this.dht = dht;
        this.sender = sender;
        this.picker = picker;
    }

    public void process(final Job job, final Callback<Boolean> workDone) {
        final PeerAddress peer = picker.findTarget(job);
        if(peer == null) {
            log.info("No target found. Is the DHT empty?");
            workDone.call(false);
        } else {
            sender.addTask(new GetRelevantPeersTask(null, job.topic(), dht, new Callback<Boolean>() {
                @Override
                public void call(Boolean responseReceived) {

                    if(!responseReceived) {
                        // remove address from our dht
                        log.info("Failed to receive response from peer (" + peer + "). Removing from DHT.");
                        dht.interestTracker().delAddress(peer);
                        dht.closenessTracker().delAddress(peer);
                    }

                    int demand = dht.interestTracker().getDemand(job.addressType(), job.topic());

                    workDone.call(demand == 0);
                }
            }));
        }
    }
}
