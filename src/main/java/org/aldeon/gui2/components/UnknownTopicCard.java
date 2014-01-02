package org.aldeon.gui2.components;

import javafx.scene.paint.Color;
import org.aldeon.model.Identifier;

public class UnknownTopicCard extends TopicCard {

    public UnknownTopicCard(Identifier topic) {
        super();
        setColor(Color.web("#222222"));
        messageIdLabel.setText(topic.toString());
        messageContentLabel.setText("Downloading topic...");
    }
}
