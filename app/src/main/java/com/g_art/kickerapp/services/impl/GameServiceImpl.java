package com.g_art.kickerapp.services.impl;

import com.g_art.kickerapp.model.Game;
import com.g_art.kickerapp.model.Player;
import com.g_art.kickerapp.model.Team;
import com.g_art.kickerapp.services.GameService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Kicker App
 * Created by G_Art on 16/3/2016.
 */
public class GameServiceImpl implements GameService {

    @Override
    public Game createGame() {
        Game game = new Game();

        Team team_1 = new Team();
        Team team_2 = new Team();
        List<Team> teamList = new ArrayList<>();
        teamList.add(team_1);
        teamList.add(team_2);

        game.setTeams(teamList);

        return game;
    }

    @Override
    public Game createGame(Team fTeam, Team sTeam) {
        Game game = new Game();

        List<Team> teamList = new ArrayList<>();
        teamList.add(fTeam);
        teamList.add(sTeam);

        game.setTeams(teamList);

        return game;
    }

    @Override
    public Game createGame(Player player) {
        Game game = createGame();

        if (player != null) {

            game.setCreatedBy(player);
            game.addPlayer(player);

            game.getTeams().get(0).addPlayer(player);
        }

        return game;
    }

    @Override
    public boolean isTeamEmpty(Team team) {
        if (team == null) {
            return true;
        }

        List<Player> players = team.getPlayers();
        if (players == null || players.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public void addPlayerToFTeam(Game game, Player newPlayer) {
        if (game != null && newPlayer != null) {
            List<Player> players = game.getPlayers();
            for (Player player : players) {
                if (newPlayer.get_id().equals(player.get_id())){
                    return;
                }
            }

            Team fTeam = game.getTeams().get(0);
            if (fTeam.getPlayers().size() >= 2) {
                return;
            }

            fTeam.addPlayer(newPlayer);
            game.addPlayer(newPlayer);
        }
    }

    public void changePlayersInFTeam(Game game, Player fPlayer, Player sPlayer) {
        if (fPlayer != null && sPlayer != null && game != null) {
            List<Player> players = game.getPlayers();

            Team fTeam = game.getFTeam();

            changePlayersInTeam(game, fPlayer, sPlayer, players, fTeam);
        }
    }
    public void changePlayersInSTeam(Game game, Player fPlayer, Player sPlayer) {
        if (fPlayer != null && sPlayer != null && game != null) {
            List<Player> players = game.getPlayers();

            Team sTeam = game.getSTeam();

            changePlayersInTeam(game, fPlayer, sPlayer, players, sTeam);
        }
    }

    @Override
    public void changePlayersInTeam(Game game, Team team, List<Player> players) {
        if (team != null && players != null && !players.isEmpty() && game != null) {
            List<Player> inGame = game.getPlayers();

            Iterator<Player> it = inGame.iterator();
            while (it.hasNext()) {
                Player inGamePlayer = it.next();
                for (Player pl : team.getPlayers()) {
                    if (pl.get_id().equals(inGamePlayer.get_id())) {
                        it.remove();
                    }
                }
            }

            team.setPlayers(players);

            for (Player player : players) {
                game.addPlayer(player);
            }
        }
    }

    private void changePlayersInTeam(Game game, Player fPlayer, Player sPlayer, List<Player> players, Team team) {
        Iterator<Player> it = players.iterator();
        while (it.hasNext()) {
            Player inGamePlayer = it.next();
            if (team.getPlayers() != null && !team.getPlayers().isEmpty()) {
                for (Player pl : team.getPlayers()) {
                    if (pl.get_id().equals(inGamePlayer.get_id())) {
                        it.remove();
                    }
                }
            }
        }

        List<Player> newPlayers = new ArrayList<>();
        newPlayers.add(fPlayer);
        newPlayers.add(sPlayer);

        team.setPlayers(newPlayers);

        game.addPlayer(fPlayer);
        game.addPlayer(sPlayer);
    }

    @Override
    public boolean isPlayerFits(Game game, Team team) {
        if (game == null || team == null) {
            return false;
        }

        if (team.getPlayers().size() >= 2) {
            return false;
        }

        return true;
    }

    @Override
    public boolean isPlayerFits(Game game, Team team, Player player) {
        if (isPlayerFits(game, team)) {
            List<Player> players = game.getPlayers();
            for (Player gPlayer : players) {
                if (player.get_id().equals(gPlayer.get_id())) {
                    return false;
                }
            }
        }
        return true;
    }
}
