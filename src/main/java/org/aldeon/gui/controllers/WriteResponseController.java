package org.aldeon.gui.controllers;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.aldeon.model.Identifier;

/**
 *
 */
public class WriteResponseController {
    public Button sendResponse;
    public TextArea responseContent;
    public HBox windowContainer;

    private int nestingLevel;
    private Parent wrcNode;
    private WriteResponseControlListener listener;
    private Identifier parentIdentifier;
    private ResponseController parentController;

    public void setNode(Parent parent) {
        wrcNode = parent;
    }

    public void setParentController(ResponseController rc) {
        parentController = rc;
    }
    public ResponseController getParentController() {
        return parentController;
    }
    public void setParentIdentifier(Identifier parentIdentifier) {
        this.parentIdentifier = parentIdentifier;
    }
    public void registerListener(WriteResponseControlListener listener) {
        this.listener = listener;
    }
    public void setNestingLevel(int nestingLevel) {
        this.nestingLevel = nestingLevel;
        windowContainer.setPadding(new Insets(0,10,0,30 + 35 * nestingLevel)); //top right bottom left
    }
    public void sendResponseClicked(MouseEvent event) {
        listener.createdResponse(wrcNode, this,  responseContent.getText(), parentIdentifier, nestingLevel);
    }
}
