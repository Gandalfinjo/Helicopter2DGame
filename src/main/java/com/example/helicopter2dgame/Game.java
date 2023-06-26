package com.example.helicopter2dgame;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.util.Objects;

public class Game extends Application {
    private static final double WINDOW_WIDTH = 800;
    private static final double WINDOW_HEIGHT = 700;

    private static final double HELICOPTER_WIDTH = 0.03 * WINDOW_WIDTH;
    private static final double HELICOPTER_HEIGHT = 0.07 * WINDOW_HEIGHT;
    private static final double HELICOPTER_SPEED_STEP = 5;
    private static final double HELICOPTER_DIRECTION_STEP = 5;
    private static final double HELICOPTER_DAMP = 0.995;

    @Override
    public void start(Stage stage) {
        Group root = new Group();

        Group packageGroup = new Group();
        Package[] packages = Package.generatePackages(5, WINDOW_WIDTH, WINDOW_HEIGHT);
        packageGroup.getChildren().addAll(packages);
        packageGroup.getTransforms().addAll(
                new Translate(-WINDOW_WIDTH / 2, -WINDOW_HEIGHT / 2)
        );

        Helicopter helicopter = new Helicopter(HELICOPTER_WIDTH, HELICOPTER_HEIGHT);

        root.getChildren().addAll(packageGroup, helicopter);
        root.getTransforms().addAll(
                new Translate(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2)
        );

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().equals(KeyCode.UP) || event.getCode().equals(KeyCode.W)) {
                helicopter.changeSpeed(HELICOPTER_SPEED_STEP);
            }
            else if (event.getCode().equals(KeyCode.DOWN) || event.getCode().equals(KeyCode.S)) {
                helicopter.changeSpeed(-HELICOPTER_SPEED_STEP);
            }

            if (event.getCode().equals(KeyCode.LEFT) || event.getCode().equals(KeyCode.A)) {
                helicopter.rotate(-HELICOPTER_DIRECTION_STEP, 0, WINDOW_WIDTH, 0, WINDOW_HEIGHT);
            }
            else if (event.getCode().equals(KeyCode.RIGHT) || event.getCode().equals(KeyCode.D)) {
                helicopter.rotate(HELICOPTER_DIRECTION_STEP, 0, WINDOW_WIDTH, 0, WINDOW_HEIGHT);
            }
        });

        MyTimer.IUpdatable helicopterWrapper = ds -> helicopter.update(ds, HELICOPTER_DAMP, 0, WINDOW_WIDTH, 0, WINDOW_HEIGHT);

        MyTimer myTimer = new MyTimer(helicopterWrapper);
        myTimer.start();

        Image image = new Image(Objects.requireNonNull(Game.class.getResourceAsStream("grass.jpg")));
        ImagePattern imagePattern = new ImagePattern(image);

        scene.setFill(imagePattern);
        stage.setTitle("Helicopter2DGame");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}