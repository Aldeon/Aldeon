package org.aldeon.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.aldeon.core.CoreModule;
import org.aldeon.core.events.AppClosingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GUIController extends Application {

    private static final Logger log = LoggerFactory.getLogger(GUIController.class);

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
            stage.getIcons().add(new Image("/gui/appIcon.png"));
            launchMain();
            //gotoId();
            primaryStage.show();
        } catch (Exception ex) {
            log.error("Failed to start GUI", ex);
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        log.info("AppClosingEvent dispatched");
        CoreModule.getInstance().getEventLoop().notify(new AppClosingEvent());
    }

    private void launchMain() {
        try {
            BorderPane borderPane = GuiUtils.loadFxml("Main.fxml");
            Scene scene = new Scene(borderPane);
            stage.setScene(scene);
            stage.sizeToScene();
        } catch (Exception e) {
            log.error("Failed to launch main", e);
        }
    }
}
