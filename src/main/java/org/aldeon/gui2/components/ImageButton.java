package org.aldeon.gui2.components;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageButton extends Button {

    private final StringProperty image = new SimpleStringProperty();

    public ImageButton() {
        super();

        setStyle("-fx-background-color: transparent; -fx-padding: 0");

        imageProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                update(s2);
            }
        });
    }

    private void update(String imagePath) {
        try {
            setGraphic(new ImageView(new Image(imagePath)));
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid image path");
        }
    }

    public StringProperty imageProperty() {
        return image;
    }

    public String getImage() {
        return imageProperty().get();
    }

    public void setImage(String val) {
        imageProperty().set(val);
    }
}
