package com.example.helicopter2dgame;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Helicopter extends Group {
    private final Ellipse cockpit;
    private final Rectangle body;
    private final Rectangle tail;
    private final Rectangle rotorBlade1;
    private final Rectangle rotorBlade2;
    private final Rectangle rotorBlade3;

    public Helicopter(double width, double height) {
        double bodyWidth = 0.2 * width;
        double bodyHeight = height - width / 2;
        double bladeWidth = 0.75 * bodyWidth;
        double bladeHeight = 0.8 * bodyHeight;
        double tailWidth = bodyHeight / 2;

        body = new Rectangle(bodyWidth, bodyHeight);
        body.setFill(Color.BLUE);
        body.getTransforms().addAll(
                new Translate(-bodyWidth / 2, 0)
        );

        cockpit = new Ellipse(width / 2, height / 3.5);
        cockpit.setFill(Color.BLUE);

        tail = new Rectangle(tailWidth, bodyWidth);
        tail.setFill(Color.BLUE);
        tail.getTransforms().addAll(
                new Translate(-tailWidth / 2, 2 * bodyHeight / 3)
        );

        rotorBlade1 = new Rectangle(bladeWidth, bladeHeight);
        rotorBlade1.setFill(Color.RED);
        rotorBlade2 = new Rectangle(bladeWidth, bladeHeight);
        rotorBlade2.setFill(Color.RED);
        rotorBlade3 = new Rectangle(bladeWidth, bladeHeight);
        rotorBlade3.setFill(Color.RED);

        rotorBlade1.getTransforms().addAll(
                new Translate(-bladeWidth / 2, -bladeHeight)
        );

        rotorBlade2.getTransforms().addAll(
                new Rotate(60),
                new Translate(-bladeWidth / 2, 0)
        );

        rotorBlade3.getTransforms().addAll(
                new Rotate(300),
                new Translate(-bladeWidth / 2, 0)
        );

        getChildren().addAll(body, cockpit, tail, rotorBlade1, rotorBlade2, rotorBlade3);
    }
}
