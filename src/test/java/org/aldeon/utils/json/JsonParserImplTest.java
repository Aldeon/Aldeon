package org.aldeon.utils.json;

import org.aldeon.model.Identifier;
import org.aldeon.net.Ipv4PeerAddress;
import org.aldeon.net.Ipv6PeerAddress;
import org.aldeon.protocol.request.CompareTreesRequest;
import org.aldeon.protocol.request.GetMessageRequest;
import org.aldeon.protocol.request.GetRelevantPeersRequest;
import org.aldeon.protocol.request.IndicateInterestRequest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsonParserImplTest {

    public static final String emptyId = "\"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-\"";

    @Test
    public void shouldParseGetMessageRequest() throws ParseException {

        GetMessageRequest req = new GetMessageRequest();
        req.id = Identifier.empty();

        JsonParser parser = new JsonParserImpl();

        String json = parser.toJson(req);

        assertEquals(
                "{\"type\":\"get_message\",\"id\":" + emptyId + "}",
                json
        );

        GetMessageRequest res = parser.fromJson(json, GetMessageRequest.class);

        assertEquals(req.type, res.type);
        assertEquals(req.id, res.id);
    }

    @Test
    public void shouldParseCompareTreesRequest() throws ParseException {

        CompareTreesRequest req = new CompareTreesRequest();
        req.force = true;
        req.parent_id = Identifier.empty();
        req.parent_xor = Identifier.empty();

        JsonParser parser = new JsonParserImpl();

        String json = parser.toJson(req);

        assertEquals(
                "{\"type\":\"compare_trees\",\"parent_id\":" + emptyId + ",\"parent_xor\":" + emptyId + ",\"force\":true}",
                json
        );

        CompareTreesRequest res = parser.fromJson(json, CompareTreesRequest.class);

        assertEquals(req.type, res.type);
        assertEquals(req.force, res.force);
        assertEquals(req.parent_id, res.parent_id);
        assertEquals(req.parent_xor, res.parent_xor);
    }

    @Test
    public void shouldParseGetPeersInterestedRequest() throws ParseException {

        GetRelevantPeersRequest req = new GetRelevantPeersRequest();
        req.target = Identifier.empty();
        req.acceptedTypes.add(Ipv4PeerAddress.class);
        req.acceptedTypes.add(Ipv6PeerAddress.class);

        JsonParser parser = new JsonParserImpl();

        String json = parser.toJson(req);

        assertEquals(
                "{\"type\":\"get_relevant_peers\",\"target\":" + emptyId + ",\"types\":[\"ipv6\",\"ipv4\"]}",
                json

        );

        GetRelevantPeersRequest res = parser.fromJson(json, GetRelevantPeersRequest.class);

        assertEquals(req.type, res.type);
        assertEquals(req.target, res.target);
        assertTrue(res.acceptedTypes.contains(Ipv4PeerAddress.class));
        assertTrue(res.acceptedTypes.contains(Ipv6PeerAddress.class));
    }

    @Test
    public void shouldParseIndicateInterestRequest() throws ParseException {

        IndicateInterestRequest req = new IndicateInterestRequest();
        req.topic = Identifier.empty();
        req.address = Ipv4PeerAddress.parse("192.168.0.50", 8080);

        JsonParser parser = new JsonParserImpl();

        String json = parser.toJson(req);

        assertEquals(
                "{\"type\":\"indicate_interest\",\"topic\":" + emptyId + ",\"address\":{\"type\":\"ipv4\",\"host\":\"192.168.0.50\",\"port\":8080}}",
                json
        );

        IndicateInterestRequest res = parser.fromJson(json, IndicateInterestRequest.class);

        assertEquals(req.type, res.type);
        assertEquals(req.topic, res.topic);


        Ipv4PeerAddress a0 = (Ipv4PeerAddress) req.address;
        Ipv4PeerAddress a1 = (Ipv4PeerAddress) res.address;

        assertEquals(a0.getHost(), a1.getHost());
        assertEquals(a1.getPort(), a1.getPort());

        System.out.println(json);
    }
}
