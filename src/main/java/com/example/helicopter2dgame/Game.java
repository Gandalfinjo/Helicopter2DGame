package com.example.helicopter2dgame;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Game extends Application {
    private static final double WINDOW_WIDTH = 700;
    private static final double WINDOW_HEIGHT = 700;

    private static final double HELICOPTER_WIDTH = 0.03 * WINDOW_WIDTH;
    private static final double HELICOPTER_HEIGHT = 0.07 * WINDOW_HEIGHT;
    private static final double SLOW_HELICOPTER_WIDTH = 1.3 * HELICOPTER_WIDTH;
    private static final double SLOW_HELICOPTER_HEIGHT = 1.3 * HELICOPTER_HEIGHT;
    private static final double FAST_HELICOPTER_WIDTH = 0.7 * HELICOPTER_WIDTH;
    private static final double FAST_HELICOPTER_HEIGHT = 0.7 * HELICOPTER_HEIGHT;
    private static final double HELICOPTER_SPEED_STEP = 5;
    private static final double HELICOPTER_DIRECTION_STEP = 5;
    private static final double HELICOPTER_DAMP = 0.995;
    private static final double HELICOPTER_MAX_SPEED = 200;
    private static final double SLOW_HELICOPTER_MAX_SPEED = 100;
    private static final double FAST_HELICOPTER_MAX_SPEED = 600;

    private static final int SLOW_HELICOPTER = 0;
    private static final int NORMAL_HELICOPTER = 1;
    private static final int FAST_HELICOPTER = 2;

    private static final double HELIPAD_WIDTH = 0.1 * WINDOW_WIDTH;
    private static final double HELIPAD_HEIGHT = 0.1 * WINDOW_HEIGHT;

    private static final double PACKAGE_SIZE = 20;

    private static final double OBSTACLE_WIDTH = WINDOW_WIDTH / 70;
    private static final double OBSTACLE_HEIGHT = WINDOW_HEIGHT / 4;

    private static final double WATER_WIDTH = 0.15 * WINDOW_WIDTH;
    private static final double WATER_HEIGHT = 0.15 * WINDOW_HEIGHT;

    private static final double SPEEDOMETER_WIDTH = WINDOW_WIDTH / 75;
    private static final double SPEEDOMETER_HEIGHT = 4 * WINDOW_HEIGHT / 5;

    private static final double HEIGHT_INDICATOR_WIDTH = WINDOW_WIDTH / 200;
    private static final double HEIGHT_INDICATOR_HEIGHT = 4 * WINDOW_HEIGHT / 5;

    private Timeline heightTimeline;
    private Timeline reverseHeightTimeline;

    private static final double MILLIS_TO_SECONDS = 1000.0;
    private static final double FUEL_CONSUMPTION_RATE = 0.000005;

    private boolean isRotorTimeline = false;
    private double fuelLevel = 1.0;

    private Helicopter helicopter;
    private int type = 1;

    private Timeline timer;
    private long lastUpdateTime = System.currentTimeMillis();

    private List<WoodRectangle> obstacles;

    private boolean sceneMovingMode = false;

    private Stage endGameStage;
    private Stage helicopterChoiceStage;

    @Override
    public void start(Stage stage) {
        showHelicopterChoiceModal();

        Group root = new Group();
        Translate package0Position = new Translate (
                -PACKAGE_SIZE / 2 + WINDOW_WIDTH / 3,
                -PACKAGE_SIZE / 2 - WINDOW_HEIGHT / 3
        );
        Translate package1Position = new Translate (
                -PACKAGE_SIZE / 2 - WINDOW_WIDTH / 3,
                -PACKAGE_SIZE / 2 - WINDOW_HEIGHT / 3
        );
        Translate package2Position = new Translate (
                -PACKAGE_SIZE / 2 + WINDOW_WIDTH / 3,
                PACKAGE_SIZE / 2 + WINDOW_HEIGHT / 3
        );
        Translate package3Position = new Translate (
                -PACKAGE_SIZE / 2 - WINDOW_WIDTH / 3,
                PACKAGE_SIZE / 2 + WINDOW_HEIGHT / 3
        );
        Package[] packages = {
                new Package (PACKAGE_SIZE, package0Position),
                new Package (PACKAGE_SIZE, package1Position),
                new Package (PACKAGE_SIZE, package2Position),
                new Package (PACKAGE_SIZE, package3Position)
        };

        switch (type) {
            case 0: {
                helicopter = new Helicopter(SLOW_HELICOPTER_WIDTH, SLOW_HELICOPTER_HEIGHT, SLOW_HELICOPTER);
                break;
            }
            case 1: {
                helicopter = new Helicopter(HELICOPTER_WIDTH, HELICOPTER_HEIGHT, NORMAL_HELICOPTER);
                break;
            }
            case 2: {
                helicopter = new Helicopter(FAST_HELICOPTER_WIDTH, FAST_HELICOPTER_HEIGHT, FAST_HELICOPTER);
                break;
            }
        }

        //Helicopter helicopter = new Helicopter(HELICOPTER_WIDTH, HELICOPTER_HEIGHT, NORMAL_HELICOPTER);
        Helipad helipad = new Helipad(HELIPAD_WIDTH, HELIPAD_HEIGHT);
        helipad.getTransforms().addAll(
                new Translate(-HELIPAD_WIDTH / 2, -HELIPAD_HEIGHT / 2)
        );

        SpecialHelipad specialHelipad1 = new SpecialHelipad(HELIPAD_WIDTH, HELIPAD_HEIGHT);
        specialHelipad1.getTransforms().addAll(
                new Translate(-HELIPAD_WIDTH / 2, -3 * WINDOW_HEIGHT / 7)
        );

        SpecialHelipad specialHelipad2 = new SpecialHelipad(HELIPAD_WIDTH, HELIPAD_HEIGHT);
        specialHelipad2.getTransforms().addAll(
                new Translate(-HELIPAD_WIDTH / 2, 3 * WINDOW_HEIGHT / 7 - HELIPAD_HEIGHT)
        );

        Speedometer speedometer = new Speedometer(SPEEDOMETER_WIDTH, SPEEDOMETER_HEIGHT, HELICOPTER_MAX_SPEED);
        speedometer.getTransforms().addAll(
                new Translate(7 * WINDOW_WIDTH / 16, -SPEEDOMETER_HEIGHT / 2)
        );

        Rectangle heightIndicator = new Rectangle(HEIGHT_INDICATOR_WIDTH, 0, Color.CADETBLUE);
        heightIndicator.getTransforms().addAll(
                new Translate(-7 * WINDOW_WIDTH / 16, SPEEDOMETER_HEIGHT / 2),
                new Rotate(180)
        );

        heightTimeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(heightIndicator.heightProperty(), 0)
                ),
                new KeyFrame(
                        Duration.seconds(2),
                        new KeyValue(heightIndicator.heightProperty(), HEIGHT_INDICATOR_HEIGHT)
                )
        );

        reverseHeightTimeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(heightIndicator.heightProperty(), HEIGHT_INDICATOR_HEIGHT)
                ),
                new KeyFrame(
                        Duration.seconds(2),
                        new KeyValue(heightIndicator.heightProperty(), 0)
                )
        );

        WoodRectangle obstacle1 = new WoodRectangle(OBSTACLE_WIDTH, OBSTACLE_HEIGHT);
        obstacle1.getTransforms().addAll(
                new Translate(-WINDOW_WIDTH / 4 - OBSTACLE_WIDTH / 2, -OBSTACLE_HEIGHT / 2)
        );
        WoodRectangle obstacle2 = new WoodRectangle(OBSTACLE_WIDTH, OBSTACLE_HEIGHT);
        obstacle2.getTransforms().addAll(
                new Translate(OBSTACLE_HEIGHT / 2, -WINDOW_WIDTH / 4),
                new Rotate(90),
                new Translate(-OBSTACLE_WIDTH / 2, 0)
        );
        WoodRectangle obstacle3 = new WoodRectangle(OBSTACLE_WIDTH, OBSTACLE_HEIGHT);
        obstacle3.getTransforms().addAll(
                new Translate(-OBSTACLE_HEIGHT / 2, WINDOW_WIDTH / 4),
                new Rotate(-90),
                new Translate(-OBSTACLE_WIDTH / 2, 0)
        );
        WoodRectangle obstacle4 = new WoodRectangle(OBSTACLE_WIDTH, OBSTACLE_HEIGHT);
        obstacle4.getTransforms().addAll(
                new Translate(WINDOW_WIDTH / 4 - OBSTACLE_WIDTH / 2, -OBSTACLE_HEIGHT / 2)
        );

        obstacles = new ArrayList<>(4);

        obstacles.add(obstacle1);
        obstacles.add(obstacle2);
        obstacles.add(obstacle3);
        obstacles.add(obstacle4);

        Water water = new Water(WATER_WIDTH, WATER_HEIGHT);
        water.getTransforms().addAll(
                new Translate(- 3 * WINDOW_WIDTH / 7, -WATER_HEIGHT / 2)
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

        Label resultLabel = new Label();
        resultLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        root.getChildren().addAll(water);
        root.getChildren().addAll(packages);
        root.getChildren().addAll(helipad, specialHelipad1, specialHelipad2, helicopter);
        root.getChildren().addAll(timerLabel);
        root.getChildren().addAll(fuelIndicator);
        root.getChildren().addAll(speedometer);
        root.getChildren().addAll(heightIndicator);
        root.getChildren().addAll(obstacle1, obstacle2, obstacle3, obstacle4);
        root.getTransforms().addAll(
                new Translate(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2)
        );

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (helicopter.isCanStart()) {
                if (event.getCode().equals(KeyCode.DIGIT2)) {
                    sceneMovingMode = true;
                }
                else if (event.getCode().equals(KeyCode.DIGIT1)) {
                    sceneMovingMode = false;
                }

                if (!sceneMovingMode) {
                    if (event.getCode().equals(KeyCode.UP) || event.getCode().equals(KeyCode.W)) {
                        if (type == 0) {
                            helicopter.changeSpeed(HELICOPTER_SPEED_STEP);
                            if (helicopter.getSpeed() > SLOW_HELICOPTER_MAX_SPEED) helicopter.setSpeed(SLOW_HELICOPTER_MAX_SPEED);
                        }
                        else if (type == 1) {
                            helicopter.changeSpeed(HELICOPTER_SPEED_STEP);
                            if (helicopter.getSpeed() > HELICOPTER_MAX_SPEED) helicopter.setSpeed(HELICOPTER_MAX_SPEED);
                            //speedometer.changeSpeed(HELICOPTER_SPEED_STEP, helicopter);
                        }
                        else if (type == 2) {
                            helicopter.changeSpeed(HELICOPTER_SPEED_STEP);
                            if (helicopter.getSpeed() > FAST_HELICOPTER_MAX_SPEED) helicopter.setSpeed(FAST_HELICOPTER_MAX_SPEED);
                        }

                    }
                    else if (event.getCode().equals(KeyCode.DOWN) || event.getCode().equals(KeyCode.S)) {
                        if (type == 0) {
                            helicopter.changeSpeed(-HELICOPTER_SPEED_STEP);
                            if (helicopter.getSpeed() < -SLOW_HELICOPTER_MAX_SPEED) helicopter.setSpeed(-SLOW_HELICOPTER_MAX_SPEED);
                        }
                        else if (type == 1) {
                            helicopter.changeSpeed(-HELICOPTER_SPEED_STEP);
                            if (helicopter.getSpeed() < -HELICOPTER_MAX_SPEED) helicopter.setSpeed(-HELICOPTER_MAX_SPEED);
                            //speedometer.changeSpeed(-HELICOPTER_SPEED_STEP, helicopter);
                        }
                        else if (type == 2) {
                            helicopter.changeSpeed(-HELICOPTER_SPEED_STEP);
                            if (helicopter.getSpeed() < -FAST_HELICOPTER_MAX_SPEED) helicopter.setSpeed(-FAST_HELICOPTER_MAX_SPEED);
                        }
                    }

                    if (event.getCode().equals(KeyCode.LEFT) || event.getCode().equals(KeyCode.A)) {
                        helicopter.rotate(-HELICOPTER_DIRECTION_STEP, 0, WINDOW_WIDTH, 0, WINDOW_HEIGHT, obstacles);
                    }
                    else if (event.getCode().equals(KeyCode.RIGHT) || event.getCode().equals(KeyCode.D)) {
                        helicopter.rotate(HELICOPTER_DIRECTION_STEP, 0, WINDOW_WIDTH, 0, WINDOW_HEIGHT, obstacles);
                    }
                }
                else {
                    double sceneMoveStep = 10.0;

                    if (event.getCode().equals(KeyCode.UP)) {
                        root.setTranslateY(root.getTranslateY() + sceneMoveStep);
                        helicopter.setTranslateY(helicopter.getTranslateY() - sceneMoveStep);
                    } else if (event.getCode().equals(KeyCode.DOWN)) {
                        root.setTranslateY(root.getTranslateY() - sceneMoveStep);
                        helicopter.setTranslateY(helicopter.getTranslateY() + sceneMoveStep);
                    } else if (event.getCode().equals(KeyCode.LEFT)) {
                        root.setTranslateX(root.getTranslateX() + sceneMoveStep);
                        helicopter.setTranslateX(helicopter.getTranslateX() - sceneMoveStep);
                    } else if (event.getCode().equals(KeyCode.RIGHT)) {
                        root.setTranslateX(root.getTranslateX() - sceneMoveStep);
                        helicopter.setTranslateX(helicopter.getTranslateX() + sceneMoveStep);
                    }
                }
            }

            if (event.getCode().equals(KeyCode.SPACE)) {
                if (!isRotorTimeline) {
                    helicopter.playRotorTimeline();
                    helicopter.playScaleTimeline();
                    heightTimeline.play();
                }
                else {
                    if (helicopter.isInsideWater(water)) {
                        timer.stop();
                        helicopter.underWaterTimeline();
                        reverseHeightTimeline.play();
                        speedometer.changeSpeed(helicopter.getSpeed());

                        resultLabel.setText("Game Over");
                        endGameStage.show();
                    }
                    else {
                        helicopter.reverseScaleTimeline();
                        reverseHeightTimeline.play();
                    }
                }
                isRotorTimeline = !isRotorTimeline;
            }
        });

        endGameStage = new Stage();
        endGameStage.initStyle(StageStyle.UTILITY);
        endGameStage.initModality(Modality.APPLICATION_MODAL);
        endGameStage.setTitle("Helicopter2DGame Ended");

        GridPane modalPane = new GridPane();
        modalPane.setVgap(10);

        modalPane.add(resultLabel, 0, 0);

        GridPane.setHalignment(resultLabel, HPos.CENTER);

        Scene endGameScene = new Scene(modalPane, 200, 100);
        endGameStage.setScene(endGameScene);

        timer = new Timeline(new KeyFrame(Duration.millis(16), event -> {
            long currentTime = System.currentTimeMillis();
            double elapsedSeconds = (currentTime - lastUpdateTime) / MILLIS_TO_SECONDS;

            if (helicopter.isOnSpecialHelipad(specialHelipad1) && specialHelipad1.getHasPlus() && helicopter.isOnTheGround()) {
                fuelLevel = 1.0;
                fuelIndicator.setFuelLevel(fuelLevel);
                specialHelipad1.removePlus();
                specialHelipad1.setHasPlus(false);
            }

            if (helicopter.isOnSpecialHelipad(specialHelipad2) && specialHelipad2.getHasPlus() && helicopter.isOnTheGround()) {
                fuelLevel = 1.0;
                fuelIndicator.setFuelLevel(fuelLevel);
                specialHelipad2.removePlus();
                specialHelipad2.setHasPlus(false);
            }

            if (fuelLevel <= 0) {
                timer.stop();
                helicopter.reverseScaleTimeline();
                reverseHeightTimeline.play();
                speedometer.changeSpeed(helicopter.getSpeed());

                resultLabel.setText("Game Over");
                endGameStage.show();

                return;
            }

            helicopter.update(elapsedSeconds, HELICOPTER_DAMP, 0, WINDOW_WIDTH, 0, WINDOW_HEIGHT, obstacles);
            speedometer.changeSpeed(helicopter.getSpeed());

            for (int i = 0; i < packages.length; i++) {
                if (packages[i] != null && packages[i].handleCollision(helicopter.getBoundsInParent())) {
                    root.getChildren().remove(packages[i]);
                    packages[i] = null;
                }
            }

            boolean allPackagesCollected = true;
            for (Package aPackage : packages) {
                if (aPackage != null) {
                    allPackagesCollected = false;
                    break;
                }
            }

            if (allPackagesCollected) {
                timer.stop();

                resultLabel.setText("You Won!");
                endGameStage.show();

                return;
            }

            double speed = Math.abs(helicopter.getSpeed());
            double fuelConsumed = sceneMovingMode ? HELICOPTER_SPEED_STEP * FUEL_CONSUMPTION_RATE : speed * FUEL_CONSUMPTION_RATE;
            fuelLevel -= fuelConsumed;
            fuelIndicator.setFuelLevel(fuelLevel);

            lastUpdateTime = currentTime;
        }));

        timer.setCycleCount(Animation.INDEFINITE);
        timer.play();

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

    private void showHelicopterChoiceModal() {
        helicopterChoiceStage = new Stage();
        helicopterChoiceStage.initStyle(StageStyle.UTILITY);
        helicopterChoiceStage.initModality(Modality.APPLICATION_MODAL);
        helicopterChoiceStage.setTitle("Choose Your Helicopter");

        VBox modalPane = new VBox(10);
        modalPane.setAlignment(Pos.CENTER);

        Label chooseLabel = new Label("Choose your helicopter:");
        chooseLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button helicopter1Button = createHelicopterChoiceButton("Normal Helicopter", "normal_helicopter.png");
        Button helicopter2Button = createHelicopterChoiceButton("Slow Helicopter", "slow_helicopter.png");
        Button helicopter3Button = createHelicopterChoiceButton("Fast Helicopter", "fast_helicopter.png");

        modalPane.getChildren().addAll(chooseLabel, helicopter1Button, helicopter2Button, helicopter3Button);

        helicopter1Button.setOnAction(event -> onHelicopterChosen(1));
        helicopter2Button.setOnAction(event -> onHelicopterChosen(0));
        helicopter3Button.setOnAction(event -> onHelicopterChosen(2));

        Scene helicopterChoiceScene = new Scene(modalPane, 500, 400);
        helicopterChoiceStage.setScene(helicopterChoiceScene);

        helicopterChoiceStage.showAndWait();
    }

    private Button createHelicopterChoiceButton(String text, String helicopterImage) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 14px;");
        button.setPrefWidth(200);

        Image image = new Image(Objects.requireNonNull(Game.class.getResourceAsStream(helicopterImage)));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);

        button.setGraphic(imageView);

        return button;
    }

    private void onHelicopterChosen(int type) {
        this.type = type;
        helicopterChoiceStage.close();
    }

    public static void main(String[] args) {
        launch();
    }
}