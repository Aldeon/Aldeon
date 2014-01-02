package org.aldeon.sync;

import org.aldeon.core.Core;
import org.aldeon.db.Db;
import org.aldeon.core.services.Service;
import org.aldeon.networking.common.Sender;
import org.aldeon.sync.procedures.CheckTimeoutProcedure;
import org.aldeon.sync.procedures.DiffDownloadingProcedure;
import org.aldeon.sync.procedures.PeerFindingProcedure;
import org.aldeon.sync.procedures.SynchronizationProcedure;
import org.aldeon.utils.various.LoopWorker;
import org.aldeon.utils.various.Provider;
import org.aldeon.utils.various.SystemTimeProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class Supervisor implements Service {

    private final TopicManager manager;
    private final Executor executor;
    private final LoopWorker loop;

    private final Map<SlotState, SlotStateUpgradeProcedure> procedures = new HashMap<>();

    public Supervisor(Core core, TopicManager manager, Executor executor) {

        this.manager = manager;
        this.executor  = executor;

        Provider<Long> timeProvider = new SystemTimeProvider();

        Db db = core.getStorage();
        Sender sender = core.getSender();
        // TODO: Wrap db and sender with threading-related decorators

        procedures.put(SlotState.EMPTY,             new PeerFindingProcedure());
        procedures.put(SlotState.SYNC_IN_PROGRESS,  new SynchronizationProcedure(db, sender));
        procedures.put(SlotState.IN_SYNC_TIMEOUT,   new DiffDownloadingProcedure(db, sender, timeProvider));
        procedures.put(SlotState.IN_SYNC_ON_TIME,   new CheckTimeoutProcedure(timeProvider));

        loop = new LoopWorker(1000, new Runnable() {
            @Override
            public void run() {
                updateSlots();
            }
        });
    }

    protected void updateSlots() {

        // log.info("Supervisor loop");

        for(final TopicState topic: manager.getTopicStates()) {
            for(final Slot slot: topic.getSlots()) {

                if(!slot.getInProgress()) {

                    final SlotStateUpgradeProcedure proc = procedures.get(slot.getSlotState());

                    slot.setInProgress(true);

                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            proc.handle(slot, topic.getIdentifier());
                        }
                    });
                }
            }
        }
    }

    @Override
    public void start() {
        loop.start();
    }

    @Override
    public void close() {
        loop.close();
    }
}
