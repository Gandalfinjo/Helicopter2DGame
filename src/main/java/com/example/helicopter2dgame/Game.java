package com.example.helicopter2dgame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.util.Objects;

public class Game extends Application {
    private static final double WINDOW_WIDTH = 700;
    private static final double WINDOW_HEIGHT = 700;

    private static final double HELICOPTER_WIDTH = 0.03 * WINDOW_WIDTH;
    private static final double HELICOPTER_HEIGHT = 0.07 * WINDOW_HEIGHT;
    private static final double HELICOPTER_SPEED_STEP = 5;
    private static final double HELICOPTER_DIRECTION_STEP = 5;
    private static final double HELICOPTER_DAMP = 0.995;
    private static final double HELICOPTER_MAX_SPEED = 200;

    private static final double HELIPAD_WIDTH = 0.1 * WINDOW_WIDTH;
    private static final double HELIPAD_HEIGHT = 0.1 * WINDOW_HEIGHT;

    private static final double SPEEDOMETER_WIDTH = WINDOW_WIDTH / 75;
    private static final double SPEEDOMETER_HEIGHT = 4 * WINDOW_HEIGHT / 5;

    private boolean isRotorTimeline = false;
    private double fuelLevel = 1.0;

    @Override
    public void start(Stage stage) {
        Group root = new Group();

        Package[] packages = Package.generatePackages(5, WINDOW_WIDTH, WINDOW_HEIGHT);

        for (Package aPackage : packages) {
            aPackage.getTransforms().add(new Translate(-WINDOW_WIDTH / 2, -WINDOW_HEIGHT / 2));
        }

        Helicopter helicopter = new Helicopter(HELICOPTER_WIDTH, HELICOPTER_HEIGHT);
        Helipad helipad = new Helipad(HELIPAD_WIDTH, HELIPAD_HEIGHT);
        helipad.getTransforms().addAll(
                new Translate(-HELIPAD_WIDTH / 2, -HELIPAD_HEIGHT / 2)
        );

        Speedometer speedometer = new Speedometer(SPEEDOMETER_WIDTH, SPEEDOMETER_HEIGHT, HELICOPTER_MAX_SPEED);
        speedometer.getTransforms().addAll(
                new Translate(7 * WINDOW_WIDTH / 16, -SPEEDOMETER_HEIGHT / 2)
        );


        Label timerLabel = new Label();
        timerLabel.setTextFill(Color.BLACK);
        timerLabel.setStyle("-fx-font-size: 20px");
        timerLabel.getTransforms().addAll(
                new Translate(-2 * WINDOW_WIDTH / 5, 2 * WINDOW_HEIGHT / 5)
        );

        FuelIndicator fuelIndicator = new FuelIndicator();
        fuelIndicator.getTransforms().addAll(
                new Translate(-2 * WINDOW_WIDTH / 5, -2 * WINDOW_HEIGHT / 5)
        );

        root.getChildren().addAll(packages);
        root.getChildren().addAll(helipad, helicopter);
        root.getChildren().addAll(timerLabel);
        root.getChildren().addAll(fuelIndicator);
        root.getChildren().addAll(speedometer);
        root.getTransforms().addAll(
                new Translate(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2)
        );

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().equals(KeyCode.UP) || event.getCode().equals(KeyCode.W)) {
                helicopter.changeSpeed(HELICOPTER_SPEED_STEP);
                speedometer.changeSpeed(HELICOPTER_SPEED_STEP, helicopter);
            }
            else if (event.getCode().equals(KeyCode.DOWN) || event.getCode().equals(KeyCode.S)) {
                helicopter.changeSpeed(-HELICOPTER_SPEED_STEP);
                speedometer.changeSpeed(-HELICOPTER_SPEED_STEP, helicopter);
            }

            if (event.getCode().equals(KeyCode.LEFT) || event.getCode().equals(KeyCode.A)) {
                helicopter.rotate(-HELICOPTER_DIRECTION_STEP, 0, WINDOW_WIDTH, 0, WINDOW_HEIGHT);
            }
            else if (event.getCode().equals(KeyCode.RIGHT) || event.getCode().equals(KeyCode.D)) {
                helicopter.rotate(HELICOPTER_DIRECTION_STEP, 0, WINDOW_WIDTH, 0, WINDOW_HEIGHT);
            }

            if (event.getCode().equals(KeyCode.SPACE)) {
                if (!isRotorTimeline) helicopter.playRotorTimeline();
                else helicopter.pauseRotorTimeline();
                isRotorTimeline = !isRotorTimeline;
            }
        });

        MyTimer.IUpdatable helicopterWrapper = ds -> {
            helicopter.update(ds, HELICOPTER_DAMP, 0, WINDOW_WIDTH, 0, WINDOW_HEIGHT);

            for (int i = 0; i < packages.length; i++) {
                if (packages[i] != null && packages[i].handleCollision(helicopter.getBoundsInParent())) {
                    root.getChildren().remove(packages[i]);
                    packages[i] = null;
                }
            }

            double speed = Math.abs(helicopter.getSpeed());
            double fuelConsumptionRate = 0.00001;
            double fuelConsumed = speed * fuelConsumptionRate;
            fuelLevel -= fuelConsumed;

            fuelIndicator.setFuelLevel(fuelLevel);
        };

        MyTimer myTimer = new MyTimer(helicopterWrapper);
        myTimer.start();

        Image image = new Image(Objects.requireNonNull(Game.class.getResourceAsStream("grass.jpg")));
        ImagePattern imagePattern = new ImagePattern(image);

        scene.setFill(imagePattern);
        stage.setTitle("Helicopter2DGame");
        stage.setScene(scene);
        stage.show();

        long startTime = System.currentTimeMillis();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - startTime;

                long minutes = (elapsedTime / 1000) / 60;
                long seconds = (elapsedTime / 1000) % 60;

                String formattedTime = String.format("%02d:%02d", minutes, seconds);

                timerLabel.setText(formattedTime);
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch();
    }
}