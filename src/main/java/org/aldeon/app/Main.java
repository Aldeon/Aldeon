package org.aldeon.app;

import org.aldeon.core.CoreModule;
import org.aldeon.core.UserManager;
import org.aldeon.crypt.Key;
import org.aldeon.crypt.exception.KeyParseException;
import org.aldeon.crypt.rsa.RsaKeyGen;
import org.aldeon.gui.GuiModule;
import org.aldeon.model.Identifier;
import org.aldeon.model.Identity;
import org.aldeon.networking.common.PeerAddress;
import org.aldeon.networking.mediums.ip.addresses.IpPeerAddress;
import org.aldeon.protocol.ProtocolModule;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.helpers.Identifiers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.interfaces.RSAKey;

public class Main {

    public static void main(String[] args) throws IOException, ConversionException {

        ProtocolModule.initialize();
        //Identifier topic = Identifiers.fromBase64("W3/vMKf3rfJacSvLBoL7DJX.EUBV1ivkDGpPazT29pk-");
        //PeerAddress peer = IpPeerAddress.create("192.168.1.42", 41530);
        //CoreModule.getInstance().getTopicManager().addTopic(topic);
        //CoreModule.getInstance().getDht().interestTracker().addAddress(peer, topic);

        System.out.println("TESTING: kmCggz0RLgoWQeuV1a6.BBIXsqwKFnUpg1p8UiXoMCJW7uDVp5OS8yLLLjqkZiaGMbvTCC9j4acQxt5pUhNpCc3MKh38gx1Olv.Ur.EI6jEDD76MAVoFxwJFzDcaT9wfTpdf6uhzXXaBRjjw/lG0KgzpLX0fVutocIODfh0ks7c-");
        /*for(Key id :CoreModule.getInstance().getUserManager().getAllIdentities().keySet()){
            System.out.println("MAM KLUCZ: "+id.toString());
            if(id.equals(decodedPrvKey))
                System.out.println("LOL JEST MATCH :/"+id);
        }*/

        GuiModule.launch();
    }

}
