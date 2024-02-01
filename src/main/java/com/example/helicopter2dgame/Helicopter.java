package com.example.helicopter2dgame;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.*;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.util.List;

public class Helicopter extends Group {
    private Point2D direction;
    private Rotate bodyRotation;
    private Rotate rotorRotation;
    private Translate position;
    private Scale scale;
    private double speed;
    private boolean canStart;

    private final Ellipse cockpit;
    private final Rectangle body;
    private final Rectangle tail;
    private final Group upperBody;
    private final Rectangle rotorBlade1;
    private final Rectangle rotorBlade2;
    private final Rectangle rotorBlade3;
    private final Group rotorBlades;
    private final Timeline rotorTimeline;
    private final Timeline scaleTimeline;
    private final Timeline reverseTimeline;
    private final Timeline underWaterTimeline;

    public boolean isCanStart() {
        return canStart;
    }

    public Helicopter(double width, double height, int type) {
        direction = new Point2D(0, -1);
        bodyRotation = new Rotate(0);
        rotorRotation = new Rotate(0);
        scale = new Scale(0.7, 0.7);
        position = new Translate();
        canStart = false;

        super.getTransforms().add(position);

        double bodyWidth = 0.2 * width;
        double bodyHeight = height - width / 2;
        double bladeWidth = 0.75 * bodyWidth;
        double bladeHeight = 0.8 * bodyHeight;
        double tailWidth = bodyHeight / 2;

        if (type == 0) {
            body = new Rectangle(bodyWidth, bodyHeight);
            body.setFill(Color.BLUE);
            body.getTransforms().addAll(
                    new Translate(-bodyWidth / 2, 0)
            );

            Stop[] stops = {
                    new Stop(0, Color.GOLD),
                    new Stop(1, Color.BLUE)
            };

            LinearGradient lg = new LinearGradient(0.5, 0, 0.5, 1, true, CycleMethod.NO_CYCLE, stops);

            cockpit = new Ellipse(width / 2, height / 3.5);
            cockpit.setFill(lg);

            tail = new Rectangle(tailWidth, bodyWidth);
            tail.setFill(Color.BLUE);
            tail.getTransforms().addAll(
                    new Translate(-tailWidth / 2, 2 * bodyHeight / 3)
            );

            upperBody = new Group();
            upperBody.getChildren().addAll(body, cockpit, tail);
            upperBody.getTransforms().add(bodyRotation);

            rotorBlade1 = new Rectangle(bladeWidth, bladeHeight);
            rotorBlade1.setFill(Color.BLACK);
            rotorBlade2 = new Rectangle(bladeWidth, bladeHeight);
            rotorBlade2.setFill(Color.BLACK);
            rotorBlade3 = new Rectangle(bladeWidth, bladeHeight);
            rotorBlade3.setFill(Color.BLACK);

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
        }
        else if (type == 1) {
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
        }
        else {
            body = new Rectangle(bodyWidth, bodyHeight);
            body.setFill(Color.BLACK);
            body.getTransforms().addAll(
                    new Translate(-bodyWidth / 2, 0)
            );

            Stop[] stops = {
                    new Stop(0, Color.BLACK),
                    new Stop(1, Color.ORANGE)
            };

            RadialGradient rg = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, stops);

            cockpit = new Ellipse(width / 2, height / 3.5);
            cockpit.setFill(rg);

            tail = new Rectangle(tailWidth, bodyWidth);
            tail.setFill(Color.ORANGE);
            tail.getTransforms().addAll(
                    new Translate(-tailWidth / 2, 2 * bodyHeight / 3)
            );

            upperBody = new Group();
            upperBody.getChildren().addAll(body, cockpit, tail);
            upperBody.getTransforms().add(bodyRotation);

            rotorBlade1 = new Rectangle(bladeWidth, bladeHeight);
            rotorBlade1.setFill(Color.ORANGE);
            rotorBlade2 = new Rectangle(bladeWidth, bladeHeight);
            rotorBlade2.setFill(Color.ORANGE);
            rotorBlade3 = new Rectangle(bladeWidth, bladeHeight);
            rotorBlade3.setFill(Color.ORANGE);

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
        }


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

        super.getTransforms().addAll(scale);

        scaleTimeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(scale.xProperty(), 0.7),
                        new KeyValue(scale.yProperty(), 0.7)
                ),
                new KeyFrame(
                        Duration.seconds(2),
                        new KeyValue(scale.xProperty(), 1),
                        new KeyValue(scale.yProperty(), 1)
                )
        );
        scaleTimeline.setOnFinished(e -> canStart = true);

        reverseTimeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(scale.xProperty(), 1),
                        new KeyValue(scale.yProperty(), 1)
                ),
                new KeyFrame(
                        Duration.seconds(2),
                        new KeyValue(scale.xProperty(), 0.7),
                        new KeyValue(scale.yProperty(), 0.7)
                )
        );
        reverseTimeline.setOnFinished(e -> pauseRotorTimeline());

        underWaterTimeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(scale.xProperty(), 1),
                        new KeyValue(scale.yProperty(), 1)
                ),
                new KeyFrame(
                        Duration.seconds(2),
                        new KeyValue(scale.xProperty(), 0),
                        new KeyValue(scale.yProperty(), 0)
                )
        );

        super.getChildren().addAll(upperBody, rotorBlades);
    }

    private boolean isWallHit(double left, double right, double up, double down) {
        Bounds cockpitBounds = cockpit.localToScene(cockpit.getBoundsInLocal());
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

        double rotorMinX = cockpitBounds.getCenterX() - rotorBlade1.getHeight();
        double rotorMaxX = cockpitBounds.getCenterX() + rotorBlade1.getHeight();
        double rotorMinY = cockpitBounds.getCenterY() - rotorBlade1.getHeight();
        double rotorMaxY = cockpitBounds.getCenterY() + rotorBlade1.getHeight();

        boolean rotorWallHit = rotorMinX <= left || rotorMaxX >= right || rotorMinY <= up || rotorMaxY >= down;

        return cockpitWallHit || bodyWallHit || rotorWallHit;
    }

    private boolean isObstacleHit(WoodRectangle obstacle) {
        Bounds helicopterBounds = getBoundsInParent();
        Bounds obstacleBounds = obstacle.getBoundsInParent();

        return helicopterBounds.intersects(obstacleBounds);
    }

    public boolean isInsideWater(Water water) {
        Bounds helicopterBounds = getBoundsInParent();
        Bounds waterBounds = water.getBoundsInParent();

        double intersectionWidth = Math.min(helicopterBounds.getMaxX(), waterBounds.getMaxX()) - Math.max(helicopterBounds.getMinX(), waterBounds.getMinX());
        double intersectionHeight = Math.min(helicopterBounds.getMaxY(), waterBounds.getMaxY()) - Math.max(helicopterBounds.getMinY(), waterBounds.getMinY());

        double intersectionArea = Math.max(0, intersectionWidth) * Math.max(0, intersectionHeight);
        double helicopterArea = helicopterBounds.getWidth() * helicopterBounds.getHeight();

        double overlapPercentage = intersectionArea / helicopterArea;

        return overlapPercentage > 0.5;
    }

    public boolean isOnSpecialHelipad(SpecialHelipad specialHelipad) {
        Bounds helicopterBounds = getBoundsInParent();
        Bounds helipadBounds = specialHelipad.getBoundsInParent();

        double intersectionWidth = Math.min(helicopterBounds.getMaxX(), helipadBounds.getMaxX()) - Math.max(helicopterBounds.getMinX(), helipadBounds.getMinX());
        double intersectionHeight = Math.min(helicopterBounds.getMaxY(), helipadBounds.getMaxY()) - Math.max(helicopterBounds.getMinY(), helipadBounds.getMinY());

        double intersectionArea = Math.max(0, intersectionWidth) * Math.max(0, intersectionHeight);
        double helicopterArea = helicopterBounds.getWidth() * helicopterBounds.getHeight();

        double overlapPercentage = intersectionArea / helicopterArea;

        return overlapPercentage > 0.5;
    }

    public void rotate(double dAngle, double left, double right, double up, double down, List<WoodRectangle> obstacles) {
        double oldAngle = bodyRotation.getAngle();
        double newAngle = oldAngle + dAngle;
        bodyRotation.setAngle(newAngle);

        if (isWallHit(left, right, up, down) || obstacles.stream().anyMatch(this::isObstacleHit)) {
            bodyRotation.setAngle(oldAngle);
        } else {
            double magnitude = direction.magnitude();
            direction = new Point2D(
                    magnitude * Math.sin(Math.toRadians(newAngle)),
                    -magnitude * Math.cos(Math.toRadians(newAngle))
            );
        }
    }

    public void playRotorTimeline() {
        rotorTimeline.play();
    }

    public void pauseRotorTimeline() {
        rotorTimeline.pause();
    }

    public void playScaleTimeline() {
        scaleTimeline.play();
    }

    public void reverseScaleTimeline() {
        canStart = false;
        speed = 0;
        reverseTimeline.play();
    }

    public void underWaterTimeline() {
        canStart = false;
        speed = 0;
        underWaterTimeline.play();
    }

    public boolean isOnTheGround() {
        return scale.getX() == 0.7;
    }

    public void changeSpeed(double dSpeed) {
        speed += dSpeed;
    }

    public void setSpeed(double dSpeed) { speed = dSpeed; }

    public double getSpeed() {
        return speed;
    }

    public void update(double ds, double speedDamp, double left, double right, double up, double down, List<WoodRectangle> obstacles) {
        double oldX = position.getX();
        double oldY = position.getY();

        double newX = oldX + ds * speed * direction.getX();
        double newY = oldY + ds * speed * direction.getY();

        position.setX(newX);
        position.setY(newY);

        if (isWallHit(left, right, up, down) || obstacles.stream().anyMatch(this::isObstacleHit)) {
            speed = 0;
            position.setX(oldX);
            position.setY(oldY);
        }
        else speed *= speedDamp;
    }
}
