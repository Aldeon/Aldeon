package org.aldeon.app;

import org.aldeon.core.CoreModule;
import org.aldeon.events.Callback;
import org.aldeon.events.SingleRunCallback;
import org.aldeon.gui2.Gui2Module;
import org.aldeon.model.Message;
import org.aldeon.protocol.ProtocolModule;
import org.aldeon.utils.conversion.ConversionException;

import java.io.IOException;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException, ConversionException {
        ProtocolModule.initialize();
        Gui2Module.launch();
    }
}
