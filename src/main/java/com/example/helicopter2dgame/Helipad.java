package com.example.helicopter2dgame;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Helipad extends Group {
    private final static double STROKE_WIDTH = 3;

    public Helipad(double width, double height) {
        Rectangle helipad = new Rectangle(width, height);
        helipad.setFill(Color.DARKGRAY);

        Circle circle = new Circle(width / 2, height / 2, width / 2);
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.WHITE);
        circle.setStrokeWidth(STROKE_WIDTH);

        Rectangle diagonal1 = new Rectangle(STROKE_WIDTH, height * Math.sqrt(2));
        diagonal1.setFill(Color.WHITE);
        diagonal1.getTransforms().addAll(
                new Rotate(-45),
                new Translate(-STROKE_WIDTH / 2, 0)
        );

        Rectangle diagonal2 = new Rectangle(STROKE_WIDTH, height * Math.sqrt(2));
        diagonal2.setFill(Color.WHITE);
        diagonal2.getTransforms().addAll(
                new Translate(-STROKE_WIDTH / 2, 0),
                new Translate(width, 0),
                new Rotate(45)
        );

        super.getChildren().addAll(helipad, circle, diagonal1, diagonal2);
    }
}
