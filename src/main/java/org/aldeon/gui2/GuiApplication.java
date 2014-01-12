package org.aldeon.gui2;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.aldeon.core.CoreModule;
import org.aldeon.core.events.AppClosingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuiApplication extends Application {

    private static final Logger log = LoggerFactory.getLogger(GuiApplication.class);

    @Override
    public void start(Stage primaryStage) {
        log.info("Starting GUI...");
        Parent root = Gui2Utils.loadFXMLfromDefaultPath("views/Main.fxml");
        primaryStage.setTitle("Aldeon");
        primaryStage.getIcons().add(new Image("/gui2/images/appIcon.png"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        log.info("Closing GUI...");
        super.stop();
        CoreModule.getInstance().getEventLoop().notify(new AppClosingEvent());
    }
}
