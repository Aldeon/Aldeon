package org.aldeon.gui2.components;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.aldeon.gui2.Gui2Utils;

public class SidebarButton extends Pane {

    private final StringProperty imagePath = new SimpleStringProperty();
    @FXML private Label label;
    @FXML private ImageView imageView;

    public SidebarButton() {
        super();

        Gui2Utils.loadFXMLandInjectController("/gui2/fxml/components/SidebarButton.fxml", this);

        imagePathProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                imageView.setImage(null);
                imageView.setImage(new Image(newValue));
            }
        });

        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                onMouseEnter();
            }
        });

        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                onMouseExit();
            }
        });

        onMouseExit();
    }

    ////

    private void onMouseEnter() {
        imageView.setOpacity(0.8);
        label.setOpacity(0.8);
        setStyle("-fx-background-color: rgba(0, 0, 0, 0.1)");
        setCursor(Cursor.HAND);
    }

    private void onMouseExit() {
        imageView.setOpacity(0.4);
        label.setOpacity(0.4);
        setStyle("-fx-background-color: transparent");
        setCursor(Cursor.DEFAULT);
    }

    public String getText() {
        return textProperty().get();
    }

    public void setText(String value) {
        textProperty().set(value);
    }

    public StringProperty textProperty() {
        return label.textProperty();
    }

    ////

    public String getImagePath() {
        return imagePathProperty().get();
    }

    public void setImagePath(String value) {
        imagePathProperty().set(value);
    }

    public StringProperty imagePathProperty() {
        return imagePath;
    }
}
