package org.aldeon.core.senderforwarding;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import org.aldeon.communication.Sender;
import org.aldeon.communication.task.OutboundRequestTask;
import org.aldeon.networking.common.AddressType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SenderDispatcher implements Sender {

    private final Set<AddressType> acceptedTypes = new HashSet<>();
    private final SetMultimap<AddressType, Sender> senders;

    public SenderDispatcher(Set<Sender> s) {

        senders = HashMultimap.create();

        for(Sender sender: s) {
            acceptedTypes.addAll(sender.acceptedTypes());
            for(AddressType addressType: sender.acceptedTypes()) {
                senders.put(addressType, sender);
            }
        }
    }

    @Override
    public void addTask(OutboundRequestTask task) {
        Set<Sender> usableSenders = senders.get(task.getAddress().getType());

        if(usableSenders.size() == 1) {
            usableSenders.iterator().next().addTask(task);
        } else
        if(usableSenders.size() == 0) {
            task.onFailure(new SuitableSenderNotFoundException());
        } else {

            // Decide which sender to use
            // For now, we just throw an exception

            task.onFailure(new AmbiguousSenderException());
        }
    }

    @Override
    public void start() {
        // do nothing - senders already started
    }

    @Override
    public void close() {
        // do nothing - senders must be closed by the core
    }

    @Override
    public Set<AddressType> acceptedTypes() {
        return Collections.unmodifiableSet(acceptedTypes);
    }
}
