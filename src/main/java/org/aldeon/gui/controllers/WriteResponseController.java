package org.aldeon.gui.controllers;

import javafx.geometry.Insets;
import javafx.scene.Parent;
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

    private int nestingLevel;
    private Parent wrcNode;
    private WriteResponseControlListener listener;

    public void setNode(Parent parent) {
        wrcNode = parent;
    }
    public void registerListener(WriteResponseControlListener listener) {
        this.listener = listener;
    }
    public void setNestingLevel(int nestingLevel) {
        this.nestingLevel = nestingLevel;
        windowContainer.setPadding(new Insets(0,10,0,30 + 35 * nestingLevel)); //top right bottom left
    }
    public void sendResponseClicked(MouseEvent event) {
        listener.createdResponse(wrcNode, responseContent.getText(), nestingLevel);
    }
}
