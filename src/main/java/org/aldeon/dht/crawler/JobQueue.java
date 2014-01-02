package org.aldeon.dht.crawler;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;
import org.aldeon.events.Callback;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class JobQueue  {

    private static final int MAX_CONCURRENT_JOBS = 2;

    private final Queue<Job> jobs = new ConcurrentLinkedQueue<>();
    private final AtomicInteger working = new AtomicInteger();
    private final Multiset<Job> counter = ConcurrentHashMultiset.create();
    private final Callback<Job> worker;

    public JobQueue(Callback<Job> worker) {
        this.worker = worker;
    }

    public void dispatch() {
        for(int i = working.getAndSet(MAX_CONCURRENT_JOBS); i < MAX_CONCURRENT_JOBS; ++i) {
            Job job = jobs.poll();
            counter.remove(job);
            if(job != null) {
                worker.call(job);
            }
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
