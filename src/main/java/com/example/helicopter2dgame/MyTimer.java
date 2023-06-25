package com.example.helicopter2dgame;

import javafx.animation.AnimationTimer;

public class MyTimer extends AnimationTimer {
    public interface IUpdatable {
        void update(double ds);
    }

    private long previous;
    private final IUpdatable[] updatables;

    public MyTimer(IUpdatable ...updatables) {
        this.updatables = new IUpdatable[updatables.length];

        System.arraycopy(updatables, 0, this.updatables, 0, this.updatables.length);
    }

    @Override
    public void handle(long now) {
        if (previous == 0) previous = now;

        double ds = (now - previous) / 1e9;
        previous = now;

        for (IUpdatable updatable : updatables) updatable.update(ds);
    }
}
