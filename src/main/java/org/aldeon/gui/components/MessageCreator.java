package org.aldeon.gui.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.aldeon.events.Callback;
import org.aldeon.gui.GuiUtils;
import org.aldeon.gui.various.DeterministicColorGenerator;
import org.aldeon.gui.various.GuiDbUtils;
import org.aldeon.gui.various.MessageEvent;
import org.aldeon.model.Identifier;
import org.aldeon.model.Identity;
import org.aldeon.model.Message;
import org.aldeon.utils.helpers.Messages;

import java.util.Set;

public class MessageCreator extends HorizontalColorContainer {

    @FXML protected TextArea textArea;
    @FXML protected ListView<Identity> identities;
    @FXML protected Button okButton;

    private final Identifier parent;
    private final ObjectProperty<EventHandler<MessageEvent>> onCreatorClosed = new SimpleObjectProperty<>();

    private Identity author;

    public MessageCreator(final Identifier parent) {
        super();
        GuiUtils.loadFXMLandInjectController("/gui/fxml/components/MessageCreator.fxml", this);
        setAuthor(null);
        this.parent = parent;

        identities.setCellFactory(new javafx.util.Callback<ListView<Identity>, ListCell<Identity>>() {
            @Override
            public ListCell<Identity> call(ListView<Identity> identityListView) {
                return new IdentityCell();
            }
        });
        identities.setItems(FXCollections.<Identity>observableArrayList());
        identities.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        identities.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Identity>() {
            @Override
            public void changed(ObservableValue<? extends Identity> observableValue, Identity identity, Identity identity2) {
                setAuthor(identity2);
            }
        });

        fillData();
    }

    @FXML protected void onOk(ActionEvent event) {
        trigger(Messages.createAndSign(parent, author.getPublicKey(), author.getPrivateKey(), textArea.getText()));
    }

    @FXML protected void onCancel(ActionEvent event) {
        trigger(null);
    }

    private void fillData() {
        GuiDbUtils.db().getIdentities(new Callback<Set<Identity>>() {
            @Override
            public void call(Set<Identity> val) {
                identities.getItems().addAll(val);
            }
        });
    }

    private void trigger(Message message) {
        EventHandler<MessageEvent> handler = getOnCreatorClosed();
        if(handler != null) {
            handler.handle(new MessageEvent(message));
        }
    }

    private void setAuthor(Identity identity) {
        author = identity;
        if(identity == null) {
            setColor(Color.web("#222222"));
            okButton.setDisable(true);
        } else {
            setColor(DeterministicColorGenerator.get(identity));
            okButton.setDisable(false);
        }
    }

    public ObjectProperty<EventHandler<MessageEvent>> onCreatorClosedProperty() {
        return onCreatorClosed;
    }

    public EventHandler<MessageEvent> getOnCreatorClosed() {
        return onCreatorClosedProperty().get();
    }

    public void setOnCreatorClosed(EventHandler<MessageEvent> eventHandler) {
        onCreatorClosedProperty().set(eventHandler);
    }

    private static class IdentityCell extends ListCell<Identity> {
        @Override
        public void updateItem(Identity identity, boolean empty) {
            super.updateItem(identity, empty);
            if(identity != null) {
                Color color = DeterministicColorGenerator.get(identity);
                Rectangle rect = new Rectangle(20, 20);
                rect.setFill(color);
                setGraphic(rect);
                setText(identity.getName());
                setStyle("-fx-text-fill: " + GuiUtils.toWebHex(color));
            }
        }
    }
}
