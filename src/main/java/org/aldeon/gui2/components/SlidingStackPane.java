package org.aldeon.gui2.components;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class SlidingStackPane extends StackPane {

    public void slideIn(final Node node) {
        if(getChildren().contains(node)) {
            return;
        }

        getChildren().add(node);
        Rectangle clip = new Rectangle();
        clip.setWidth(getWidth());
        clip.setHeight(0);
        clip.setTranslateX(0);
        clip.setTranslateY(getHeight());

        node.setTranslateX(0);
        node.setTranslateY(-getHeight());
        node.setClip(clip);

        EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                node.setClip(null);
            }
        };

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);

        KeyValue kv1 = new KeyValue(clip.heightProperty(), getHeight());
        KeyValue kv2 = new KeyValue(clip.translateYProperty(), 0);
        KeyValue kv3 = new KeyValue(node.translateYProperty(), 0);
        KeyFrame kf = new KeyFrame(Duration.millis(1000), onFinished, kv1, kv2, kv3);
        timeline.getKeyFrames().add(kf);

        timeline.play();
    }

    public void slideOut(final Node node) {
        if(!getChildren().contains(node)) {
            return;
        }

        Rectangle clip = new Rectangle();
        clip.setWidth(getWidth());
        clip.setHeight(getHeight());
        clip.setTranslateX(0);
        clip.setTranslateY(0);

        node.setTranslateX(0);
        node.setTranslateY(0);
        node.setClip(clip);

        EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                node.setClip(null);
                getChildren().remove(node);
            }
        };

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);

        KeyValue kv1 = new KeyValue(clip.heightProperty(), 0);
        KeyValue kv2 = new KeyValue(clip.translateYProperty(), getHeight());
        KeyValue kv3 = new KeyValue(node.translateYProperty(), -getHeight());
        KeyFrame kf = new KeyFrame(Duration.millis(1000), onFinished, kv1, kv2, kv3);
        timeline.getKeyFrames().add(kf);

        timeline.play();
    }

}
