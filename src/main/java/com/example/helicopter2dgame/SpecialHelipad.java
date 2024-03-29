package com.example.helicopter2dgame;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

public class SpecialHelipad extends Helipad {
    private final Group plus;
    private boolean hasPlus;

    public SpecialHelipad(double width, double height) {
        super(width, height);

        hasPlus = true;

        Rectangle plus1 = new Rectangle(STROKE_WIDTH, height / 4);
        plus1.setFill(Color.GREEN);
        plus1.getTransforms().addAll(
                new Translate(width / 2 - STROKE_WIDTH / 2, 3 * height / 8)
        );

        Rectangle plus2 = new Rectangle(width / 4, STROKE_WIDTH);
        plus2.setFill(Color.GREEN);
        plus2.getTransforms().addAll(
                new Translate(3 * width / 8, height / 2 - STROKE_WIDTH / 2)
        );

        plus = new Group();
        plus.getChildren().addAll(plus1, plus2);

        super.getChildren().addAll(plus);
    }

    public void removePlus() {
        super.getChildren().remove(plus);
    }

    public boolean getHasPlus() {
        return hasPlus;
    }

    public void setHasPlus(boolean hasPlus) {
        this.hasPlus = hasPlus;
    }
}
