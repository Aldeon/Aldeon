package org.aldeon.sync.tasks;

public class DiffResult {
    public int messagesDownloaded = 0;
    public int failedRequests = 0;

    public static DiffResult requestFailed() {
        DiffResult result = new DiffResult();
        result.failedRequests++;
        return result;
    }
}

