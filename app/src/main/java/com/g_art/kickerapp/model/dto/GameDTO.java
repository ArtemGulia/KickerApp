package com.g_art.kickerapp.model.dto;

import com.g_art.kickerapp.model.GameState;
import com.g_art.kickerapp.model.Player;
import com.g_art.kickerapp.model.Team;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Kicker App
 * Created by G_Art on 5/4/2016.
 */
public class GameDTO {
    private String _id;
    private String createdBy;
    private String name;
    private int wins;
    @SerializedName("status")
    private GameState state;
    private Date date;
    private List<TeamDTO> teams;
    private List<String> players;

    public GameDTO() {
        players = new ArrayList<>();
        teams = new ArrayList<>();
        date = new Date();
        state = GameState.CREATED;
    }

    public GameDTO(String _id) {
        this();
        this._id = _id;
    }

    public GameDTO(String _id, String name) {
        this(_id);
        this.name = name;
    }

    public GameDTO(String name, Date date, List<TeamDTO> teams) {
        this.name = name;
        this.date = date;
        this.teams = teams;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int score) {
        this.wins = score;
    }

    @SerializedName("status")
    public GameState getState() {
        return state;
    }

    @SerializedName("status")
    public void setState(GameState state) {
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<TeamDTO> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamDTO> teams) {
        this.teams = teams;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }
}
