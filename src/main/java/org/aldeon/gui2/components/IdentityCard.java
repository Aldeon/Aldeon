package org.aldeon.gui2.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.aldeon.gui2.Gui2Utils;
import org.aldeon.model.Identity;

public class IdentityCard extends VerticalColorContainer {

    @FXML protected ColorizedImageView avatar;
    @FXML protected Label usernameLabel;
    @FXML protected Label identifierLabel;

    private final ObjectProperty<Identity> identityProperty = new SimpleObjectProperty<>();

    public IdentityCard() {
        super();
        Gui2Utils.loadFXMLandInjectController("/gui2/fxml/components/IdentityCard.fxml", this);

        colorProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observableValue, Color oldColor, Color newColor) {
                avatar.setColorize(newColor);
            }
        });

        identityProperty().addListener(new ChangeListener<Identity>() {
            @Override
            public void changed(ObservableValue<? extends Identity> observableValue, Identity oldIdentity, Identity newIdentity) {
                update(newIdentity);
            }
        });
    }

    protected void update(Identity identity) {
        usernameLabel.setText(identity.getName());
        identifierLabel.setText(identity.getPublicKey().toString());
    }

    public ObjectProperty<Identity> identityProperty() {
        return identityProperty;
    }

    public Identity getIdentity() {
        return identityProperty().get();
    }

    public void setIdentity(Identity identity) {
        identityProperty().set(identity);
    }
}
