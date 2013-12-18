package org.aldeon.dht.crawler;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class JobManagerImpl implements JobManager {

    private final Queue<Job> queue = new LinkedList<>();
    private final Multiset<Job> active = HashMultiset.create();
    private final Multiset<Job> total = HashMultiset.create();

    @Override
    public synchronized void maintainJobCount(Job job, int count) {
        int toInsert = count - total.count(job);

        for(int i = 0; i < toInsert; ++i) {
            queue.add(job);
            total.add(job);
        }
    }

    @Override
    public synchronized void makeInactive(Job job) {
        active.remove(job);
        total.remove(job);
    }

    @Override
    public synchronized void makeInactiveAndReinsert(Job job) {
        active.remove(job);
        queue.add(job);
    }

    @Override
    public synchronized List<Job> popAndMakeJobsActive(int maxActiveJobs) {
        List<Job> result = new LinkedList<>();
        int toPop = maxActiveJobs - active.size();
        int limit = Math.min(toPop, queue.size());

        for(int i = 0; i < limit; ++i) {
            Job job = queue.poll();
            result.add(job);
            active.add(job);
        }
        return result;
    }
}
