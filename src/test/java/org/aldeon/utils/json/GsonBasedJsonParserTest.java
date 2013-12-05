package org.aldeon.utils.json;

import org.aldeon.model.Identifier;
import org.aldeon.net.Ipv4PeerAddress;
import org.aldeon.protocol.request.CompareTreesRequest;
import org.aldeon.protocol.request.GetMessageRequest;
import org.aldeon.protocol.request.GetRelevantPeersRequest;
import org.aldeon.protocol.request.IndicateInterestRequest;
import org.aldeon.utils.base64.Base64;
import org.aldeon.utils.conversion.ConversionException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GsonBasedJsonParserTest {

    public static final String emptyId = "test";

    @Test
    public void shouldParseGetMessageRequest() throws ParseException, ConversionException {

        GetMessageRequest req = new GetMessageRequest();
        req.id = Identifier.empty();

        Base64 base64 = mock(Base64.class);
        when(base64.encode(Identifier.empty().getByteBuffer())).thenReturn(emptyId);
        when(base64.decode(emptyId)).thenReturn(Identifier.empty().getByteBuffer());

        JsonParser parser = new GsonBasedJsonParser(base64);

        String json = parser.toJson(req);

        assertEquals(
                "{\"type\":\"get_message\",\"id\":\"" + emptyId + "\"}",
                json
        );

        GetMessageRequest res = parser.fromJson(json, GetMessageRequest.class);

        assertEquals(req.type, res.type);
        assertEquals(req.id, res.id);
    }

    @Test
    public void shouldParseCompareTreesRequest() throws ParseException, ConversionException {

        CompareTreesRequest req = new CompareTreesRequest();
        req.force = true;
        req.parent_id = Identifier.empty();
        req.parent_xor = Identifier.empty();

        Base64 base64 = mock(Base64.class);
        when(base64.encode(Identifier.empty().getByteBuffer())).thenReturn(emptyId);
        when(base64.decode(emptyId)).thenReturn(Identifier.empty().getByteBuffer());

        JsonParser parser = new GsonBasedJsonParser(base64);

        String json = parser.toJson(req);

        assertEquals(
                "{\"type\":\"compare_trees\",\"parent_id\":\"" + emptyId + "\",\"parent_xor\":\"" + emptyId + "\",\"force\":true}",
                json
        );

        CompareTreesRequest res = parser.fromJson(json, CompareTreesRequest.class);

        assertEquals(req.type, res.type);
        assertEquals(req.force, res.force);
        assertEquals(req.parent_id, res.parent_id);
        assertEquals(req.parent_xor, res.parent_xor);
    }

    @Test
    public void shouldParseGetPeersInterestedRequest() throws ParseException, ConversionException {

        GetRelevantPeersRequest req = new GetRelevantPeersRequest();
        req.target = Identifier.empty();

        Base64 base64 = mock(Base64.class);
        when(base64.encode(Identifier.empty().getByteBuffer())).thenReturn(emptyId);
        when(base64.decode(emptyId)).thenReturn(Identifier.empty().getByteBuffer());

        JsonParser parser = new GsonBasedJsonParser(base64);

        String json = parser.toJson(req);

        assertEquals(
                "{\"type\":\"get_relevant_peers\",\"target\":\"" + emptyId + "\"}",
                json

        );

        GetRelevantPeersRequest res = parser.fromJson(json, GetRelevantPeersRequest.class);

        assertEquals(req.type, res.type);
        assertEquals(req.target, res.target);
    }

    @Test
    public void shouldParseIndicateInterestRequest() throws ParseException, ConversionException {

        IndicateInterestRequest req = new IndicateInterestRequest();
        req.topic = Identifier.empty();
        req.address = Ipv4PeerAddress.parse("192.168.0.50", 8080);

        Base64 base64 = mock(Base64.class);
        when(base64.encode(Identifier.empty().getByteBuffer())).thenReturn(emptyId);
        when(base64.decode(emptyId)).thenReturn(Identifier.empty().getByteBuffer());

        JsonParser parser = new GsonBasedJsonParser(base64);

        String json = parser.toJson(req);

        assertEquals(
                "{\"type\":\"indicate_interest\",\"topic\":\"" + emptyId + "\",\"address\":{\"type\":\"ipv4\",\"host\":\"192.168.0.50\",\"port\":8080}}",
                json
        );

        IndicateInterestRequest res = parser.fromJson(json, IndicateInterestRequest.class);

        assertEquals(req.type, res.type);
        assertEquals(req.topic, res.topic);


        Ipv4PeerAddress a0 = (Ipv4PeerAddress) req.address;
        Ipv4PeerAddress a1 = (Ipv4PeerAddress) res.address;

        assertEquals(a0.getHost(), a1.getHost());
        assertEquals(a1.getPort(), a1.getPort());

    }
}
