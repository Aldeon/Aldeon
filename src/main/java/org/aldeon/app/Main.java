package org.aldeon.app;

import org.aldeon.core.CoreModule;
import org.aldeon.gui.GuiModule;
import org.aldeon.model.Identifier;
import org.aldeon.crypt.rsa.RsaKeyGen;
import org.aldeon.model.Identity;
import org.aldeon.protocol.ProtocolModule;
import org.aldeon.utils.codec.Codec;
import org.aldeon.utils.codec.base64.Base64Module;
import org.aldeon.utils.conversion.ConversionException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, ConversionException {

        ProtocolModule.initialize();

        Codec base64 = new Base64Module().get();

        //Identifier topic = Identifier.fromByteBuffer(base64.decode("i5DLsCvFZupO6r4sVqkQ6WPaYYyvKhZlPgrr7V68Ke4-"), false);

        // CoreModule.getInstance().getDht(IpPeerAddress.IPV4).registerAddress(IpPeerAddress.create("192.168.1.90", 41530), topic);

        // CoreModule.getInstance().getTopicManager().addTopic(topic);
        CoreModule.getInstance().getUserManager().addIdentity(Identity.create("BLARGH1", new RsaKeyGen()));
        CoreModule.getInstance().getUserManager().addIdentity(Identity.create("BLARGH2", new RsaKeyGen()));
        CoreModule.getInstance().getUserManager().addIdentity(Identity.create("BLARGH3", new RsaKeyGen()));

        GuiModule.launch();
    }

}
