package com.g_art.kickerapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Kicker App
 * Created by G_Art on 16/2/2016.
 */
public class Game {
    private String _id;
    private Player createdBy;
    private String name;
    private int wins;
    @SerializedName("status")
    private GameState state;
    private Date date;
    private List<Team> teams;
    private List<Player> players;

    public Game() {
        players = new ArrayList<>();
        teams = new ArrayList<>();
        date = new Date();
        state = GameState.CREATED;
    }

    public Game(String _id) {
        this();
        this._id = _id;
    }

    public Game(String _id, String name) {
        this(_id);
        this.name = name;
    }

    public Game(String name, Date date, List<Team> teams) {
        this.name = name;
        this.date = date;
        this.teams = teams;
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

    public Player getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Player createdBy) {
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
        this.wins = wins;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
