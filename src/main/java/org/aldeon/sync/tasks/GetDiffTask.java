package org.aldeon.sync.tasks;

import org.aldeon.db.Db;
import org.aldeon.events.Callback;
import org.aldeon.events.SingleRunCallback;
import org.aldeon.model.Identifier;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.common.Sender;
import org.aldeon.protocol.Response;
import org.aldeon.protocol.request.GetDiffRequest;
import org.aldeon.protocol.response.DiffResponse;
import org.aldeon.utils.collections.DependencyDispatcher;
import org.aldeon.utils.collections.DependencyDispatcherModule;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class GetDiffTask extends AbstractOutboundTask<GetDiffRequest> {

    private final Db db;
    private final Sender sender;
    private final Callback<DiffResult> onFinished;
    private final AtomicInteger messagesDownloaded = new AtomicInteger(0);
    private final AtomicInteger failedRequests = new AtomicInteger(0);
    private final AtomicInteger accidents = new AtomicInteger(0);
    private Long receivedClock;

    public GetDiffTask(PeerAddress peerAddress, Identifier topic, long clock, Db db, Sender sender, Callback<DiffResult> onFinished) {
        super(peerAddress);

        setRequest(new GetDiffRequest());
        getRequest().clock = clock;
        getRequest().topic = topic;

        this.db = db;
        this.sender = sender;
        this.onFinished = new SingleRunCallback<>(onFinished);

        this.receivedClock = null;
    }

    @Override
    public void onSuccess(Response response) {
        if(response instanceof DiffResponse) {
            onDiff((DiffResponse) response);
        } else {
            onFinished.call(DiffResult.requestFailed());
        }
    }

    private void onDiff(DiffResponse response) {
        receivedClock = response.clock;
        DependencyDispatcher<Identifier> dispatcher = DependencyDispatcherModule.create(response.ids);
        dispatchDownloads(dispatcher, response.ids);
    }

    private void dispatchDownloads(final DependencyDispatcher<Identifier> dispatcher, final Map<Identifier, Identifier> parents) {

        while(true) {
            final Identifier downloadTarget = dispatcher.next();

            if(downloadTarget == null) {
                break;
            }

            // TODO: check if the message exists in our database
            // TODO: check if every identifier is in the appropriate topic (!!!)

            sender.addTask(downloadTask(downloadTarget, parents.get(downloadTarget), new Callback<DownloadMessageTask.Result>() {
                @Override
                public void call(DownloadMessageTask.Result downloadResult) {
                    switch(downloadResult) {
                        case MESSAGE_INSERTED:
                            // Great, proceed.
                            messagesDownloaded.incrementAndGet();
                            dispatcher.remove(downloadTarget);
                            break;

                        case MESSAGE_EXISTS:
                            // We must have downloaded it from someone else in the meantime. Proceed.
                            dispatcher.remove(downloadTarget);
                            break;

                        case MESSAGE_NOT_ON_SERVER:
                            // Server must have removed it after sending us the diff
                            accidents.incrementAndGet();
                            dispatcher.removeRecursively(downloadTarget);
                            break;

                        case PARENT_UNKNOWN:
                            // We must have deleted the branch. Do not download.
                            accidents.incrementAndGet();
                            dispatcher.removeRecursively(downloadTarget);
                            break;

                        case CHECK_FAILED:
                            // Very suspicious - the server may be spamming us
                            failedRequests.incrementAndGet();
                            dispatcher.removeRecursively(downloadTarget);

                        case COMMUNICATION_ERROR:
                            failedRequests.incrementAndGet();
                            dispatcher.removeRecursively(downloadTarget);
                            break;
                    }
                    // Finally, rerun the loop and check if the work is done
                    dispatchDownloads(dispatcher, parents);
                }
            }));
        }

        if(dispatcher.isFinished()) {
            DiffResult result = new DiffResult();
            result.messagesDownloaded = messagesDownloaded.get();
            result.failedRequests = failedRequests.get();
            result.accidents = accidents.get();
            result.clock = receivedClock;
            onFinished.call(result);
        }
    }

    private DownloadMessageTask downloadTask(Identifier id, Identifier parent, Callback<DownloadMessageTask.Result> onOperationCompleted) {
        return new DownloadMessageTask(getAddress(), id, parent, false, db, onOperationCompleted);
    }

    @Override
    public void onFailure(Throwable cause) {
        onFinished.call(DiffResult.requestFailed());
    }
}
