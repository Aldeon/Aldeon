package org.aldeon.gui2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;

public class Gui2Utils {
    private static final String FXML_PATH = "/gui2/fxml";

    public static String toWebHex(Color color) {
        return String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) );
    };

    public static Parent loadFXML(String path) {
        try {
            return FXMLLoader.load(Gui2Utils.class.getResource(path));
        } catch (IOException e) {
            return null;
        }
    }

    public static Parent loadFXMLfromDefaultPath(String file) {
        return loadFXML(FXML_PATH + "/" + file);
    }

    public static Object loadFXMLandInjectController(String path, Object controller) {
        URL url = Gui2Utils.class.getResource(path);
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        fxmlLoader.setRoot(controller);
        fxmlLoader.setController(controller);
        try {
            return fxmlLoader.load();
        } catch (IOException exception) {
            return null;
        }
    }
}
