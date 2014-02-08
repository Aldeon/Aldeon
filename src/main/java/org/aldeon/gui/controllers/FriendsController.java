package org.aldeon.gui.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import org.aldeon.core.CoreModule;
import org.aldeon.core.events.UserAddedEvent;
import org.aldeon.crypt.Key;
import org.aldeon.events.Callback;
import org.aldeon.events.EventLoop;
import org.aldeon.gui.Gui2Utils;
import org.aldeon.gui.components.FriendCreator;
import org.aldeon.gui.components.IdentityCreator;
import org.aldeon.gui.components.SlidingStackPane;
import org.aldeon.gui.components.UserCard;
import org.aldeon.gui.various.*;
import org.aldeon.model.Identity;
import org.aldeon.model.User;
import org.aldeon.utils.helpers.Callbacks;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class FriendsController {
    @FXML
    protected SlidingStackPane slider;
    @FXML protected FlowPane container;

    public static final String FXML_FILE = "views/Friends.fxml";

    private Map<Key, UserCard> friendsCards = new HashMap<>();

    public static Node create() {
        return Gui2Utils.loadFXMLfromDefaultPath(FXML_FILE);
    }

    public void initialize() {
        EventLoop loop = CoreModule.getInstance().getEventLoop();

        loop.assign(UserAddedEvent.class, new FxCallback<UserAddedEvent>() {
            @Override
            protected void react(UserAddedEvent event) {
                addCard(event.getUser());
            }
        });

//        loop.assign(UserChan.class, new FxCallback<IdentityRemovedEvent>() {
//            @Override
//            protected void react(IdentityRemovedEvent event) {
//                delCard(event.getPublicKey());
//            }
//        });

        GuiDbUtils.db().getUsers(new Callback<Set<User>>() {
            @Override
            public void call(Set<User> users) {
                for(User user: users) {
                    addCard(user);
                }
            }
        });
    }

    private void addCard(User user) {         //change to user
        if(friendsCards.get(user.getPublicKey()) == null) {
            final UserCard card = new UserCard();
            card.setUser(user);
            friendsCards.put(user.getPublicKey(), card);
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
        UserCard card = friendsCards.get(publicKey);
        if(card != null) {
            container.getChildren().remove(card);
            friendsCards.remove(publicKey);
        }
    }

    //TODO remove
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
        System.out.println("card clicked!!!");
        GuiDbUtils.db().getUser(card.getUser().getPublicKey(), new FxCallback<User>() {
            @Override
            protected void react(User user) {
                if (user != null) {
                    editUser(user);
                }
            }
        });

//        GuiDbUtils.db().getIdentity(card.getUser().getPublicKey(), new FxCallback<Identity>() {
//            @Override
//            protected void react(Identity identity) {
//                if(identity != null) {
//                    editIdentity(identity);
//                }
//            }
//        });
    }

    private void editUser(final User user) {
        final FriendCreator creator = new FriendCreator(user);

        slider.slideIn(creator, Direction.RIGHT);

        creator.setOnCreatorClosed(new EventHandler<UserEvent>() {
            @Override
            public void handle(final UserEvent userEvent) {
                slider.slideOut(creator, Direction.RIGHT);
                if (userEvent.user() != null) {
                    delCard(user.getPublicKey());
                    GuiDbUtils.db().deleteUser(user.getPublicKey(), new Callback<Boolean>() {
                        @Override
                        public void call(Boolean deleteStatus) {
                            GuiDbUtils.db().insertUser(userEvent.user(), new FxCallback<Boolean>() {
                                @Override
                                protected void react(Boolean val) {
                                     addCard(userEvent.user());
                                }
                            });
                        }
                    });
                } else if (userEvent.toDelete()) {
                    delCard(user.getPublicKey());
                    GuiDbUtils.db().deleteUser(user.getPublicKey(), Callbacks.<Boolean>emptyCallback());
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
