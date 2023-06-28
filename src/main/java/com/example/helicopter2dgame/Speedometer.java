package com.example.helicopter2dgame;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

public class Speedometer extends Group {
    private Rectangle speedometer;
    private Circle speedIndicator;

    private double maxSpeed;
    private double speedIndicatorOffset;

    public Speedometer(double width, double height, double maxSpeed) {
        this.maxSpeed = maxSpeed;

        speedometer = new Rectangle(width, height);
        speedometer.setFill(Color.TRANSPARENT);
        speedometer.setStroke(Color.BLACK);
        speedometer.setStrokeWidth(1);

        double indicatorRadius = width / 2;
        speedIndicator = new Circle(indicatorRadius);
        speedIndicator.setFill(Color.RED);
        speedIndicator.getTransforms().addAll(
                new Translate(indicatorRadius, height / 2)
        );

        speedIndicatorOffset = (height - 2 * indicatorRadius) / 2;

        super.getChildren().addAll(speedometer, speedIndicator);
    }

    public void changeSpeed(double dSpeed, Helicopter helicopter) {
        /*double indicatorPosition = helicopter.getSpeed() + dSpeed / maxSpeed * speedIndicatorOffset;
        double indicatorPositionOffset = -indicatorPosition / speedometer.getHeight() * speedometer.getHeight();

        speedIndicator.getTransforms().addAll(
                new Translate(0, indicatorPositionOffset)
        );*/

        double limitedSpeed = Math.max(-maxSpeed, Math.min(maxSpeed, helicopter.getSpeed() + dSpeed));
        double indicatorPositionOffset = -limitedSpeed / maxSpeed * (speedometer.getHeight() / 2);

        speedIndicator.getTransforms().addAll(
                new Translate(0, indicatorPositionOffset)
        );
    }
}
