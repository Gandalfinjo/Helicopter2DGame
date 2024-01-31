package com.example.helicopter2dgame;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

import java.util.Random;

public class Package extends Rectangle {
    private static final int PACKAGE_SIZE = 20;

    public Package(double x, double y) {
        super(x, y, PACKAGE_SIZE, PACKAGE_SIZE);
        setFill(Color.rgb(123, 0, 44));
    }

    public Package(double size, Translate position) {
        super(size, size, Color.DARKRED);
        super.getTransforms().addAll(position);
    }

    public static Package[] generatePackages(int numOfPackages, double sceneWidth, double sceneHeight) {
        Random random = new Random();
        Package[] packages = new Package[numOfPackages];

        for (int i = 0; i < numOfPackages; i++) {
            double x = random.nextDouble() * (sceneWidth - PACKAGE_SIZE);
            double y = random.nextDouble() * (sceneHeight - PACKAGE_SIZE);
            packages[i] = new Package(x, y);
        }

        return packages;
    }

    public boolean handleCollision(Bounds helicopterBounds) {
        return super.getBoundsInParent().intersects(helicopterBounds);
    }

}
