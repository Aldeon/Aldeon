package org.aldeon.sync.tasks;

import org.aldeon.utils.various.Reducer;

public class SyncResultReducer implements Reducer<SyncResult> {
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
