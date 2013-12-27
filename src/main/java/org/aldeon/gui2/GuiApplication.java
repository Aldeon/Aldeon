package org.aldeon.gui2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GuiApplication extends Application {

    private static final Logger log = LoggerFactory.getLogger(GuiApplication.class);

    /*
        TODO:
            - list topics in topicsview, autorefresh
            - list identities / friends

     */

    @Override
    public void start(Stage primaryStage) {
        Parent root = Gui2Utils.loadFXMLfromDefaultPath("views/Main.fxml");

        if(root == null) {
            System.err.println("Failed to load Main.fxml");
            return;
        }

        primaryStage.setTitle("Aldeon");
        primaryStage.getIcons().add(new Image("/gui2/images/appIcon.png"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
