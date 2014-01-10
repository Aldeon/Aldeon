package org.aldeon.networking.mediums.ip.nat.upnp;

import org.aldeon.networking.mediums.ip.nat.utils.AddressTranslation;
import org.aldeon.networking.common.Port;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.model.types.UnsignedIntegerTwoBytes;
import org.fourthline.cling.support.model.PortMapping;

import java.net.InetAddress;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class UpnpAddressTranslationFactory {

    private static final int LEASE_DURATION_SECONDS = 3600 * 24; //debug

    public static Future<AddressTranslation> create(Port desiredInternalPort, Port desiredExternalPort) {

        PortMapping pm = new PortMapping();
        pm.setDescription("Aldeon UPnP TCP");
        pm.setProtocol(PortMapping.Protocol.TCP);
        pm.setLeaseDurationSeconds(new UnsignedIntegerFourBytes(LEASE_DURATION_SECONDS));
        pm.setInternalPort(new UnsignedIntegerTwoBytes(desiredInternalPort.getIntValue()));
        pm.setExternalPort(new UnsignedIntegerTwoBytes(desiredExternalPort.getIntValue()));

        FutureImpl future = new FutureImpl();

        future.listener = new PortMappingAndIpListener(pm);
        future.upnp = new UpnpServiceImpl(future.listener);
        future.desiredInternalPort = desiredInternalPort;
        future.desiredExternalPort = desiredExternalPort;

        future.upnp.getControlPoint().search();

        return future;
    }

    private static class FutureImpl extends Thread implements Future<AddressTranslation> {

        private Port desiredInternalPort;
        private Port desiredExternalPort;
        private PortMappingAndIpListener listener;
        private UpnpService upnp;
        private boolean isCancelled = false;
        private boolean isDone = false;

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            if(isCancelled || isDone) {
                return false;
            } else {
                upnp.shutdown();
                isCancelled = true;
                if(mayInterruptIfRunning) {
                    this.interrupt();
                }
                return true;
            }
        }

        @Override
        public boolean isCancelled() {
            return isCancelled;
        }

        @Override
        public boolean isDone() {
            if(!isDone) {
                isDone = listener.getActiveMappings().size() > 0;
            }
            return isDone;
        }

        @Override
        public AddressTranslation get() throws InterruptedException {
            try {
                return get(Long.MAX_VALUE, TimeUnit.DAYS);
            } catch (TimeoutException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public AddressTranslation get(long timeout, TimeUnit unit) throws TimeoutException, InterruptedException {
            long milisecondsToWait = unit.toMillis(timeout);

            long start = System.currentTimeMillis();
            while(!isDone()) {
                if(System.currentTimeMillis() - start > milisecondsToWait) {
                    throw new TimeoutException();
                } else {
                    sleep(100);
                }
            }

            Set<PortMappingAndIpListener.AddressPair> addresses = listener.getActiveMappings();
            final PortMappingAndIpListener.AddressPair addressPair = addresses.iterator().next(); //we take only the first one

            return new AddressTranslation() {
                @Override
                public Port getInternalPort() {
                    return desiredInternalPort;
                }

                @Override
                public Port getExternalPort() {
                    return desiredExternalPort;
                }

                @Override
                public InetAddress getInternalAddress() {
                    return addressPair.getInternalAddress();
                }

                @Override
                public InetAddress getExternalAddress() {
                    return addressPair.getExternalAddress();
                }

                @Override
                public void shutdown() {
                    upnp.shutdown();
                }
            };
        }
    }
}
