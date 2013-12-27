package org.aldeon.app;

import org.aldeon.gui2.Gui2Module;
import org.aldeon.protocol.ProtocolModule;

public class Debug {

    public static void main(String[] args) {
        ProtocolModule.initialize();
        Gui2Module.launch();
    }
}
