package org.aldeon.gui2.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import org.aldeon.events.Callback;
import org.aldeon.gui2.Gui2Utils;
import org.aldeon.gui2.components.VerticalColorContainer;
import org.aldeon.model.Identity;

import java.util.Set;

public class IdentitiesController {

    @FXML protected FlowPane container;

    public static final String FXML_FILE = "views/Identities.fxml";

    public static Node create() {
        return Gui2Utils.loadFXMLfromDefaultPath(FXML_FILE);
    }

    public void initialize() {
        Gui2Utils.guiDb().getIdentities(new Callback<Set<Identity>>() {
            @Override
            public void call(Set<Identity> identities) {
                for(Identity identity: identities) {
                    VerticalColorContainer vcc = new VerticalColorContainer();
                    vcc.setContent(new Label(identity.getName()));
                    addToContainer(vcc);
                }
            }
        });
    }

    protected void addToContainer(Node node) {
        if(node != null) {
            container.getChildren().add(node);
        }
    }

    protected void delFromContainer(Node node) {
        container.getChildren().remove(node);
    }
}
