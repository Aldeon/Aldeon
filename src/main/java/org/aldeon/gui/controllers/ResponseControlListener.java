package org.aldeon.gui.controllers;

import javafx.scene.Parent;

/**
 *
 */
public interface ResponseControlListener {
    void responseClicked(ResponseController rc, String text);
    void responseRespondClicked(Parent rc, int nestingLevel);
    void responseDeleteClicked(ResponseController rc);
}

//or use event loop