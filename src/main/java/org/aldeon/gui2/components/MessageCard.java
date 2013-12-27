package org.aldeon.gui2.components;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.aldeon.gui2.Gui2Utils;

public class MessageCard extends BorderPane{

    @FXML protected Label content;

    public MessageCard() {
        Gui2Utils.loadFXMLandInjectController("/gui2/fxml/components/MessageCard.fxml", this);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                content.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin sapien erat, condimentum ut dictum sit amet, aliquet a enim. Nulla justo nisi, hendrerit pretium lacus nec");
                //content.setText("Lorem");
            }
        });
    }
}
