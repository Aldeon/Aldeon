package org.aldeon.app;

import org.aldeon.core.CoreModule;
import org.aldeon.events.Callback;
import org.aldeon.events.SingleRunCallback;
import org.aldeon.gui2.Gui2Module;
import org.aldeon.model.Message;
import org.aldeon.protocol.ProtocolModule;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.various.VirtualMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException, ConversionException {
        if(VirtualMachine.getNumberOfApplicationInstances() > 1)
        {
            log.error("Attempting to run more than one instance. Exiting.");
            System.exit(-1);
        }

        ProtocolModule.initialize();
        Gui2Module.launch();
    }
}
