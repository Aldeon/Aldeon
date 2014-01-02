package org.aldeon.gui2.controllers;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import org.aldeon.core.CoreModule;
import org.aldeon.core.events.IdentityAddedEvent;
import org.aldeon.core.events.IdentityRemovedEvent;
import org.aldeon.crypt.Key;
import org.aldeon.events.Callback;
import org.aldeon.events.EventLoop;
import org.aldeon.gui2.Gui2Utils;
import org.aldeon.gui2.components.IdentityCreator;
import org.aldeon.gui2.components.SlidingStackPane;
import org.aldeon.gui2.components.UserCard;
import org.aldeon.gui2.various.Direction;
import org.aldeon.gui2.various.FxCallback;
import org.aldeon.gui2.various.GuiDbUtils;
import org.aldeon.gui2.various.IdentityEvent;
import org.aldeon.model.Identity;
import org.aldeon.utils.helpers.Callbacks;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IdentitiesController {

    @FXML protected SlidingStackPane slider;
    @FXML protected FlowPane container;

    public static final String FXML_FILE = "views/Identities.fxml";

    private Map<Key, UserCard> identityCards = new HashMap<>();

    public static Node create() {
        return Gui2Utils.loadFXMLfromDefaultPath(FXML_FILE);
    }

    public void initialize() {
        EventLoop loop = CoreModule.getInstance().getEventLoop();

        loop.assign(IdentityAddedEvent.class, new FxCallback<IdentityAddedEvent>() {
            @Override
            protected void react(IdentityAddedEvent event) {
                addCard(event.getIdentity());
            }
        });

        loop.assign(IdentityRemovedEvent.class, new FxCallback<IdentityRemovedEvent>() {
            @Override
            protected void react(IdentityRemovedEvent event) {
                delCard(event.getPublicKey());
            }
        });

        GuiDbUtils.db().getIdentities(new Callback<Set<Identity>>() {
            @Override
            public void call(Set<Identity> identities) {
                for(Identity identity: identities) {
                    addCard(identity);
                }
            }
        });
    }

    private void addCard(Identity identity) {
        if(identityCards.get(identity.getPublicKey()) == null) {
            final UserCard card = new UserCard();
            card.setUser(identity);
            identityCards.put(identity.getPublicKey(), card);
            container.getChildren().add(card);
            card.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    cardClicked(card);
                }
            });
        }
    }

    private void delCard(Key publicKey) {
        UserCard card = identityCards.get(publicKey);
        if(card != null) {
            container.getChildren().remove(card);
            identityCards.remove(publicKey);
        }
    }

    @FXML protected void clickedNewId(MouseEvent event) {
        final IdentityCreator creator = new IdentityCreator();

        creator.setOnCreatorClosed(new EventHandler<IdentityEvent>() {
            @Override
            public void handle(IdentityEvent identityEvent) {
                slider.slideOut(creator, Direction.RIGHT);
                if(identityEvent.identity() != null) {
                    GuiDbUtils.db().insertIdentity(identityEvent.identity(), Callbacks.<Boolean>emptyCallback());
                }
            }
        });

        slider.slideIn(creator, Direction.RIGHT);
    }

    private void cardClicked(UserCard card) {
        GuiDbUtils.db().getIdentity(card.getUser().getPublicKey(), new FxCallback<Identity>() {
            @Override
            protected void react(Identity identity) {
                if(identity != null) {
                    editIdentity(identity);
                }
            }
        });
    }

    private void editIdentity(final Identity identity) {
        final IdentityCreator creator = new IdentityCreator(identity);
        creator.setShuffleAllowed(false);

        creator.setOnCreatorClosed(new EventHandler<IdentityEvent>() {
            @Override
            public void handle(final IdentityEvent identityEvent) {
                slider.slideOut(creator, Direction.RIGHT);
                if(identityEvent.identity() != null) {
                    delCard(identity.getPublicKey());
                    GuiDbUtils.db().deleteIdentity(identity.getPublicKey(), new Callback<Boolean>() {
                        @Override
                        public void call(Boolean deleteStatus) {
                            System.out.println("delete status: " + deleteStatus);
                            GuiDbUtils.db().insertIdentity(identityEvent.identity(), new FxCallback<Boolean>() {
                                @Override
                                protected void react(Boolean insertStatus) {
                                    System.out.println("insert status: " + insertStatus);
                                    addCard(identityEvent.identity());
                                }
                            });
                        }
                    });
                }
            }
        });

        slider.slideIn(creator, Direction.RIGHT);
    }
}
