package org.aldeon.app;

import javafx.application.Application;
import javafx.stage.Stage;
import org.aldeon.common.EndpointWithPortPolicy;
import org.aldeon.common.net.ConcretePort;
import org.aldeon.common.net.StaticPortPolicy;
import org.aldeon.gui.GUIManager;
import org.aldeon.jetty.JettyModule;

import java.io.IOException;

//javafx odpala tego starta przez czary i magię więc dopóki nie wykminię skąd on bierze
//tego stage'a to tak będzie na tym branchu
public class Main extends Application{

    public static void main(String[] args) {
        launch(args);
        /*EndpointWithPortPolicy endpoint = JettyModule.getEndpoint();
        endpoint.setObserver(new FooObserver());
        endpoint.setPortPolicy(new StaticPortPolicy(new ConcretePort(8080), new ConcretePort(8080)));
        endpoint.start();

        try {
            System.in.read();
        } catch (IOException e) {}

        endpoint.stop();*/
    }


    @Override
    public void start(Stage stage) throws Exception {
        GUIManager gui = new GUIManager();
        gui.start(stage);
    }
}
