package org.aldeon.dht.crawler;

import org.aldeon.model.Identifier;
import org.aldeon.networking.common.AddressType;

public class Job {

    private final Identifier topic;
    private final AddressType type;

    public Job(Identifier topic, AddressType type) {
        this.topic = topic;
        this.type = type;
    }

    public Identifier topic() {
        return topic;
    }

    public AddressType addressType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Job) {
            Job that = (Job) obj;
            return that.topic().equals(topic()) && that.addressType().equals(addressType());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return topic().hashCode() + addressType().hashCode();
    }
}
