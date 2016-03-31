package com.g_art.kickerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.g_art.kickerapp.R;
import com.g_art.kickerapp.model.Player;
import com.g_art.kickerapp.utils.api.UserApi;
import com.g_art.kickerapp.utils.prefs.SharedPrefsHandler;
import com.g_art.kickerapp.utils.rest.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;

/**
 * Kicker App
 * Created by G_Art on 17/3/2016.
 */
public class SplashActivity extends AppCompatActivity {
    private SharedPrefsHandler loginHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initSharedPrefs();

        checkPlayerAccount();

    }

    private void checkPlayerAccount() {
        if (loginHandler.isLogged()) {
            authorizeUser();
        } else {
            openPlayerProfile(null);
        }
    }

    private void initSharedPrefs() {
        SharedPreferences mPrefs = this.getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        loginHandler = SharedPrefsHandler.getInstance(mPrefs);
    }

    private void authorizeUser() {
        UserApi userApi = RestClient.getUserApi();
        Map<String, ?> profileCash = loginHandler.getAll();
        Map<String, String> profile = new HashMap<>();
        profile.put(SharedPrefsHandler.PROVIDER_ID,
                profileCash.get(SharedPrefsHandler.PROVIDER_ID).toString());
        profile.put(SharedPrefsHandler.PLAYER_NAME,
                profileCash.get(SharedPrefsHandler.PLAYER_NAME).toString());
        profile.put(SharedPrefsHandler.LOGIN_PROVIDER,
                profileCash.get(SharedPrefsHandler.LOGIN_PROVIDER).toString());

        userApi.loginPlayerOrCreate(profile, new Callback<Player>() {
            @Override
            public void success(Player player, Response response) {
                List<Header> headers = response.getHeaders();
                for (Header header : headers) {
                    String headerName = header.getName();
                    if (SharedPrefsHandler.COOKIE.equals(headerName)) {
                        String cookie = header.getValue();
                        RestClient.setsSessionId(cookie);
                    }
                }

                openPlayerProfile(player);
            }

            @Override
            public void failure(RetrofitError error) {
                if (error != null) {
                    Toast.makeText(getApplicationContext(), error.getUrl(),
                            Toast.LENGTH_LONG).show();
                    openPlayerProfile(null);
                }
            }
        });
    }

    private void openPlayerProfile(Player player) {
        Intent intent = new Intent(this, KickerAppActivity.class);
        intent.putExtra(KickerAppActivity.PLAYER_KEY, player);
        startActivity(intent);
        finish();
    }
}