package org.aldeon.app;

import org.aldeon.core.Core;
import org.aldeon.core.CoreModule;
import org.aldeon.db.DbImpl;
import org.aldeon.gui.GuiModule;
import org.aldeon.protocol.ProtocolModule;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        ProtocolModule.initialize();

        // Instantiate the core
        Core core = CoreModule.getInstance();

        core.initSenders();
        core.initReceivers();

        DbImpl db = (DbImpl) core.getStorage();

        db.insertTestData();

        System.in.read();
        db.closeDbConnection();

        new File("aldeon.db.lck").delete();
        new File("aldeon.db.properties").delete();
        new File("aldeon.db.log").delete();
        new File("aldeon.db.script").delete();

        // Launch GUI
        //GuiModule.launch();

        /*
            Now everything should work.
            To see the results, go to:

            http://localhost:8080?query={"type":"get_message","id":"CaKjxIm3DbrEmeCsso5hFX8AyagHBrv1UBiSrpN8vjE-"}
         */

        // Core awaits for the AppClosingEvent to occur. Then it will close.
    }

}
