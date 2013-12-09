package org.aldeon.gui.controllers;

import javafx.scene.Parent;
import org.aldeon.model.Identifier;

/**
 *
 */
public interface WriteResponseControlListener {
    void createdResponse(Parent wrcNode, WriteResponseController wrc, String responseText, Identifier parentId, int nestingLevel);
}
