package com.example.helicopter2dgame;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

public class SpecialHelipad extends Helipad {
    public SpecialHelipad(double width, double height) {
        super(width, height);

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

        Group plus = new Group();
        plus.getChildren().addAll(plus1, plus2);

        super.getChildren().addAll(plus);
    }

//    public boolean isHelicopterInside(Helicopter helicopter) {
//        double helipadCenterX = getTranslateX() + width / 2;
//        double helipadCenterY = getTranslateY() + height / 2;
//
//        double helicopterCenterX = helicopter.getTranslateX() + helicopter.getWidth() / 2;
//        double helicopterCenterY = helicopter.getTranslateY() + helicopter.getHeight() / 2;
//
//        double minX = helipadCenterX - width / 2;
//        double maxX = helipadCenterX + width / 2;
//        double minY = helipadCenterY - height / 2;
//        double maxY = helipadCenterY + height / 2;
//
//        return helicopterCenterX >= minX && helicopterCenterX <= maxX &&
//                helicopterCenterY >= minY && helicopterCenterY <= maxY;
//    }
}
