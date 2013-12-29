package org.aldeon.gui2.components;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import org.aldeon.crypt.KeyGen;
import org.aldeon.crypt.rsa.RsaModule;
import org.aldeon.gui2.Gui2Utils;
import org.aldeon.gui2.various.DeterministicColorGenerator;
import org.aldeon.gui2.various.IdentityEvent;
import org.aldeon.model.Identity;

public class IdentityCreator extends BorderPane {

    @FXML protected Button okButton;
    @FXML protected Button shuffleButton;
    @FXML protected Button cancelButton;
    @FXML protected TextField nameTextField;
    @FXML protected TextField hashTextField;
    @FXML protected ColorizedImageView avatar;

    private final BooleanProperty shuffleAllowed = new SimpleBooleanProperty(true);
    private final ObjectProperty<EventHandler<IdentityEvent>> onCreatorClosed = new SimpleObjectProperty<>();

    private final KeyGen generator = new RsaModule().get();
    private KeyGen.KeyPair keyPair;

    public IdentityCreator() {
        super();
        Gui2Utils.loadFXMLandInjectController("/gui2/fxml/components/IdentityCreator.fxml", this);

        shuffleAndUpdate();
        nameTextField.setText("");

        shuffleAllowedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                shuffleButton.setVisible(newValue);
            }
        });

        shuffleButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                shuffleAndUpdate();
            }
        });

        okButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                triggerEvent(Identity.create(nameTextField.getText(), keyPair.publicKey, keyPair.privateKey));
            }
        });

        cancelButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                triggerEvent(null);
            }
        });
    }

    private void shuffleAndUpdate() {
        keyPair = generator.generate();
        hashTextField.setText(keyPair.publicKey.toString());
        avatar.setColorize(DeterministicColorGenerator.getColorForSeed(keyPair.publicKey.hashCode()));
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
