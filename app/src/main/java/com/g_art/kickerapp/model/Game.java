package com.g_art.kickerapp.model;

import java.util.Date;
import java.util.List;

/**
 * Kicker App
 * Created by G_Art on 16/2/2016.
 */
public class Game {
    private String name;
    private Date date;
    private List<Team> teams;

    public Game() {
    }

    public Game(String name) {
        this.name = name;
    }

    public Game(String name, Date date, List<Team> teams) {
        this.name = name;
        this.date = date;
        this.teams = teams;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
