package com.g_art.kickerapp.utils.api;

import com.g_art.kickerapp.model.Game;
import com.g_art.kickerapp.model.Player;
import com.g_art.kickerapp.model.dto.GameDTO;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Kicker App
 * Created by G_Art on 18/2/2016.
 */
public interface GameApi {

    @Headers("Content-Type: application/json")
    @POST("/games/create")
    void createGame(Player user, Callback<Game> callback);

    @Headers("Content-Type: application/json")
    @POST("/games/update")
    void updateGame(@Body GameDTO game, Callback<Game> callback);

    @Headers("Content-Type: application/json")
    @POST("/games/players")
    void getPlayersForTheGame(@Body Game game, Callback<List<Player>> callback);

    @Headers("Content-Type: application/json")
    @POST("/game/get")
    void getGame(@Body Game id, Callback<Game> callback);

    void getActiveGames(Callback<List<Game>> callback);

    @GET("/games/list")
    void getAllGames(Callback<List<Game>> callback);

    @Headers("Content-Type: application/json")
    @POST("/games/addScore")
    void addScore(@Body Map<String, String> params, Callback<Game> callback);
}
