package org.aldeon.core;

import org.aldeon.networking.mediums.ip.addresses.IpPeerAddress;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;


public class PropertiesManager {
    private static final String CONFIG_NAME="aldeon.conf";
    private static final Properties props = new Properties();
    public static final String ADDRESS_TRANSLATION="addressTranslation";
    public static final String IP_AUTO="ipAuto";
    public static final String IP_ADDRESS="ipAddress";
    public static final String PORT_AUTO="portAuto";
    public static final String PORT_NUMBER="portNumber";
    public static final String PRIVACY_LEVEL="privacyLevel";
    public static final String DIFF_TIMEOUT="syncInterval";
    public static final String INIT_PEERS="initPeers";
    public static final String DEFAULT_ADDRESS_TRANSLATION = "true";
    public static final String DEFAULT_IP_ADDRESS = "127.0.0.1";
    public static final String DEFAULT_IP_AUTO = "true";
    public static final String DEFAULT_PORT_RANDOM = "true";
    public static final String DEFAULT_PORT_NUMBER = "41530";
    public static final String DEFAULT_DIFF_TIMEOUT = "5";
    public static final String DEFAULT_PRIVACY_LEVEL = "1";
    public static final String DEFAULT_INIT_PEERS = "";

    public PropertiesManager(){
        File config = new File(CONFIG_NAME);
        if( config.exists())
            try {
                props.load(new FileInputStream(config));
            } catch (IOException e) {
                e.printStackTrace();
            }
        else
            try {
                props.store(new FileOutputStream(CONFIG_NAME),null);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void removeProperty(String property){
        props.remove(property);
        saveChange();
    }

    public void setProperty(String property, String value){
        props.put(property, value);
        saveChange();
    }

    public String getProperty(String property){
        return props.getProperty(property);
    }
    public String getProperty(String property, String defaultValue){
        return props.getProperty(property,defaultValue);
    }

    public boolean getAddressTranslation(){
        return Boolean.parseBoolean(getProperty(ADDRESS_TRANSLATION,DEFAULT_ADDRESS_TRANSLATION));
    }

    public boolean getIpAutodetection(){
        return Boolean.parseBoolean(getProperty(IP_AUTO,DEFAULT_IP_AUTO));
    }

    public boolean getPortRandomization(){
        return Boolean.parseBoolean(getProperty(PORT_AUTO,DEFAULT_PORT_RANDOM));
    }

    public int getPortNumber(){
        return Integer.parseInt(getProperty(PORT_NUMBER,DEFAULT_PORT_NUMBER));
    }

    public int getDiffTimeout(){
        return Integer.parseInt(getProperty(DIFF_TIMEOUT,DEFAULT_DIFF_TIMEOUT));
    }

    public Set<String> getPeersForGUI(){
        Set<String>peers = new HashSet<>();
        String rawPeers = getProperty(INIT_PEERS,DEFAULT_INIT_PEERS);
        String processPeers[] = rawPeers.split(",");
        for(String peer : processPeers){
                peers.add(peer);
        }
        return peers;
    }

    public Set<IpPeerAddress> getInitPeers(){
        Set<IpPeerAddress> peers = new HashSet<>();
        String rawPeers = getProperty(INIT_PEERS,DEFAULT_INIT_PEERS);
        String processPeers[] = rawPeers.split(",");
        for(String peer : processPeers){
            try {
                peers.add(IpPeerAddress.create(InetAddress.getByName(peer),getPortNumber()));
            } catch (UnknownHostException e) {
                System.out.println("One of peers is invalid: "+peer);
            }
        }
        return peers;
    }

    public int getPrivacyLevel(){
        return Integer.parseInt(getProperty(PRIVACY_LEVEL, DEFAULT_PRIVACY_LEVEL));
    }

    public IpPeerAddress getMyIp(){
        try {
            return IpPeerAddress.create(InetAddress.getByName(getProperty(IP_ADDRESS, DEFAULT_IP_ADDRESS)), getPortNumber());
        } catch (UnknownHostException e) {
            System.out.println("DEFAULT IP INCORRECT");
        }
        return null;
    }

    public void saveChange(){
        try {
            props.store(new FileOutputStream(CONFIG_NAME),null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
