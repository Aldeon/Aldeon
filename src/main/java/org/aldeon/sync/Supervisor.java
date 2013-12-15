package org.aldeon.sync;

import org.aldeon.core.Core;
import org.aldeon.core.CoreModule;
import org.aldeon.db.Db;
import org.aldeon.networking.common.Sender;
import org.aldeon.sync.procedures.CheckTimeoutProcedure;
import org.aldeon.sync.procedures.DiffDownloadingProcedure;
import org.aldeon.sync.procedures.PeerFindingProcedure;
import org.aldeon.sync.procedures.SynchronizationProcedure;
import org.aldeon.utils.various.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class Supervisor implements Runnable{

    private static final Logger log = LoggerFactory.getLogger(Supervisor.class);

    private final TopicManager manager;
    private final Executor executor;

    private final Map<SlotState, SlotStateUpgradeProcedure> procedures = new HashMap<>();

    public Supervisor(Core core, TopicManager manager, Executor executor) {
        this.manager = manager;
        this.executor  = executor;

        Provider<Long> timeProvider = new Provider<Long>() {
            @Override
            public Long get() {
                return System.currentTimeMillis();
            }
        };

        Db db = core.getStorage();
        Sender sender = core.getSender();
        // TODO: Wrap db and sender with threading-related decorators

        procedures.put(SlotState.EMPTY,             new PeerFindingProcedure());
        procedures.put(SlotState.SYNC_IN_PROGRESS,  new SynchronizationProcedure(db, sender));
        procedures.put(SlotState.IN_SYNC_TIMEOUT,   new DiffDownloadingProcedure(db, sender, timeProvider));
        procedures.put(SlotState.IN_SYNC_ON_TIME,   new CheckTimeoutProcedure(timeProvider));
    }

    @Override
    public void run() {

        // log.info("Supervisor loop");

        for(final TopicState topic: manager.getTopicStates()) {
            for(final Slot slot: topic.getSlots()) {

                if(!slot.getInProgress()) {

                    log.info("Slot " + slot + " not in progress. Calling procedure...");

                    final SlotStateUpgradeProcedure proc = procedures.get(slot.getSlotState());

                    if(proc == null) {
                        log.warn("Procedure for slot " + slot + " not found");
                    } else {
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
    }
}
