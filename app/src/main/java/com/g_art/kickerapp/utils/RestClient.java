package com.g_art.kickerapp.utils;

//import com.squareup.okhttp.Interceptor;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Response;

import java.io.IOException;

import retrofit.RestAdapter;
import retrofit.client.OkClient;


/**
 * Kicker App
 * Created by G_Art on 18/2/2016.
 */
public class RestClient {

    private static String API_BASE_URL = "http://kickerapp-statistics19.rhcloud.com" ;
    private static UserApi userApi;
    private static GameApi gameApi;

    private static RestAdapter.Builder builder = new RestAdapter.Builder()
            .setEndpoint(API_BASE_URL);
//            .setClient(new OkClient(new OkHttpClient()));


    public static UserApi getUserApi() {
        if (userApi == null) {
            RestAdapter adapter = builder.build();
            userApi = adapter.create(UserApi.class);
        }
        return userApi ;
    }

    public static GameApi getGameApi() {
        if (gameApi == null) {
            RestAdapter adapter = builder.build();
            gameApi = adapter.create(GameApi.class);
        }
        return gameApi ;
    }
}
