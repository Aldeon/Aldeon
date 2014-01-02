package org.aldeon.dht.crawler.pickers;

import org.aldeon.dht.crawler.Job;
import org.aldeon.dht.crawler.TargetPicker;
import org.aldeon.networking.common.PeerAddress;

import java.util.List;

public class PickerDispatcher implements TargetPicker {

    private final List<TargetPicker> pickers;

    public PickerDispatcher(List<TargetPicker> pickers) {
        this.pickers = pickers;
    }

    @Override
    public PeerAddress findTarget(Job job) {
        for(TargetPicker picker: pickers) {
            PeerAddress result = picker.findTarget(job);
            if(result != null) {
                return result;
            }
        }
        return null;
    }
}
