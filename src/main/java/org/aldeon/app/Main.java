package org.aldeon.app;

import org.aldeon.core.Core;
import org.aldeon.core.CoreModule;
import org.aldeon.gui.GuiModule;
import org.aldeon.protocol.Protocol;
import org.aldeon.protocol.ProtocolModule;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        // Instantiate the protocol
        Protocol protocol = ProtocolModule.createProtocol();

        // Instantiate the core
        Core core = CoreModule.getInstance();

        core.initSenders();
        core.initReceivers(protocol);

        // Core awaits for the AppClosingEvent to occur. Then it will close.
        //Launch GUI
        GuiModule.launch();

        /*
            Now everything should work.
            To see the results, go to:

            http://localhost:8080?query={"type":"get_message","id":"CaKjxIm3DbrEmeCsso5hFX8AyagHBrv1UBiSrpN8vjE-"}
         */
    }

}
