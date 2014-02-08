package org.aldeon.gui.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.aldeon.gui.Gui2Utils;
import org.aldeon.gui.various.DeterministicColorGenerator;
import org.aldeon.model.User;

public class UserCard extends VerticalColorContainer {

    @FXML protected ColorizedImageView avatar;
    @FXML protected Label usernameLabel;
    @FXML protected Label identifierLabel;

    private final ObjectProperty<User> userProperty = new SimpleObjectProperty<>();

    public UserCard() {
        super();
        Gui2Utils.loadFXMLandInjectController("/gui2/fxml/components/UserCard.fxml", this);

        colorProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observableValue, Color oldColor, Color newColor) {
                avatar.setColorize(newColor);
            }
        });

        userProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observableValue, User oldUser, User newUser) {
                update(newUser);
            }
        });
    }

    protected void update(User user) {
        usernameLabel.setText(user.getName());
        identifierLabel.setText(user.getPublicKey().toString());
        setColor(DeterministicColorGenerator.get(user.getPublicKey().hashCode()));
    }

    public ObjectProperty<User> userProperty() {
        return userProperty;
    }

    public User getUser() {
        return userProperty().get();
    }

    public void setUser(User user) {
        userProperty().set(user);
    }
}
