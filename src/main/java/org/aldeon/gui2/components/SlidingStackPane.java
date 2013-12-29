package org.aldeon.gui2.components;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.aldeon.gui2.various.Direction;

public class SlidingStackPane extends StackPane {

    public static final int DEFAULT_TRANSITION_TIME = 500;

    private final IntegerProperty transitionTime = new SimpleIntegerProperty(DEFAULT_TRANSITION_TIME);

    public void slideIn(final Node node, Direction dir) {
        if(getChildren().contains(node)) {
            return;
        }

        getChildren().add(node);

        Rectangle clip = new Rectangle();
        clip.setWidth(dir.isVertical() ? getWidth() : 0);
        clip.setHeight(dir.isHorizontal() ? getHeight() : 0);
        clip.setTranslateX(dir == Direction.LEFT ? getWidth() : 0);
        clip.setTranslateY(dir == Direction.TOP ? getHeight() : 0);

        node.setTranslateX(dir.getX() * getWidth());
        node.setTranslateY(dir.getY() * getHeight());
        node.setClip(clip);

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);

        EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                node.setClip(null);
            }
        };

        KeyValue kv1 = new KeyValue(clip.heightProperty(), getHeight());
        KeyValue kv2 = new KeyValue(clip.widthProperty(), getWidth());
        KeyValue kv3 = new KeyValue(clip.translateXProperty(), 0);
        KeyValue kv4 = new KeyValue(clip.translateYProperty(), 0);
        KeyValue kv5 = new KeyValue(node.translateXProperty(), 0);
        KeyValue kv6 = new KeyValue(node.translateYProperty(), 0);
        KeyFrame kf = new KeyFrame(Duration.millis(getTransitionTime()), onFinished, kv1, kv2, kv3, kv4, kv5, kv6);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    public void slideOut(final Node node, Direction dir) {
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

        KeyValue kv1 = new KeyValue(clip.widthProperty(), dir.isHorizontal() ? 0 : getWidth());
        KeyValue kv2 = new KeyValue(clip.heightProperty(), dir.isVertical() ? 0 : getHeight());
        KeyValue kv3 = new KeyValue(clip.translateXProperty(), dir == Direction.LEFT ? getWidth() : 0);
        KeyValue kv4 = new KeyValue(clip.translateYProperty(), dir == Direction.TOP ? getHeight() : 0);
        KeyValue kv5 = new KeyValue(node.translateXProperty(), dir.getX() * getWidth());
        KeyValue kv6 = new KeyValue(node.translateYProperty(), dir.getY() * getHeight());
        KeyFrame kf = new KeyFrame(Duration.millis(getTransitionTime()), onFinished, kv1, kv2, kv3, kv4, kv5, kv6);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    public IntegerProperty transitionTimeProperty() {
        return transitionTime;
    }

    public int getTransitionTime() {
        return transitionTimeProperty().get();
    }

    public void setTransitionTime(int time) {
        transitionTimeProperty().set(time);
    }
}
