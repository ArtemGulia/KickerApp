package com.g_art.kickerapp.model.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Kicker App
 * Created by G_Art on 5/4/2016.
 */
public class TeamDTO {
    private String _id;
    private int scores;
    @SerializedName("players")
    private List<String> players;

    public TeamDTO() {
    }

    public TeamDTO(String _id, List<String> players) {
        this._id = _id;
        this.players = players;
    }

    public void addPlayer(String playersId) {
        if (players == null) {
            players = new ArrayList<>();
        }
        players.add(playersId);
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

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }
}
