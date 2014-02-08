package org.aldeon.gui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.paint.Color;
import org.aldeon.core.CoreModule;
import org.aldeon.events.EventLoop;
import org.aldeon.events.executors.ExecutorLogger;
import org.aldeon.events.executors.ThrowableInterceptor;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Executor;

public class Gui2Utils {
    private static final String FXML_PATH = "/gui2/fxml";
    private static EventLoop loop;
    private static Executor fxExecutor = null;

    static {
        fxExecutor = new ExecutorLogger("javafx", new ThrowableInterceptor(new Executor() {
            @Override
            public void execute(Runnable command) {
                Platform.runLater(command);
            }
        }));
        loop = CoreModule.getInstance().getEventLoop();
    }

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

    public static EventLoop loop() {
        return loop;
    }

    public static Executor fxExecutor() {
        return fxExecutor;
    }

    public static void copyToClipboard(String data) {
        ClipboardContent content = new ClipboardContent();
        content.putString(data);
        Clipboard.getSystemClipboard().setContent(content);
    }
}
