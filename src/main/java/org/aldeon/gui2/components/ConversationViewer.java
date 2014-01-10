package org.aldeon.gui2.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.BorderPane;
import org.aldeon.model.Message;

public abstract class ConversationViewer extends BorderPane {

    private ObjectProperty<Message> focus = new SimpleObjectProperty<>();

    public ObjectProperty<Message> focusProperty() {
        return focus;
    }

    public Message getFocus() {
        return focusProperty().get();
    }

    public void setFocus(Message message) {
        focusProperty().set(message);
    }

    public abstract void onRemovedFromScene();
}
