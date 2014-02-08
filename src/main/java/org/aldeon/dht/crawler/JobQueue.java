package org.aldeon.dht.crawler;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;
import org.aldeon.events.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class JobQueue  {

    private static final Logger log = LoggerFactory.getLogger(JobQueue.class);
    private static final int MAX_CONCURRENT_JOBS = 2;

    private final Queue<Job> jobs = new ConcurrentLinkedQueue<>();
    private final AtomicInteger working = new AtomicInteger(0);
    private final Multiset<Job> counter = ConcurrentHashMultiset.create();
    private Callback<Job> worker;

    public void setWorker(Callback<Job> worker) {
        this.worker = worker;
    }

    public void dispatch() {
        for(int i = working.getAndSet(MAX_CONCURRENT_JOBS); i < MAX_CONCURRENT_JOBS; ++i) {
            Job job = jobs.poll();
            if(job == null) {
                working.decrementAndGet();
            } else {
                worker.call(job);
            }
        }
    }

    public void markAsDone(Job job) {
        if(counter.remove(job)) {
            working.decrementAndGet();
        }
    }

    public void add(Job job) {
        jobs.add(job);
        counter.add(job);
    }

    public int count(Job job) {
        return counter.count(job);
    }
}
