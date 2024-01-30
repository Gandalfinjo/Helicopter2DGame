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
    private double currentSpeed;
    private double speedIndicatorOffset;

    public Speedometer(double width, double height, double maxSpeed) {
        this.currentSpeed = 0.0;
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

        currentSpeed = dSpeed;
        if (currentSpeed > maxSpeed) currentSpeed = maxSpeed;
        else if (currentSpeed < -maxSpeed) currentSpeed = -maxSpeed;

        double normalizedSpeed = (currentSpeed + maxSpeed) / (maxSpeed + maxSpeed);
        double yPos = (1 - normalizedSpeed) * (speedometer.getHeight()) - speedometer.getHeight() / 2;
        if (yPos == -speedometer.getHeight() / 2) yPos += speedIndicator.getRadius();
        else if (yPos == speedometer.getHeight() / 2) yPos -= speedIndicator.getRadius();
        speedIndicator.setTranslateY(yPos);

//        double normalizedSpeed = currentSpeed / maxSpeed;
//        double indicatorY = (1 - normalizedSpeed) * (speedometer.getHeight() - speedIndicator.getRadius() * 2);
//        speedIndicator.setCenterY(indicatorY + speedIndicator.getRadius());

        /*double limitedSpeed = Math.max(-maxSpeed, Math.min(maxSpeed, helicopter.getSpeed() + dSpeed));
        double indicatorPositionOffset = -limitedSpeed / maxSpeed * (speedometer.getHeight() / 2);

        speedIndicator.getTransforms().addAll(
                new Translate(0, indicatorPositionOffset)
        );*/
    }
}
