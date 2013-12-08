package org.aldeon.networking.wrappers;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import org.aldeon.networking.common.Sender;
import org.aldeon.networking.common.OutboundRequestTask;
import org.aldeon.networking.common.AddressType;
import org.aldeon.networking.exceptions.AmbiguousSenderException;
import org.aldeon.networking.exceptions.SuitableSenderNotFoundException;

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
        for(Sender sender: senders.values()) {
            sender.start();
        }
    }

    @Override
    public void close() {
        for(Sender sender: senders.values()) {
            sender.close();
        }
    }

    @Override
    public Set<AddressType> acceptedTypes() {
        return Collections.unmodifiableSet(acceptedTypes);
    }
}
