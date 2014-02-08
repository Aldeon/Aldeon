package org.aldeon.gui.controllers;

import javafx.scene.Node;
import org.aldeon.gui.GuiUtils;

public class SettingsController {

    public static final String FXML_FILE = "views/Settings.fxml";

    public static Node create() {
        return GuiUtils.loadFXMLfromDefaultPath(FXML_FILE);
    }
}
