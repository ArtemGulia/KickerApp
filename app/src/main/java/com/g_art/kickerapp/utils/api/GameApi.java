package com.g_art.kickerapp.utils.api;

import com.g_art.kickerapp.model.Game;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Kicker App
 * Created by G_Art on 18/2/2016.
 */
public interface GameApi {

    void createGame(Game game, Callback<Game> callback);

    void getGame(Game game, Callback<Game> callback);

    void getActiveGames(Callback<List<Game>> callback);

    @GET("/games/list")
    void getAllGames(Callback<List<Game>> callback);
}
