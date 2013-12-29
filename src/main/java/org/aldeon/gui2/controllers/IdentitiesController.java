package org.aldeon.gui2.controllers;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import org.aldeon.core.CoreModule;
import org.aldeon.core.events.IdentityAddedEvent;
import org.aldeon.core.events.IdentityRemovedEvent;
import org.aldeon.crypt.Key;
import org.aldeon.events.ACB;
import org.aldeon.events.Callback;
import org.aldeon.events.EventLoop;
import org.aldeon.gui2.Gui2Utils;
import org.aldeon.gui2.components.IdentityCreator;
import org.aldeon.gui2.components.SlidingStackPane;
import org.aldeon.gui2.components.UserCard;
import org.aldeon.gui2.various.Direction;
import org.aldeon.gui2.various.IdentityEvent;
import org.aldeon.model.Identity;
import org.aldeon.utils.helpers.Callbacks;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

public class IdentitiesController {

    @FXML protected SlidingStackPane slider;
    @FXML protected BorderPane cardView;
    @FXML protected FlowPane container;

    public static final String FXML_FILE = "views/Identities.fxml";

    private Map<Key, UserCard> identityCards = new HashMap<>();

    public static Node create() {
        return Gui2Utils.loadFXMLfromDefaultPath(FXML_FILE);
    }

    public void initialize() {
        EventLoop loop = CoreModule.getInstance().getEventLoop();
        Executor ex = Gui2Utils.fxExecutor();

        loop.assign(IdentityAddedEvent.class, new ACB<IdentityAddedEvent>(ex) {
            @Override
            protected void react(IdentityAddedEvent event) {
                addCard(event.getIdentity());
            }
        });

        loop.assign(IdentityRemovedEvent.class, new ACB<IdentityRemovedEvent>(ex) {
            @Override
            protected void react(IdentityRemovedEvent event) {
                delCard(event.getPublicKey());
            }
        });

        Gui2Utils.db().getIdentities(new Callback<Set<Identity>>() {
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
            identityCards.put(identity.getPublicKey(), card);
            container.getChildren().add(card);
        }
    }

    protected void delCard(Key publicKey) {
        UserCard card = identityCards.get(publicKey);
        if(card != null) {
            container.getChildren().remove(card);
        }
    }

    @FXML protected void clickedNewId(MouseEvent event) {
        final IdentityCreator creator = new IdentityCreator();

        creator.setOnCreatorClosed(new EventHandler<IdentityEvent>() {
            @Override
            public void handle(IdentityEvent identityEvent) {
                slider.slideOut(creator, Direction.RIGHT);
                if(identityEvent.identity() != null) {
                    Gui2Utils.db().insertIdentity(identityEvent.identity(), Callbacks.<Boolean>emptyCallback());
                }
            }
        });

        slider.slideIn(creator, Direction.RIGHT);
    }
}
