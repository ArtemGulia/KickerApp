package com.g_art.kickerapp.services;

import com.g_art.kickerapp.model.Game;
import com.g_art.kickerapp.model.Player;
import com.g_art.kickerapp.model.Team;

import java.util.List;

/**
 * Kicker App
 * Created by G_Art on 16/3/2016.
 */
public interface GameService {

    Game createGame();

    Game createGame(Team fTeam, Team sTeam);

    Game createGame(Player player);

    void addPlayerToFTeam(Game game, Player player);

    void changePlayersInFTeam(Game game, Player fPlayer, Player sPlayer);

    void changePlayersInSTeam(Game game, Player fPlayer, Player sPlayer);

    void changePlayersInTeam(Game game, Team team, List<Player> players);

    boolean isTeamEmpty(Team team);

    boolean isPlayerFits(Game game, Team team);

    boolean isPlayerFits(Game game, Team team, Player player);
}
