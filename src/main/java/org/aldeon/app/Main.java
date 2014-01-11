package org.aldeon.app;

import org.aldeon.gui2.Gui2Module;
import org.aldeon.protocol.ProtocolModule;
import org.aldeon.utils.conversion.ConversionException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, ConversionException {
        ProtocolModule.initialize();
        Gui2Module.launch();
    }
}
