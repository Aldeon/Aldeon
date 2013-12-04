package org.aldeon.sync;

import java.util.concurrent.Executor;

public class Supervisor  implements Runnable{

    private final TopicManager manager;
    private final Executor executor;

    public Supervisor(TopicManager manager, Executor executor) {
        this.manager = manager;
        this.executor  = executor;
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
