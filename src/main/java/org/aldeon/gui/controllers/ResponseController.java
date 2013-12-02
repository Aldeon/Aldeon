package org.aldeon.gui.controllers;

import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.aldeon.gui.GUIController;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 */
public class ResponseController extends Pane implements Initializable {
    public Text message;
    public Rectangle backgroundRectangle;
    public Rectangle colorRectangle;
    public Pane respPane;
    public HBox windowContainer;
    public Separator separator;

    public Parent toPass;

    private int nestingLevel;

    private static final double initialHeight = 132;

    ResponseControlListener listener;

    public void deleteClicked(MouseEvent event) {
        if (listener != null) listener.responseDeleteClicked(toPass);
    }

    public void respondClicked(MouseEvent event) {
        if (listener != null) listener.responseClicked(this, message.getText());
    }

    public void writeResponseClicked(MouseEvent event) {
        if (listener != null) listener.responseRespondClicked(toPass, nestingLevel);
    }

    public void mouseOnIcon(MouseEvent event) {
    }

    public void registerListener(ResponseControlListener listener) {
        this.listener = listener;
    }

    public void setMessage(String msg, int nestingLevel) {
        this.message.setText(msg);
        this.nestingLevel = nestingLevel;

        //borderPane.prefWidthProperty().bind(root.widthProperty());
        //respPane.prefHeightProperty().bind(colorRectangle.heightProperty());
        //colorRectangle.heightProperty().bind(respPane.prefHeightProperty());
        //respPane.prefHeightProperty().bindBidirectional(colorRectangle.heightProperty());
        backgroundRectangle.setHeight(
                Math.max(message.layoutBoundsProperty().get().getHeight()
                        + message.getLayoutY(),
                        initialHeight));

        colorRectangle.setHeight(
                backgroundRectangle.getHeight()-2);
        respPane.setPrefHeight(colorRectangle.getHeight() + 30);
        respPane.prefWidthProperty().bindBidirectional(windowContainer.prefWidthProperty());
        //separator.prefWidthProperty().bindBidirectional(windowContainer.prefWidthProperty());
        separator.prefHeightProperty().bindBidirectional(colorRectangle.heightProperty());

        windowContainer.setPadding(new Insets(0,10,0,35 * nestingLevel)); //top right bottom left
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //initialHeight = backgroundRectangle.getHeight();
    }
}


