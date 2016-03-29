package com.g_art.kickerapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Kicker App
 * Created by G_Art on 16/2/2016.
 */
public class Team {
    private String _id;
    private int scores;
    @SerializedName("players")
    private List<Player> players;

    public Team() {
    }

    public Team(String _id, List<Player> players) {
        this._id = _id;
        this.players = players;
    }

    public void addPlayer(Player player) {
        if (players == null) {
            players = new ArrayList<>();
        }
        players.add(player);
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
