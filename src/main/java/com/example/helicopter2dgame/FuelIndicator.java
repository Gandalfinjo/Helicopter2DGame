package com.example.helicopter2dgame;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Line;

public class FuelIndicator extends Group {
    private static final double INDICATOR_RADIUS = 50;
    private static final double INDICATOR_WIDTH = 5;

    private Arc redArc;
    private Arc blackArc;
    private Line pointerLine;

    public FuelIndicator() {

        redArc = new Arc(INDICATOR_RADIUS, INDICATOR_RADIUS, INDICATOR_RADIUS, INDICATOR_RADIUS, 120, 60);
        redArc.setFill(Color.TRANSPARENT);
        redArc.setStroke(Color.RED);
        redArc.setStrokeWidth(INDICATOR_WIDTH);

        blackArc = new Arc(INDICATOR_RADIUS, INDICATOR_RADIUS, INDICATOR_RADIUS, INDICATOR_RADIUS, 0, 120);
        blackArc.setFill(Color.TRANSPARENT);
        blackArc.setStroke(Color.BLACK);
        blackArc.setStrokeWidth(INDICATOR_WIDTH);

        pointerLine = new Line();
        pointerLine.setStroke(Color.BLACK);
        pointerLine.setStrokeWidth(INDICATOR_WIDTH);
        pointerLine.setStartX(INDICATOR_RADIUS);
        pointerLine.setStartY(INDICATOR_RADIUS);
        pointerLine.setEndX(INDICATOR_RADIUS - INDICATOR_WIDTH);
        pointerLine.setEndY(INDICATOR_RADIUS);

        getChildren().addAll(redArc, blackArc, pointerLine);
    }

    public void setFuelLevel(double fuelLevel) {
        double angle = -180 + (fuelLevel * 180);
        double radians = Math.toRadians(angle);

        double pointerX = INDICATOR_RADIUS + Math.cos(radians) * (INDICATOR_RADIUS - INDICATOR_WIDTH);
        double pointerY = INDICATOR_RADIUS + Math.sin(radians) * (INDICATOR_RADIUS - INDICATOR_WIDTH);

        pointerLine.setEndX(pointerX);
        pointerLine.setEndY(pointerY);
    }
}
