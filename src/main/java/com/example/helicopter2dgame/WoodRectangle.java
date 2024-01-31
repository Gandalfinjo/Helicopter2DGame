package com.example.helicopter2dgame;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

public class WoodRectangle extends Rectangle {
    public WoodRectangle(double width, double height) {
        super(width, height);
        Image woodImage = new Image(Objects.requireNonNull(Game.class.getResourceAsStream("wood.jpg")));
        ImagePattern imagePattern = new ImagePattern(woodImage);
        setFill(imagePattern);
    }

}
