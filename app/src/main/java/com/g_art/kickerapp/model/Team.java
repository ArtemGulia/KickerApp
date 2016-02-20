package com.g_art.kickerapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Kicker App
 * Created by G_Art on 16/2/2016.
 */
public class Team {
    private String _id;
    private String scores;
    @SerializedName("players")
    private List<Player> playerList;

    public Team() {
    }

    public Team(String _id, List<Player> playerList) {
        this._id = _id;
        this.playerList = playerList;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getScores() {
        return scores;
    }

    public void setScores(String scores) {
        this.scores = scores;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }
}
