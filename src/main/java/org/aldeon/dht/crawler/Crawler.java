package org.aldeon.dht.crawler;

import org.aldeon.dht.Dht;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.common.Sender;

import java.util.Set;

/**
 * Constantly communicates with other peers in order to
 * broaden our knowledge about the network structure.
 *
 * Tasks:
 *  - find peers with ids closest to our address hash (hashes).
 *  - fulfill non-zero demands for peers interested in particular topics       // done
 *  - remove unresponsive peers from our dht structures                        // done
 */
public class Crawler {

    private static final int MAX_ACTIVE_JOBS = 8;
    private static final int REQUESTS_PER_JOB = 4;

    private final Dht dht;
    private final Sender sender;
    private final JobManager jobManager;


    public Crawler(Dht dht, Sender sender) {
        this.dht = dht;
        this.sender = sender;
        jobManager = new JobManagerImpl();
    }

    private int demand(Job job) {
        return dht.interestTracker().getDemand(job.addressType(),job.topic());
    }

    private PeerAddress nextTarget(Job job) {
        return null;
    }

    private void runJob(final Job job) {
        final PeerAddress peer = nextTarget(job);
        if(peer == null) {
            jobManager.makeInactive(job);
        } else {
            sender.addTask(new GetRelevantPeersTask(peer, job.topic(), dht.interestTracker(), dht.closenessTracker(), new Callback<Boolean>() {
                @Override
                public void call(Boolean peerResponded) {
                    if(!peerResponded) {
                        dht.interestTracker().delAddress(peer);
                        dht.closenessTracker().delAddress(peer);
                    }
                    if(demand(job) > 0) {
                        jobManager.makeInactiveAndReinsert(job);
                    } else {
                        jobManager.makeInactive(job);
                    }
                }
            }));
        }
    }

    public void rerun() {
        Set<Job> jobsToRun = jobManager.popAndMakeJobsActive(MAX_ACTIVE_JOBS);
        for(Job job: jobsToRun) {
            runJob(job);
        }
    }

    public void notifyDemandChanged(AddressType addressType, Identifier topic) {
        Job job = new Job(topic, addressType);
        if(demand(job) > 0) {
            jobManager.maintainJobCount(job, REQUESTS_PER_JOB);
        }
        rerun();
    }

}
