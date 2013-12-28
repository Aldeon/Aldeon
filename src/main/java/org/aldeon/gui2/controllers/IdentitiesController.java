package org.aldeon.gui2.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import org.aldeon.crypt.Key;
import org.aldeon.events.Callback;
import org.aldeon.gui2.Gui2Utils;
import org.aldeon.gui2.components.UserCard;
import org.aldeon.model.Identity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IdentitiesController {

    @FXML protected FlowPane container;

    public static final String FXML_FILE = "views/Identities.fxml";

    private Map<Key, UserCard> identityCards = new HashMap<>();

    public static Node create() {
        return Gui2Utils.loadFXMLfromDefaultPath(FXML_FILE);
    }

    public void initialize() {
        Gui2Utils.guiDb().getIdentities(new Callback<Set<Identity>>() {
            @Override
            public void call(Set<Identity> identities) {
                for(Identity identity: identities) {
                    addCard(identity);
                }
            }
        });
    }

    protected void addCard(Identity identity) {
        if(identityCards.get(identity.getPublicKey()) == null) {
            UserCard card = new UserCard();
            card.setUser(identity);
            addToContainer(card);

            identityCards.put(identity.getPublicKey(), card);
        }
    }

    protected void delCard(Identity identity) {
        UserCard card = identityCards.get(identity.getPublicKey());
        if(card != null) {
            delFromContainer(card);
        }
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
