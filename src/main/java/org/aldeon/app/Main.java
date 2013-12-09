package org.aldeon.app;

import org.aldeon.gui.GuiModule;
import org.aldeon.model.Identifier;
import org.aldeon.protocol.ProtocolModule;
import org.aldeon.utils.codec.Codec;
import org.aldeon.utils.codec.base64.Base64Module;
import org.aldeon.utils.conversion.ConversionException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, ConversionException {

        ProtocolModule.initialize();

        Codec base64 = new Base64Module().get();

        Identifier topic = Identifier.fromByteBuffer(base64.decode("MyYO4o6ScFt3/f1JTNdb8Ta5oCcGs0MHHenEpWVxF5Y-"), false);

        // CoreModule.getInstance().getTopicManager().addTopic(topic);

        GuiModule.launch();
    }

}
