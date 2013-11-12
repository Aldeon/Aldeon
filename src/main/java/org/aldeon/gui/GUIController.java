package org.aldeon.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.aldeon.app.Main;
import org.aldeon.core.events.AppClosingEvent;
import org.aldeon.gui.controllers.*;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GUIController extends Application {

    private Stage stage;
    private final double MINIMUM_WINDOW_WIDTH = 796.0;
    private final double MINIMUM_WINDOW_HEIGHT = 600.0;

    @Override
    public void start(Stage primaryStage) {
        try {
            stage = primaryStage;
            stage.setTitle("Aldeon");
            stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
            stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
            launchMain();
            //gotoId();
            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(GUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Main.getCore().getEventLoop().notify(new AppClosingEvent());
    }

    private void launchMain() {
        try {
            MainController profile = (MainController) changeFxml("Main.fxml");
            profile.setRoot(this);
        } catch (Exception e) {}
    }

    public Initializable changeFxml(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = GUIController.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(GUIController.class.getResource(fxml));
        BorderPane page;
        try {
            page = (BorderPane) loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(page);
        stage.setScene(scene);
        stage.sizeToScene();
        return loader.getController();
    }
}