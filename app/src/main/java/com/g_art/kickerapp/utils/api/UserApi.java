package com.g_art.kickerapp.utils.api;

import com.g_art.kickerapp.model.Player;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;


/**
 * Kicker App
 * Created by G_Art on 18/2/2016.
 */
public interface UserApi {

    @GET("/auth/user")
    void authUser(Callback<Player> callback);

    @POST("")
    void getPlayer(@Body String providerId, @Body String provider, @Body String displayName, Callback<Player> callback);

    @POST("")
    void loginPlayerOrCreate(@Body String providerId, @Body String provider, @Body String displayName, Callback<Player> callback);

    @POST("/auth/android")
    void loginPlayerOrCreate(@Body Map<String, String> profile, Callback<Player> callback);
}
