package org.aldeon.sync.tasks;

import org.aldeon.utils.various.Reducer;

public class SyncResult {
    public int messagesDownloaded = 0;
    public int messagesSuggested = 0;
    public int failedRequests = 0;
    public int purposefulErrors = 0;
    public int accidentalErrors = 0;

    public static SyncResult noChanges() {
        return new SyncResult();
    }

    public static SyncResult requestFailed() {
        SyncResult result = new SyncResult();
        result.failedRequests++;
        return result;
    }

    public static SyncResult accidentalError() {
        SyncResult result = new SyncResult();
        result.accidentalErrors++;
        return result;
    }

    public static SyncResult purposefulError() {
        SyncResult result = new SyncResult();
        result.purposefulErrors++;
        return result;
    }

    public static SyncResult messageSuggested() {
        SyncResult result = new SyncResult();
        result.messagesSuggested++;
        return result;
    }

    public static SyncResult messageDownloaded() {
        SyncResult result = new SyncResult();
        result.messagesDownloaded++;
        return result;
    }

    public static class SyncResultReducer implements Reducer<SyncResult> {
        @Override
        public SyncResult reduce(SyncResult a, SyncResult b) {
            SyncResult result = new SyncResult();

            result.messagesSuggested    = a.messagesSuggested + b.messagesSuggested;
            result.accidentalErrors     = a.accidentalErrors + b.accidentalErrors;
            result.failedRequests       = a.failedRequests + b.failedRequests;
            result.messagesDownloaded   = a.messagesDownloaded + b.messagesDownloaded;
            result.purposefulErrors     = a.purposefulErrors + b.purposefulErrors;

            return result;
        }
    }
}
