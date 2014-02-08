package org.aldeon.gui.components;

import javafx.scene.paint.Color;
import org.aldeon.model.Identifier;

public class UnknownTopicCard extends TopicCard {

    private Identifier id;

    public UnknownTopicCard(Identifier topic) {
        super();
        id = topic;
        setColor(Color.web("#222222"));
        messageIdLabel.setText(topic.toString());
        messageContentLabel.setText("Downloading topic...");
    }

    @Override
    public Identifier getMessageId() {
        if(getMessage() != null) return getMessage().getIdentifier();
        return id;
    }
}
