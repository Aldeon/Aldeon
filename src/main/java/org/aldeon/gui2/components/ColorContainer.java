package org.aldeon.gui2.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.aldeon.gui2.Gui2Utils;

public abstract class ColorContainer extends BorderPane {

    private final ObjectProperty<Color> color = new SimpleObjectProperty<>();
    private final ObjectProperty<Node> content = new SimpleObjectProperty<>();
    @FXML protected StackPane container;
    @FXML protected BorderPane main;

    public ColorContainer(){
        colorProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observableValue, Color oldColor, Color newColor) {
                main.setStyle("-fx-background-color: " + Gui2Utils.toWebHex(newColor));
            }
        });

        contentProperty().addListener(new ChangeListener<Node>() {
            @Override
            public void changed(ObservableValue<? extends Node> observableValue, Node oldNode, Node newNode) {
                container.getChildren().clear();
                container.getChildren().add(newNode);
            }
        });
    }

    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    public Color getColor() {
        return colorProperty().get();
    }

    public void setColor(Color color) {
        colorProperty().set(color);
    }

    public ObjectProperty<Node> contentProperty() {
        return content;
    }

    public Node getContent() {
        return contentProperty().get();
    }

    public void setContent(Node node) {
        contentProperty().set(node);
    }
}
