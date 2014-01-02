package org.aldeon.gui2.components;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class ColorizedImageView extends ImageView {

    private final ObjectProperty<Color> colorizeProperty = new SimpleObjectProperty<>();

    public ColorizedImageView() {
        super();
        registerListener();
    }

    public ColorizedImageView(String string) {
        super(string);
        registerListener();
    }

    public ColorizedImageView(Image image) {
        super(image);
        registerListener();
    }

    private void registerListener() {
        colorizeProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observableValue, Color oldColorize, Color newColorize) {
                createEffect(newColorize);
            }
        });
    }

    private void createEffect(Color color) {
        setClip(new ImageView(getImage()));

        ColorAdjust monochrome = new ColorAdjust();
        monochrome.setSaturation(-1.0);

        Blend blend = new Blend(BlendMode.MULTIPLY, monochrome,
                new ColorInput(0, 0, getImage().getWidth(), getImage().getHeight(), color));

        setEffect(blend);
        //setCache(true);
        //setCacheHint(CacheHint.SPEED);
    }

    public ObjectProperty<Color> colorizeProperty() {
        return colorizeProperty;
    }

    public Color getColorize() {
        return colorizeProperty().get();
    }

    public void setColorize(Color colorize) {
        colorizeProperty().set(colorize);
    }
}
