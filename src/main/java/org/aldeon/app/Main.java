package org.aldeon.app;

import org.aldeon.core.CoreModule;
import org.aldeon.gui.GuiModule;
import org.aldeon.model.Identifier;
import org.aldeon.crypt.rsa.RsaKeyGen;
import org.aldeon.model.Identity;
import org.aldeon.networking.mediums.ip.addresses.IpPeerAddress;
import org.aldeon.protocol.ProtocolModule;
import org.aldeon.utils.codec.Codec;
import org.aldeon.utils.codec.base64.Base64Module;
import org.aldeon.utils.conversion.ConversionException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, ConversionException {

        ProtocolModule.initialize();

        Codec base64 = new Base64Module().get();

        Identifier topic = Identifier.fromByteBuffer(base64.decode("kWCg9mtJBbc0SqJ/XMusbFCs8QP02NScFhRuCerFXwk-"), false);

        //CoreModule.getInstance().getDht(IpPeerAddress.IPV4).registerAddress(IpPeerAddress.create("192.168.1.105", 41530), topic);

        //CoreModule.getInstance().getTopicManager().addTopic(topic);

        GuiModule.launch();
    }

}
