package com.example.helicopter2dgame;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

public class Package extends Rectangle {
    public Package(double size, Translate position) {
        super(size, size, Color.DARKRED);
        super.getTransforms().addAll(position);
    }

    public boolean handleCollision(Bounds helicopterBounds) {
        return super.getBoundsInParent().intersects(helicopterBounds);
    }

}
