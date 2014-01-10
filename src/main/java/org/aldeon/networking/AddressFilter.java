package org.aldeon.networking;

import org.aldeon.networking.common.PeerAddress;
import org.aldeon.utils.various.Predicate;

public class AddressFilter implements Predicate<PeerAddress> {

    private final NetworkService service;

    public AddressFilter(NetworkService service) {
        this.service = service;
    }

    @Override
    public boolean check(PeerAddress argument) {
        return (service.addressBelieveable(argument));
    }
}
