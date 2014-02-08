package org.aldeon.dht.crawler;

import org.aldeon.networking.common.PeerAddress;

public interface TargetPicker {
    PeerAddress findTarget(Job job);
}
