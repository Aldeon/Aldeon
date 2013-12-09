package org.aldeon.app;

import org.aldeon.gui.GuiModule;
import org.aldeon.protocol.ProtocolModule;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        ProtocolModule.initialize();

        GuiModule.launch();
    }

}
