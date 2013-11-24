package org.aldeon.gui.controllers;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 *
 */
public class WriteResponseController extends HBox {
    public Button sendResponse;
    public TextArea responseContent;
    public HBox windowContainer;

    public void setNestingLevel(int nestingLevel) {
        System.out.println("set nesting level invoked");
        windowContainer.setPadding(new Insets(0,10,0,50 * nestingLevel)); //top right bottom left
    }
    public void sendResponseClicked(MouseEvent event) {
        System.out.println("send response clicked");
    }
}
