package org.aldeon.gui.controllers;

import javafx.scene.Parent;

/**
 *
 */
public interface ResponseControlListener {
    void responseHideClicked(Parent rcNode, ResponseController rc);
    void responseShowClicked(Parent rcNode, ResponseController rc);
    void responseClicked(ResponseController rc, String text);
    void responseRespondClicked(Parent rcNode, ResponseController rc, int nestingLevel);
    void responseDeleteClicked(Parent responseNode, ResponseController rc);
}

//or use event loop