package com.g_art.kickerapp.model;

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
    private Date date;
    private List<Team> teams;

    public Game() {
    }

    public Game(String _id) {
        this._id = _id;
    }

    public Game(String _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public Game(String name, Date date, List<Team> teams) {
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
}
