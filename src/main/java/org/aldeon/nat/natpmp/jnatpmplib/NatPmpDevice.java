/*
 * This file is part of jNAT-PMPlib.
 *
 * http://sourceforge.net/projects/jnat-pmplib/
 *
 * jNAT-PMPlib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jNAT-PMPlib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jNAT-PMPlib.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.aldeon.nat.natpmp.jnatpmplib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class manages a NAT-PMP device. This class is thread-safe.
 *
 * This manages communication with a NAT-PMP device on the network. There are
 * two types of messages that can be sent to the device.
 * {@link ExternalAddressRequestMessage} can be sent to get the external IP of
 * the gateway. {@link MapRequestMessage} can be sent to map a port for a
 * certain amount of time. These two messages can be put into the message queue
 * through the {@link #enqueueMessage(Message)} method.
 *
 * As this class manages a message queue to the NAT-PMP device, it is important
 * to shut it down corectly. Any mapped ports that are no longer desired should
 * be unmapped before shutdown occurs. Refer to {@link #NatPmpDevice(boolean)}
 * for details about the shutdown mechanism.
 * @author flszen
 */
public class NatPmpDevice {
    // Shutdown control instance fields.
    private boolean isShutdown = false;
    private Thread shutdownHookThread = null;
    private final Object shutdownLock = new Object();

    private MessageQueue messageQueue;

    /**
     * Constructs a new NatPmpDevice.
     *
     * @param shutdownHookEnabled Shutting down existing port mappings is a
     * desired behavior; therefore, this value is required! Refer to
     * {@link #setShutdownHookEnabled(boolean)} for details about what value
     * should be provided here and how it alters the object's behavior.
     *
     * @throws NatPmpException A NatPmpException may be thrown if the local
     * network is not using addresses defined in RFC1918. A NatPmpException may
     * also be thrown if the the network gateway cannot be determined, which may
     * rarely be due to the network not using IPv4. NAT-PMP should only be used 
     * on RFC1918 networks using IPv4.
     *
     * @see #setShutdownHookEnabled(boolean)
     * @see #shutdown()
     */
    public NatPmpDevice(boolean shutdownHookEnabled) throws NatPmpException {
        // Use the accessor to set the shutdown hook state.
        setShutdownHookEnabled(shutdownHookEnabled);

        // Get the gateway IP.
        Inet4Address gateway = getGatewayIP();

        // Reject if the gateway is null.
        // This indicates either no gateway or the network is not IPv4.
        // It could also be that the netstat response is not supported.
        if (gateway == null) {
            throw new NatPmpException("The network gateway cannot be located.");
        }

        // Reject if it is not RFC1918.
        if (!gateway.isSiteLocalAddress()) {
            throw new NatPmpException("The network gateway address is not RFC1918 compliant.");
        }

        // Set up messaging queue.
        messageQueue = MessageQueue.createMessageQueue(gateway);
    }

    /**
     * Enqueues a message for sending.
     * @param message The {@link Message} to send.
     * @see #clearQueue()
     */
    public void enqueueMessage(Message message) {
        messageQueue.enqueueMessage(message);
    }
    
    /**
     * Clears the queue of messages to send. If a message is currently sending,
     * it is not interrupted.
     * @see #enqueueMessage(Message)
     */
    public void clearQueue() {
        messageQueue.clearQueue();
    }

    /**
     * Synchronously waits until the queue is empty before returning.
     */
    public void waitUntilQueueEmpty() {
        messageQueue.waitUntilQueueEmpty();
    }

    /**
     * Flag indicates if the shutdown hook is enabled or disabled.
     *
     * @return A value of true indicates the hook is enabled. A value of false
     * indicates the hook is disabled.
     * 
     * @see #setShutdownHookEnabled(boolean)
     * @see #shutdown()
     */
    public final boolean isShutdownHookEnabled() {
        synchronized (shutdownLock) {
            return shutdownHookThread != null;
        }
    }

    /**
     * Sets the enabled state of the shutdown hook functionality of this object.
     * If you do not intend to manually shutdown this object, true is the
     * preferred value. This will most effectively notify the NAT-PMP gateway
     * to remove existing port mappings.
     *
     * @param enabled If true is specified, the NatPmpDevice will hook into the
     * shutdown of the Java VM. When the VM is shutting down, this object will
     * attempt to remove the port mappings. The procedure may silently fail, in
     * which case, the timeout on the port map will eventually occur. If false
     * is specified, it is up to the consumer of the object to call
     * {@link #shutdown()} when the device needs to be disregarded. If shutdown
     * is called when true is specified here, the hook will be automatically
     * removed.
     *
     * @see #isShutdownHookEnabled()
     * @see #shutdown()
     */
    public final void setShutdownHookEnabled(boolean enabled) {
        synchronized (shutdownLock) {
            if (isShutdownHookEnabled()) {
                // Currently enabled.
                if (!enabled) {
                    // Set to disabled.
                    Runtime.getRuntime().removeShutdownHook(shutdownHookThread);
                    shutdownHookThread = null;
                }
            } else {
                // Not currently enabled.
                if (enabled) {
                    // Set to enabled.
                    // The shutdown hook simply runs the shutdown method.
                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            shutdown();
                        }
                    }, "NatPmpDevice:ShutdownHook");

                    Runtime.getRuntime().addShutdownHook(t);
                    shutdownHookThread = t;
                }
            }
        }
    }

    /**
     * Shuts down this NatPmpDevice. If the shutdown hook is disabled, this
     * method should be called manually at the time port mappings through the
     * NAT-PMP gateway are no longer needed. If the shutdown hook is enabled,
     * this method is called automatically during Java VM shutdown.
     *
     * When this method is called, if the shutdown hook is enabled, it is
     * automatically disabled.
     *
     * It should be noted that when this method is called manually, it blocks
     * until it completes. If it is desired to shutdown asynchronously, the
     * {@link #shutdownAsync(boolean)} method should be called.
     *
     * @see #isShutdown()
     * @see #isShutdownHookEnabled()
     * @see #setShutdownHookEnabled(boolean)
     * @see #shutdownAsync(boolean)
     */
    public void shutdown() {
        synchronized (shutdownLock) {
            // First, remove the shutdown hook in case it exists.
            setShutdownHookEnabled(false);

            // Do the shutdown stuff.
            messageQueue.shutdown();

            // Set the isShutdown flag.
            isShutdown = true;
        }
    }

    /**
     * This method creates and runs a thread for the {@link #shutdown()} method.
     * If the shutdown hook is enabled, it is disabled automatically.
     * @param daemon Run the thread as a daemon. The Java VM will not wait
     * for the thread to complete when it is shutting down.
     * @return The {@link Thread} that was created and started. This return
     * value is useful is you would like to monitor the thread.
     * @see #shutdown()
     * @see #isShutdown()
     */
    public Thread shutdownAsync(boolean daemon) {
        synchronized (shutdownLock) {
            // First, remove the shutdown hook.
            setShutdownHookEnabled(false);

            // Create the thread.
            Thread t = new Thread(new Runnable() {
                public void run() {
                    shutdown();
                }
            }, "NatPmpDevice:ShutdownAsync");

            // Configure and run the thread.
            t.setDaemon(daemon);
            t.start();

            // Return the running thread. Due to synchronization, the thread
            // will not actually start executing code until this returns.
            return t;
        }
    }

    /**
     * Flag indicates if this NatPmpDevice is shutdown. This method will block
     * if a shutdown is in progress. If you desire to wait for an asynchronous
     * shutdown to complete, please monitor the returned {@link Thread} instead.
     * @return True if this NatPmpDeviceTest is shutdown, false if it is not.
     * @see #shutdown() 
     */
    public boolean isShutdown() {
        synchronized (shutdownLock) {
            return isShutdown;
        }
    }

    /**
     * Returns the IP of the getaway from netstat.
     *
     * Note that this methods was adapted from
     * http://forums.sun.com/thread.jspa?threadID=5289135, and as such appears
     * to be in the public domain.
     *
     * @return A string representing the IP, or null if none is found.
     */
    static Inet4Address getGatewayIP(){
        // Regexp helpers.
        String _255 = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
        String exIP = "(?:" + _255 + "\\.){3}" + _255;

        // Regexp to find the gateway.
        Pattern gatewayPattern;
        String osName = System.getProperty("os.name");
        if (osName.equals("Mac OS X")) {
            // NOTE: It may be that "default" is different in other locales. Be
            // aware of this case.
            gatewayPattern = Pattern.compile("^\\s*(?:default\\s*)("+exIP+").*");
        } else if (osName.startsWith("Windows")) {
            gatewayPattern = Pattern.compile("^\\s*(?:0\\.0\\.0\\.0\\s*){1,2}("+exIP+").*");
        } else {
            return null;
        }

        // Try to determine the gateway.
        try {
            // Run netstat. This gets the table of routes.
            Process proc = Runtime.getRuntime().exec("netstat -rn");

            InputStream inputstream = proc.getInputStream();
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

            // Parse the result.
            String line;
            while ((line = bufferedreader.readLine()) != null) {
                Matcher m = gatewayPattern.matcher(line);

                // This is the gateway line.
                if(m.matches()){
                    // Return the first group as an address. It is the gateway.
                    return (Inet4Address)Inet4Address.getByName(m.group(1));
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(NatPmpDevice.class.getName()).log(Level.SEVERE, "NatPmpDevice: Unable to determine gateway.", ex);
        }

        return null;
    }
}
