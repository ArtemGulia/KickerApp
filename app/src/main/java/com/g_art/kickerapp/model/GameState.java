package com.g_art.kickerapp.model;

/**
 * Kicker App
 * Created by G_Art on 24/3/2016.
 */
public enum GameState {
    CREATED(1),
    READY(2),
    ACTIVE(3),
    FINISHED(4);

    private int state;

    GameState(int state) {
        this.state = state;
    }
}
