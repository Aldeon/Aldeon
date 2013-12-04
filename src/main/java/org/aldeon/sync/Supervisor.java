package org.aldeon.sync;

import org.aldeon.sync.procedures.CheckTimeoutProcedure;
import org.aldeon.sync.procedures.DeltaDownloadingProcedure;
import org.aldeon.sync.procedures.PeerFindingProcedure;
import org.aldeon.sync.procedures.SynchronizationProcedure;
import org.aldeon.utils.collections.Provider;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class Supervisor  implements Runnable{

    private final TopicManager manager;
    private final Executor executor;

    private final Map<SlotState, SlotStateUpgradeProcedure> procedures;

    public Supervisor(TopicManager manager, Executor executor) {
        this.manager = manager;
        this.executor  = executor;

        Provider<Long> timeProvider = new Provider<Long>() {
            @Override
            public Long get() {
                return System.currentTimeMillis();
            }
        };

        procedures = new HashMap<>();

        procedures.put(SlotState.EMPTY,             new PeerFindingProcedure());
        procedures.put(SlotState.SYNC_IN_PROGRESS,  new SynchronizationProcedure(timeProvider));
        procedures.put(SlotState.IN_SYNC_TIMEOUT,   new DeltaDownloadingProcedure(timeProvider));
        procedures.put(SlotState.IN_SYNC_ON_TIME,   new CheckTimeoutProcedure(timeProvider));
    }

    @Override
    public void run() {

        for(final TopicState topic: manager.getTopicStates()) {
            for(final Slot slot: topic.getSlots()) {

                if(!slot.getInProgress()) {

                    // Here we pick an appropriate procedure
                    final SlotStateUpgradeProcedure proc = null;

                    slot.setInProgress(true);

                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            proc.call(slot, topic.getIdentifier());
                        }
                    });
                }

            }
        }

    }
}
