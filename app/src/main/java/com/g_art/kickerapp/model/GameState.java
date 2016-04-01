package com.g_art.kickerapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Kicker App
 * Created by G_Art on 24/3/2016.
 */
public enum GameState {
    @SerializedName("1")
    CREATED(1),
    @SerializedName("2")
    READY(2),
    @SerializedName("3")
    ACTIVE(3),
    @SerializedName("4")
    FINISHED(4);

    private int status;

    GameState(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
