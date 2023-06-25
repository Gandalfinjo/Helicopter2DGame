package com.example.helicopter2dgame;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class Helicopter extends Group {
    private Point2D direction;
    private Rotate bodyRotation;
    private Rotate rotorRotation;
    private Translate position;
    private double speed;

    private final Ellipse cockpit;
    private final Rectangle body;
    private final Rectangle tail;
    private final Group upperBody;
    private final Rectangle rotorBlade1;
    private final Rectangle rotorBlade2;
    private final Rectangle rotorBlade3;
    private final Group rotorBlades;
    private final Timeline rotorTimeline;

    public Helicopter(double width, double height) {
        direction = new Point2D(0, -1);
        bodyRotation = new Rotate(0);
        rotorRotation = new Rotate(0);
        position = new Translate();

        super.getTransforms().add(position);

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

        upperBody = new Group();
        upperBody.getChildren().addAll(body, cockpit, tail);
        upperBody.getTransforms().add(bodyRotation);

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

        rotorBlades = new Group();
        rotorBlades.getChildren().addAll(rotorBlade1, rotorBlade2, rotorBlade3);
        rotorBlades.getTransforms().add(rotorRotation);

        rotorTimeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(rotorRotation.angleProperty(), 0)
                ),
                new KeyFrame(
                        Duration.seconds(1),
                        new KeyValue(rotorRotation.angleProperty(), 360)
                )
        );
        rotorTimeline.setCycleCount(Animation.INDEFINITE);
        rotorTimeline.play();

        //super.getChildren().addAll(body, cockpit, tail, rotorBlades);
        super.getChildren().addAll(upperBody, rotorBlades);
    }

    private boolean isWallHit(double left, double right, double up, double down) {
        Bounds cockpitBounds = cockpit.localToScene(cockpit.getBoundsInLocal());
        Bounds rotorBounds = rotorBlades.localToScene(rotorBlades.getBoundsInLocal());
        Bounds bodyBounds = body.localToScene(body.getBoundsInLocal());

        double cockpitMinX = cockpitBounds.getCenterX() - cockpit.getRadiusX();
        double cockpitMaxX = cockpitBounds.getCenterX() + cockpit.getRadiusX();
        double cockpitMinY = cockpitBounds.getCenterY() - cockpit.getRadiusY();
        double cockpitMaxY = cockpitBounds.getCenterY() + cockpit.getRadiusY();

        boolean cockpitWallHit = cockpitMinX <= left || cockpitMaxX >= right || cockpitMinY <= up || cockpitMaxY >= down;

        double bodyMinX = bodyBounds.getMinX();
        double bodyMaxX = bodyBounds.getMaxX();
        double bodyMinY = bodyBounds.getMinY();
        double bodyMaxY = bodyBounds.getMaxY();

        boolean bodyWallHit = bodyMinX <= left || bodyMaxX >= right || bodyMinY <= up || bodyMaxY >= down;

        return cockpitWallHit || bodyWallHit;
    }

    public void rotate(double dAngle, double left, double right, double up, double down) {
        double oldAngle = bodyRotation.getAngle();
        double newAngle = oldAngle + dAngle;
        bodyRotation.setAngle(newAngle);

        if (isWallHit(left, right, up, down)) {
            bodyRotation.setAngle(oldAngle);
        } else {
            double magnitude = direction.magnitude();
            direction = new Point2D(
                    magnitude * Math.sin(Math.toRadians(newAngle)),
                    -magnitude * Math.cos(Math.toRadians(newAngle))
            );
        }
    }

    public void changeSpeed(double dSpeed) {
        speed += dSpeed;
    }

    public void update(double ds, double speedDamp, double left, double right, double up, double down) {
        double oldX = position.getX();
        double oldY = position.getY();

        double newX = oldX + ds * speed * direction.getX();
        double newY = oldY + ds * speed * direction.getY();

        position.setX(newX);
        position.setY(newY);

        if (isWallHit(left, right, up, down)) {
            speed = 0;
            position.setX(oldX);
            position.setY(oldY);
        }
        else speed *= speedDamp;
    }
}
