package org.aldeon.gui2.elements;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.net.URL;

public class SidebarButton extends Pane {

    private final StringProperty imagePath = new SimpleStringProperty();
    private final DoubleProperty normalOpacity = new SimpleDoubleProperty();
    private final DoubleProperty hoverOpacity = new SimpleDoubleProperty();
    @FXML private Label label;
    @FXML private ImageView imageView;

    public SidebarButton() {
        super();

        URL url = getClass().getResource("SidebarButton.fxml");

        FXMLLoader fxmlLoader = new FXMLLoader(url);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

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
                System.out.println("entered");
            }
        });

        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("exited");
            }
        });

        normalOpacityProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                System.out.println("changed from " + number + " to " + number2);
            }
        });

        setNormalOpacity(0.0);

    }

    ////

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

    ////

    public double getNormalOpacity() {
        return normalOpacityProperty().get();
    }

    public void setNormalOpacity(Double value) {
        normalOpacityProperty().set(value);
    }

    public DoubleProperty normalOpacityProperty() {
        return normalOpacity;
    }

    ////

    public double getHoverOpacity() {
        return hoverOpacityProperty().get();
    }

    public void setHoverOpacity(Double value) {
        hoverOpacityProperty().set(value);
    }

    public DoubleProperty hoverOpacityProperty() {
        return hoverOpacity;
    }

}
