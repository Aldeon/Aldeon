package org.aldeon.gui2.controllers;

import javafx.scene.Node;
import org.aldeon.gui2.Gui2Utils;

public class WelcomeController {

    public static final String FXML_FILE = "views/Welcome.fxml";

    public static Node create() {
        return Gui2Utils.loadFXMLfromDefaultPath(FXML_FILE);
    }

}
