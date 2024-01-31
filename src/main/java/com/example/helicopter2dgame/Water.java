package com.example.helicopter2dgame;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

public class Water extends Rectangle {
    public Water(double width, double height) {
        super(width, height);
        Image waterImage = new Image(Objects.requireNonNull(Game.class.getResourceAsStream("water.jpg")));
        ImagePattern imagePattern = new ImagePattern(waterImage);
        setFill(imagePattern);
    }
}
