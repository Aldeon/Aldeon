package org.aldeon.gui2.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.aldeon.core.CoreModule;
import org.aldeon.events.Callback;
import org.aldeon.gui2.Gui2Utils;
import org.aldeon.gui2.components.ConversationViewer;
import org.aldeon.gui2.components.MessageCreator;
import org.aldeon.gui2.components.SlidingStackPane;
import org.aldeon.gui2.components.TopicCard;
import org.aldeon.gui2.components.UnknownTopicCard;
import org.aldeon.gui2.various.Direction;
import org.aldeon.gui2.various.FxCallback;
import org.aldeon.gui2.various.GuiDbUtils;
import org.aldeon.gui2.various.MessageEvent;
import org.aldeon.model.Identifier;
import org.aldeon.model.Message;
import org.aldeon.utils.codec.Codec;
import org.aldeon.utils.codec.base64.Base64Module;
import org.aldeon.utils.conversion.ConversionException;
import org.aldeon.utils.helpers.Callbacks;

import java.util.Set;

public class TopicsController {

    public static final String FXML_FILE = "views/Topics.fxml";
    private static final Codec base64 = new Base64Module().get();

    // Topics view elements
    @FXML protected SlidingStackPane slider;
    @FXML protected TextField watchTopicTextField;
    @FXML protected Button watchTopicButton;
    @FXML protected VBox topics;

    private final ObservableList<TopicCard> topicCards = FXCollections.observableArrayList();

    public void initialize() {
        Bindings.bindContent(topics.getChildren(), topicCards);
        GuiDbUtils.db().getMessagesByParentId(Identifier.empty(), new Callback<Set<Message>>() {
            @Override
            public void call(Set<Message> topicsSet) {
                for (Message topic : topicsSet) {
                    addTopic(topic);
                }
            }
        });
    }

    private void addTopic(final Message topic) {
        final TopicCard card = new TopicCard(topic);
        card.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                focusOnTopic(topic);
            }
        });
        card.setOnRemove(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                topicCards.remove(card);
                GuiDbUtils.db().deleteMessage(card.getMessage().getIdentifier(), Callbacks.<Boolean>emptyCallback());
            }
        });
        card.setOnResume(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CoreModule.getInstance().getTopicManager().addTopic(topic.getIdentifier());
            }
        });
        card.setOnPause(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CoreModule.getInstance().getTopicManager().delTopic(topic.getIdentifier());
            }
        });
        topicCards.add(card);
    }

    private void focusOnTopic(Message topic) {
        final ConversationViewer viewer = new ConversationViewer();
        viewer.setFocus(topic);
        viewer.setOnViewerClosed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                slider.slideOut(viewer, Direction.RIGHT);
            }
        });
        slider.slideIn(viewer, Direction.RIGHT);
    }

    public static Node create() {
        return Gui2Utils.loadFXMLfromDefaultPath(FXML_FILE);
    }

    @FXML public void newTopicClicked(ActionEvent actionEvent) {
        final MessageCreator creator = new MessageCreator(Identifier.empty());
        creator.setOnCreatorClosed(new EventHandler<MessageEvent>() {
            @Override
            public void handle(final MessageEvent messageEvent) {
                if(messageEvent.message() != null) {
                    GuiDbUtils.db().insertMessage(messageEvent.message(), new FxCallback<Boolean>() {
                        @Override
                        protected void react(Boolean val) {
                            addTopic(messageEvent.message());
                        }
                    });
                }
                slider.slideOut(creator, Direction.RIGHT);
            }
        });
        slider.slideIn(creator, Direction.RIGHT);
    }

    @FXML public void watchTopicClicked(ActionEvent actionEvent) {
        String topicHash = watchTopicTextField.getText();
        try {
            Identifier topic = Identifier.fromByteBuffer(base64.decode(topicHash), false);
            topicCards.add(new UnknownTopicCard(topic));
        } catch (ConversionException e) {
            System.out.println("illegal identifier");
        }
    }
}
