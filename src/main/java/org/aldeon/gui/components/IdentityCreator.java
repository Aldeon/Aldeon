package org.aldeon.gui.components;

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
import org.aldeon.crypt.KeyGen;
import org.aldeon.crypt.rsa.RsaModule;
import org.aldeon.gui.Gui2Utils;
import org.aldeon.gui.various.DeterministicColorGenerator;
import org.aldeon.gui.various.IdentityEvent;
import org.aldeon.model.Identity;

public class IdentityCreator extends BorderPane {

    @FXML protected Button shuffleButton;
    @FXML protected TextField nameTextField;
    @FXML protected TextField hashTextField;
    @FXML protected ColorizedImageView avatar;

    private final BooleanProperty shuffleAllowed = new SimpleBooleanProperty(true);
    private final ObjectProperty<EventHandler<IdentityEvent>> onCreatorClosed = new SimpleObjectProperty<>();

    private final KeyGen generator = new RsaModule().get();
    private KeyGen.KeyPair keyPair;

    public IdentityCreator() {
        this(null);
    }

    public IdentityCreator(Identity identity) {
        super();
        Gui2Utils.loadFXMLandInjectController("/gui2/fxml/components/IdentityCreator.fxml", this);

        if(identity == null) {
            shuffleKeys();
            update("");
        } else {
            keyPair = new KeyGen.KeyPair(identity.getPublicKey(), identity.getPrivateKey());
            update(identity.getName());
        }

        shuffleAllowedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                shuffleButton.setVisible(newValue);
            }
        });
    }

    @FXML protected void onOk(ActionEvent event) {
        triggerEvent(Identity.create(nameTextField.getText(), keyPair.publicKey, keyPair.privateKey));
    }

    @FXML protected void onShuffle(ActionEvent event) {
        shuffleKeys();
        update(nameTextField.getText());
    }

    @FXML protected void onCancel(ActionEvent event) {
        triggerEvent(null);
    }

    private void update(String name) {
        nameTextField.setText(name);
        hashTextField.setText(keyPair.publicKey.toString());
        avatar.setColorize(DeterministicColorGenerator.get(keyPair.publicKey.hashCode()));
    }

    private void shuffleKeys() {
        keyPair = generator.generate();
    }

    private void triggerEvent(Identity identity) {
        EventHandler<IdentityEvent> handler = getOnCreatorClosed();
        if(handler != null) {
            handler.handle(new IdentityEvent(identity));
        }
    }

    public BooleanProperty shuffleAllowedProperty() {
        return shuffleAllowed;
    }

    public boolean getShuffleAllowed() {
        return shuffleAllowedProperty().get();
    }

    public void setShuffleAllowed(boolean allowed) {
        shuffleAllowedProperty().set(allowed);
    }

    public ObjectProperty<EventHandler<IdentityEvent>> onCreatorClosedProperty() {
        return onCreatorClosed;
    }

    public EventHandler<IdentityEvent> getOnCreatorClosed() {
        return onCreatorClosedProperty().get();
    }

    public void setOnCreatorClosed(EventHandler<IdentityEvent> eventHandler) {
        onCreatorClosedProperty().set(eventHandler);
    }
}
