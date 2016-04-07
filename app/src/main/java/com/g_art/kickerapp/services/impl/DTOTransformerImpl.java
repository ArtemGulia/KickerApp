package com.g_art.kickerapp.services.impl;

import com.g_art.kickerapp.model.Game;
import com.g_art.kickerapp.model.Player;
import com.g_art.kickerapp.model.Team;
import com.g_art.kickerapp.model.dto.GameDTO;
import com.g_art.kickerapp.model.dto.TeamDTO;
import com.g_art.kickerapp.services.transformer.DTOTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Kicker App
 * Created by G_Art on 5/4/2016.
 */
public class DTOTransformerImpl implements DTOTransformer {

    @Override
    public Game transform(GameDTO gameDTO) {
        if (gameDTO == null) {
            return null;
        }

        Game game = new Game();

        if (gameDTO.get_id() != null) {
            game.set_id(gameDTO.get_id());
        }

        game.setDate(gameDTO.getDate());
        game.setName(gameDTO.getName());
        game.setState(gameDTO.getState());
        game.setWins(gameDTO.getWins());

        return game;
    }

    @Override
    public GameDTO transform(Game game) {
        if (game == null) {
            return null;
        }

        GameDTO gameDTO = new GameDTO();
        gameDTO.set_id(game.get_id());
        gameDTO.setWins(game.getWins());
        gameDTO.setState(game.getState());
        gameDTO.setName(game.getName());

        if (game.getCreatedBy() != null) {
            gameDTO.setCreatedBy(game.getCreatedBy().get_id());
        }

        if (game.getPlayers() != null) {
            List<String> players = new ArrayList<>();

            for (Player player : game.getPlayers()) {
                players.add(player.get_id());
            }
            gameDTO.setPlayers(players);
        }

        if (game.getTeams() != null) {
            List<TeamDTO> teamsDTO = new ArrayList<>();

            for (Team team : game.getTeams()) {
                teamsDTO.add(transform(team));
            }
            gameDTO.setTeams(teamsDTO);
        }

        return gameDTO;
    }

    @Override
    public Team transform(TeamDTO teamDTO) {
        return null;
    }

    @Override
    public TeamDTO transform(Team team) {
        if (team == null) {
            return null;
        }

        TeamDTO teamDTO = new TeamDTO();

        teamDTO.set_id(team.get_id());
        teamDTO.setScores(team.getScores());

        if (team.getPlayers() != null) {
            List<String> playersID = new ArrayList<>();

            for (Player player : team.getPlayers()) {
                playersID.add(player.get_id());
            }

            teamDTO.setPlayers(playersID);
        }

        return teamDTO;
    }
}
