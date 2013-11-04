package org.aldeon.protocol.response;

import org.aldeon.net.PeerAddress;
import org.aldeon.protocol.Response;

import java.util.ArrayList;

/**
 *
 */
public class RelevantPeersResponse implements Response {
    public static final String TYPE = "relevant_peers";
    public String type = TYPE;

    public int id = 4;
    //public ArrayList<Integer> lista = new ArrayList<Integer>();
    public ArrayList<PeerAddress> haveInfo;
    public ArrayList<PeerAddress> askedForIt;
    public ArrayList<PeerAddress> closestInDHT;

    public RelevantPeersResponse() {

        haveInfo = new ArrayList<PeerAddress>();
        askedForIt = new ArrayList<PeerAddress>();
        closestInDHT = new ArrayList<PeerAddress>();

//        lista.add(1);
//        lista.add(2);
//        lista.add(3);


        //peery od ktorych ja uzyskalem odpowiedz na dany temat
        // trzymana gdzies w DHT
        //peery ktore pytaly o ten temat
        //peery ktore sa najblizej tego tematu wg DHT
    }
}
