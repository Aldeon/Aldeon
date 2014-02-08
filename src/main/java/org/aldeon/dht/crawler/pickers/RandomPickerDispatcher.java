package org.aldeon.dht.crawler.pickers;

import org.aldeon.dht.crawler.Job;
import org.aldeon.dht.crawler.TargetPicker;
import org.aldeon.networking.common.PeerAddress;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class RandomPickerDispatcher implements TargetPicker {

    private final Set<TargetPicker> pickers;

    public RandomPickerDispatcher(Set<TargetPicker> pickers) {
        this.pickers = pickers;
    }

    @Override
    public PeerAddress findTarget(Job job) {
        List<TargetPicker> list = new LinkedList<>(pickers);
        Collections.shuffle(list);
        return new SequentialPickerDispatcher(list).findTarget(job);
    }
}
