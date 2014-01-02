package org.aldeon.dht.crawler;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;
import org.aldeon.dht.Dht;
import org.aldeon.dht.crawler.pickers.PickerModule;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.model.Service;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.common.Sender;
import org.aldeon.utils.various.LoopWorker;

public class Crawler implements Service {

    private static final long INTERVAL = 1000;
    private static final long JOBS_PER_DEMAND = 1;

    private LoopWorker loop;
    private final JobWorker worker;
    private final JobQueue queue;
    private final Multiset<Job> activeJobs = ConcurrentHashMultiset.create();

    public Crawler(Dht dht, Sender sender) {

        Callback<Job> workCallback = new Callback<Job>() {
            @Override
            public void call(final Job job) {
                activeJobs.add(job);
                worker.process(job, new Callback<Boolean>() {
                    @Override
                    public void call(Boolean workDone) {
                        if(!workDone) {
                            addJob(job);
                        }
                        activeJobs.remove(job);
                    }
                });
            }
        };

        worker = new JobWorker(dht, sender, PickerModule.create(dht));
        queue = new JobQueue(workCallback);

        loop = new LoopWorker(INTERVAL, new Runnable() {
            @Override
            public void run() {
                queue.dispatch();
            }
        });
    }

    protected void addJob(Job job) {
        queue.add(job);
    }

    @Override
    public void start() {
        loop.start();
    }

    @Override
    public void close() {
        loop.close();
    }

    public void handleDemand(Identifier topic, AddressType addressType) {
        Job job = new Job(topic, addressType);
        for(int i = queue.count(job) + activeJobs.count(job); i < JOBS_PER_DEMAND; ++i) {
            queue.add(job);
        }
    }
}
