package com.g_art.kickerapp.services.transformer;

import com.g_art.kickerapp.model.Game;
import com.g_art.kickerapp.model.Team;
import com.g_art.kickerapp.model.dto.GameDTO;
import com.g_art.kickerapp.model.dto.TeamDTO;

/**
 * Kicker App
 * Created by G_Art on 5/4/2016.
 */
public interface DTOTransformer {

    Game transform(GameDTO gameDTO);
    GameDTO transform(Game game);

    Team transform(TeamDTO teamDTO);
    TeamDTO transform(Team team);
}
