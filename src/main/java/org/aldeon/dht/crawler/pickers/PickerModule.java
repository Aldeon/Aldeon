package org.aldeon.dht.crawler.pickers;

import com.google.common.collect.Sets;
import org.aldeon.dht.Dht;
import org.aldeon.dht.crawler.TargetPicker;

public class PickerModule {

    public static TargetPicker create(Dht dht) {
        return new RandomPickerDispatcher(Sets.<TargetPicker>newHashSet(
                new InterestedTargetPicker(dht.interestTracker(), 4),
                new RingTargetPicker(dht.closenessTracker(), 4)
        ));
    }
}
