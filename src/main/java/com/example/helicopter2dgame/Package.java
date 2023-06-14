package com.example.helicopter2dgame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Package extends Rectangle {
    private static final int PACKAGE_SIZE = 20;

    public Package(double x, double y) {
        super(x, y, PACKAGE_SIZE, PACKAGE_SIZE);
        setFill(Color.AQUA);
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

}
