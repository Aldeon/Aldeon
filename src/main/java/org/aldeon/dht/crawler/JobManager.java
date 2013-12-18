package org.aldeon.dht.crawler;


import java.util.List;

public interface JobManager {
    void maintainJobCount(Job job, int count);
    void makeInactive(Job job);
    void makeInactiveAndReinsert(Job job);

    List<Job> popAndMakeJobsActive(int maxActiveJobs);
}
