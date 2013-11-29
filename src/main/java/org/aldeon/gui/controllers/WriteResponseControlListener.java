package org.aldeon.gui.controllers;

import javafx.scene.Parent;

/**
 *
 */
public interface WriteResponseControlListener {
    void createdResponse(Parent wrcNode, String responseText, int nestingLevel);
}
