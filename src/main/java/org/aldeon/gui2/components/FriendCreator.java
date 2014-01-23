package org.aldeon.gui2.components;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import org.aldeon.crypt.Key;
import org.aldeon.crypt.KeyGen;
import org.aldeon.crypt.rsa.RsaModule;
import org.aldeon.gui2.Gui2Utils;
import org.aldeon.gui2.various.DeterministicColorGenerator;
import org.aldeon.gui2.various.IdentityEvent;
import org.aldeon.gui2.various.UserEvent;
import org.aldeon.model.Identity;
import org.aldeon.model.User;
import org.aldeon.model.UserImpl;

/**
 *
 */
public class FriendCreator extends BorderPane {
    @FXML protected TextField nameTextField;
    @FXML protected TextField hashTextField;
    @FXML protected ColorizedImageView avatar;

    private final ObjectProperty<EventHandler<UserEvent>> onCreatorClosed = new SimpleObjectProperty<>();
    private Key publicKey;

    public FriendCreator(Key newUserKey) {
        super();
        Gui2Utils.loadFXMLandInjectController("/gui2/fxml/components/FriendCreator.fxml", this);

        publicKey = newUserKey;
        update("");
    }

    public FriendCreator(User user) {
        super();
        Gui2Utils.loadFXMLandInjectController("/gui2/fxml/components/FriendCreator.fxml", this);

        publicKey = user.getPublicKey();
        update(user.getName());
    }

    @FXML protected void onOk(ActionEvent event) {
        triggerEvent(UserImpl.create(nameTextField.getText(), publicKey), false);
    }

    @FXML protected void onCancel(ActionEvent event) {
        triggerEvent(null, false);
    }

    @FXML protected void onDelete(ActionEvent event) {
        triggerEvent(null, true);
    }

    private void triggerEvent(UserImpl user, boolean toDelete) {
        EventHandler<UserEvent> handler = getOnCreatorClosed();
        if(handler != null) {
            handler.handle(new UserEvent(user, toDelete));
        }
    }

    private void update(String name) {
        nameTextField.setText(name);
        hashTextField.setText(publicKey.toString());
        avatar.setColorize(DeterministicColorGenerator.get(publicKey.hashCode()));
    }

    public ObjectProperty<EventHandler<UserEvent>> onCreatorClosedProperty() {
        return onCreatorClosed;
    }

    public EventHandler<UserEvent> getOnCreatorClosed() {
        return onCreatorClosedProperty().get();
    }

    public void setOnCreatorClosed(EventHandler<UserEvent> eventHandler) {
        onCreatorClosedProperty().set(eventHandler);
    }
}
