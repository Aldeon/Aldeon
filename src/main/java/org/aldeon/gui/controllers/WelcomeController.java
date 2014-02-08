package org.aldeon.gui.controllers;

import javafx.scene.Node;
import org.aldeon.gui.Gui2Utils;

public class WelcomeController {

    public static final String FXML_FILE = "views/Welcome.fxml";

    public static Node create() {
        return Gui2Utils.loadFXMLfromDefaultPath(FXML_FILE);
    }

}
