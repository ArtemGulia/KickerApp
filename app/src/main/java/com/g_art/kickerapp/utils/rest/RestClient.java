package com.g_art.kickerapp.utils.rest;

import com.g_art.kickerapp.utils.api.GameApi;
import com.g_art.kickerapp.utils.api.UserApi;
import com.g_art.kickerapp.utils.prefs.SharedPrefsHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;


/**
 * Kicker App
 * Created by G_Art on 18/2/2016.
 */
public class RestClient {

    private static final String API_BASE_URL = "http://kickerapp-statistics19.rhcloud.com";
    public static final String COOKIE = "COOKIE";
    private static String sSessionId;
    private static UserApi userApi;
    private static GameApi gameApi;

    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();

    private static RestAdapter.Builder builder = new RestAdapter.Builder()
            .setEndpoint(API_BASE_URL)
            .setConverter(new GsonConverter(gson))
            .setRequestInterceptor(new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    if (null != sSessionId) {
                        request.addHeader(COOKIE, sSessionId);
                    }
                }
            })
            .setLogLevel(RestAdapter.LogLevel.FULL);


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

    public static String getsSessionId() {
        return sSessionId;
    }

    public static void setsSessionId(String sSessionId) {
        RestClient.sSessionId = sSessionId;
    }
}
