package org.aldeon.gui.controllers;

import javafx.scene.Parent;
import org.aldeon.model.Identifier;
import org.aldeon.model.Identity;

/**
 *
 */
public interface WriteResponseControlListener {
    void createdResponse(Parent wrcNode, WriteResponseController wrc, String responseText, Identity author, Identifier parentId, int nestingLevel);
}
