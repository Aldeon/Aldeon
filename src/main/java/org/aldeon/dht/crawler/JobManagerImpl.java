package org.aldeon.dht.crawler;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

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
    public synchronized Set<Job> popAndMakeJobsActive(int maxActiveJobs) {
        Set<Job> result = new HashSet<>();
        int toPop = maxActiveJobs - active.size();
        for(int i = 0; i < toPop; ++i) {
            Job job = queue.poll();
            result.add(job);
            active.add(job);
        }
        return result;
    }
}
