package com.g_art.kickerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.g_art.kickerapp.R;
import com.g_art.kickerapp.model.Game;
import com.g_art.kickerapp.model.Player;
import com.g_art.kickerapp.utils.GameApi;
import com.g_art.kickerapp.utils.RestClient;
import com.g_art.kickerapp.utils.SharedPrefsHandler;
import com.g_art.kickerapp.utils.UserApi;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Kicker App
 * Created by G_Art on 16/2/2016.
 */
public class KickerAppActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SharedPrefsHandler loginHandler;
    public static int LOGIN_REQUEST_CODE = 1;

    private Player player;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://kickerapp-statistics19.rhcloud.com");
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences mPrefs = this.getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        loginHandler = SharedPrefsHandler.getInstance(mPrefs);

        checkLogin();

        mSocket.connect();


        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void checkLogin() {
        if (!loginHandler.isLogged()) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
//            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivityForResult(loginIntent, LOGIN_REQUEST_CODE);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                getPlayer();

                GameApi gameApi = RestClient.getGameApi();

                //get player from server via id
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    private void getPlayer() {
        UserApi userApi = RestClient.getUserApi();
        Map<String, ?> allProps = loginHandler.getAll();
        String provider = (String) allProps.get(SharedPrefsHandler.LOGIN_PROVIDER);
        String providerId = (String) allProps.get(SharedPrefsHandler.PLAYER_ID);
        String displayName = (String) allProps.get(SharedPrefsHandler.PLAYER_NAME);
        Map<String, String> profile = new HashMap<>();
        profile.put(SharedPrefsHandler.PROVIDER_ID, providerId);
        profile.put(SharedPrefsHandler.PLAYER_NAME, displayName);
        profile.put(SharedPrefsHandler.LOGIN_PROVIDER, provider);
        userApi.loginPlayerOrCreate(profile, new Callback<Player>() {
            @Override
            public void success(Player player, Response response) {
                if (response != null) {

                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (error != null) {

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
