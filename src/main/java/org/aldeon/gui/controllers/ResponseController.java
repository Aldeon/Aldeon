package org.aldeon.gui.controllers;

import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.aldeon.gui.GUIController;

/**
 *
 */
public class ResponseController extends Pane {
    public Text message;
    public Rectangle backgroundRectangle;
    public Rectangle colorRectangle;
    public Pane respPane;
    public HBox windowContainer;

    public void setMessage(String msg, int nestingLevel) {
        this.message.setText(msg);

        //borderPane.prefWidthProperty().bind(root.widthProperty());
        //respPane.prefHeightProperty().bind(colorRectangle.heightProperty());
        //colorRectangle.heightProperty().bind(respPane.prefHeightProperty());
        //respPane.prefHeightProperty().bindBidirectional(colorRectangle.heightProperty());

        backgroundRectangle.setHeight(
            message.layoutBoundsProperty().get().getHeight()
            + message.getLayoutY() - backgroundRectangle.getLayoutY());

        colorRectangle.setHeight(
                backgroundRectangle.getHeight());
        respPane.setPrefHeight(colorRectangle.getHeight() + 30);
        respPane.prefWidthProperty().bindBidirectional(windowContainer.prefWidthProperty());

        windowContainer.setPadding(new Insets(0,10,0,50 * nestingLevel)); //top right bottom left
    }

}


