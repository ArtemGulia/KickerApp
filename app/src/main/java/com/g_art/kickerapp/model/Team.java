package com.g_art.kickerapp.model;

import java.util.List;

/**
 * Kicker App
 * Created by G_Art on 16/2/2016.
 */
public class Team {
    private String teamName;
    private List<Player> playerList;

    public Team() {
    }

    public Team(String teamName, List<Player> playerList) {
        this.teamName = teamName;
        this.playerList = playerList;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }
}
