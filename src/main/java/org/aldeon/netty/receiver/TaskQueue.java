package org.aldeon.netty.receiver;

import org.aldeon.common.nio.task.InboundRequestTask;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class TaskQueue {

    private BlockingQueue<InboundRequestTask> q;
    private static final int TIMEOUT = 100;
    private boolean closed = false;

    public TaskQueue(){
        q = new LinkedBlockingDeque<>();
    }

    public void addTask(InboundRequestTask task) {
        q.add(task);
    }

    public InboundRequestTask getTask() {
        while(!closed) {
            try {
                InboundRequestTask ret = q.poll(TIMEOUT, TimeUnit.MILLISECONDS);   // TODO: get rid of timeout
                if(ret != null) {
                    return ret;
                }
            } catch (InterruptedException e) { }
        }
        return null;
    }

    public void close() {
        closed = true;
    }
}
