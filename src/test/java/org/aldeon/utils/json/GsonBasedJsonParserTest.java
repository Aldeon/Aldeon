package org.aldeon.utils.json;

import org.aldeon.model.Identifier;
import org.aldeon.networking.mediums.ip.addresses.IpPeerAddress;
import org.aldeon.protocol.request.CompareTreesRequest;
import org.aldeon.protocol.request.GetMessageRequest;
import org.aldeon.protocol.request.GetRelevantPeersRequest;
import org.aldeon.protocol.request.IndicateInterestRequest;
import org.aldeon.utils.codec.Codec;
import org.aldeon.utils.conversion.ConversionException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GsonBasedJsonParserTest {

    public static final String emptyId = "test";

    @Test
    public void shouldParseGetMessageRequest() throws ParseException, ConversionException {

        GetMessageRequest req = new GetMessageRequest();
        req.id = Identifier.empty();

        Codec codec = mock(Codec.class);
        when(codec.encode(Identifier.empty().getByteBuffer())).thenReturn(emptyId);
        when(codec.decode(emptyId)).thenReturn(Identifier.empty().getByteBuffer());

        JsonParser parser = new GsonBasedJsonParser(codec);

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

        Codec codec = mock(Codec.class);
        when(codec.encode(Identifier.empty().getByteBuffer())).thenReturn(emptyId);
        when(codec.decode(emptyId)).thenReturn(Identifier.empty().getByteBuffer());

        JsonParser parser = new GsonBasedJsonParser(codec);

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

        Codec codec = mock(Codec.class);
        when(codec.encode(Identifier.empty().getByteBuffer())).thenReturn(emptyId);
        when(codec.decode(emptyId)).thenReturn(Identifier.empty().getByteBuffer());

        JsonParser parser = new GsonBasedJsonParser(codec);

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
        req.address = IpPeerAddress.create("192.168.0.50", 8080);

        Codec codec = mock(Codec.class);
        when(codec.encode(Identifier.empty().getByteBuffer())).thenReturn(emptyId);
        when(codec.decode(emptyId)).thenReturn(Identifier.empty().getByteBuffer());

        JsonParser parser = new GsonBasedJsonParser(codec);

        String json = parser.toJson(req);

        assertEquals(
                "{\"type\":\"indicate_interest\",\"topic\":\"" + emptyId + "\",\"address\":{\"type\":\"ipv4\",\"host\":\"192.168.0.50\",\"port\":8080}}",
                json
        );

        IndicateInterestRequest res = parser.fromJson(json, IndicateInterestRequest.class);

        assertEquals(req.type, res.type);
        assertEquals(req.topic, res.topic);


        IpPeerAddress a0 = (IpPeerAddress) req.address;
        IpPeerAddress a1 = (IpPeerAddress) res.address;

        assertEquals(a0.getHost(), a1.getHost());
        assertEquals(a1.getPort(), a1.getPort());

    }
}