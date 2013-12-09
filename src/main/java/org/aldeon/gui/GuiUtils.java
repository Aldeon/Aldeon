package org.aldeon.gui;

import javafx.fxml.FXMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

public class GuiUtils {

    private static final Logger log = LoggerFactory.getLogger(GuiUtils.class);

    private static final String FXML_PATH = "/gui/fxml/";

    public static <T> T loadFxml(String fileName) {
        URL fxmlUrl = GuiUtils.class.getResource(FXML_PATH + fileName);
        if(fxmlUrl == null) {
            log.error("The selected FXML file (" + FXML_PATH + fileName +") does not exist");
            return null;
        } else {
            try {
                return FXMLLoader.load(fxmlUrl);
            } catch (IOException e) {
                log.error("Failed to load FXML (" + fileName + ")", e);
                return null;
            }
        }
    }

    public static <T> T getController(String fxml) {
        URL fxmlUrl = GuiUtils.class.getResource(FXML_PATH + fxml);
        if(fxmlUrl == null) {
            log.error("The selected FXML file (" + FXML_PATH + fxml +") does not exist");
            return null;
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.load(fxmlUrl);
                return loader.getController();
            } catch (IOException e) {
                log.error("Failed to load FXML (" + fxml + ")", e);
                return null;
            }
        }
    }
}
