package org.aldeon.dht.crawler;


import java.util.Set;

public interface JobManager {
    void maintainJobCount(Job job, int count);
    void makeInactive(Job job);
    void makeInactiveAndReinsert(Job job);

    Set<Job> popAndMakeJobsActive(int maxActiveJobs);
}
