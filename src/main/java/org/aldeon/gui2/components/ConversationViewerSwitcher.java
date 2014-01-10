package org.aldeon.gui2.components;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import org.aldeon.gui2.Gui2Utils;
import org.aldeon.model.Message;

public class ConversationViewerSwitcher extends BorderPane {

    private ObjectProperty<Message> focus = new SimpleObjectProperty<>();
    private ObjectProperty<EventHandler<? super ActionEvent>> onViewerClosed = new SimpleObjectProperty<>();

    @FXML protected Button back;
    @FXML protected Button toggle;
    @FXML protected SlidingStackPane slider;
    protected ConversationViewer conversationViewer;
    protected boolean isList = true;

    public ConversationViewerSwitcher() {
        super();
        Gui2Utils.loadFXMLandInjectController("/gui2/fxml/components/ConversationViewerSwitcher.fxml", this);

        show(new ListConversationViewer());
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (conversationViewer != null) {
                    conversationViewer.onRemovedFromScene();
                }
                if (getOnViewerClosed() != null) {
                    getOnViewerClosed().handle(actionEvent);
                }
            }
        });
        focusProperty().addListener(new ChangeListener<Message>() {
            @Override
            public void changed(ObservableValue<? extends Message> observableValue, Message message, Message message2) {
                if(message2 == null) {
                    back.getOnAction().handle(null);
                }
            }
        });
    }

    @FXML protected void onToggle(ActionEvent event) {
        if(isList) {
            show(new TreeConversationViewer());
        } else {
            show(new ListConversationViewer());
        }
        isList = !isList;
    }

    protected void show(ConversationViewer viewer) {
        viewer.setFocus(getFocus());
        unshow();
        Bindings.bindBidirectional(focusProperty(), viewer.focusProperty());
        slider.insertWithoutAnimation(viewer);
        conversationViewer = viewer;
    }

    protected void unshow() {
        if(conversationViewer != null) {
            Bindings.unbindBidirectional(focusProperty(), conversationViewer.focusProperty());
            slider.removeWithoutAnimation(conversationViewer);
            conversationViewer.onRemovedFromScene();
            conversationViewer = null;
        }
    }

    public ObjectProperty<Message> focusProperty() {
        return focus;
    }

    public Message getFocus() {
        return focusProperty().get();
    }

    public void setFocus(Message message) {
        focusProperty().set(message);
    }

    public ObjectProperty<EventHandler<? super ActionEvent>> onViewerClosedProperty() {
        return onViewerClosed;
    }

    public EventHandler<? super ActionEvent> getOnViewerClosed() {
        return onViewerClosedProperty().get();
    }

    public void setOnViewerClosed(EventHandler<? super ActionEvent> eventHandler) {
        onViewerClosedProperty().set(eventHandler);
    }
}
