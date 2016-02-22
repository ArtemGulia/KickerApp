package com.g_art.kickerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.g_art.kickerapp.R;
import com.g_art.kickerapp.fragment.GamesFragment;
import com.g_art.kickerapp.fragment.PlayerFragment;
import com.g_art.kickerapp.fragment.TournamentsFragment;
import com.g_art.kickerapp.model.Player;
import com.g_art.kickerapp.utils.prefs.SharedPrefsHandler;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;


/**
 * Kicker App
 * Created by G_Art on 16/2/2016.
 */
public class KickerAppActivity extends AppCompatActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {
    private SharedPrefsHandler loginHandler;
    public static int LOGIN_REQUEST_CODE = 1;

    private TextView mTxtPlayerName;
    private ImageView mImgNavPlayerAvatar;

    private Player player;

    private Socket mSocket;
//    {
//        try {
//            mSocket = IO.socket("http://kickerapp-statistics19.rhcloud.com");
//        } catch (URISyntaxException e) {}
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences mPrefs = this.getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        loginHandler = SharedPrefsHandler.getInstance(mPrefs);

        checkLogin();

//        mSocket.connect();

        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Initialize the first fragment when the application first loads.
        if (savedInstanceState == null) {
//            navigationView.setCheckedItem(R.id.nav_camera);
        }

        mTxtPlayerName = (TextView) findViewById(R.id.txt_nav_header_player_name);
        mImgNavPlayerAvatar = (ImageView) findViewById(R.id.nav_header_player_avatar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment fragment = new PlayerFragment();
        ft.replace(R.id.contentContainer, fragment).commit();

    }

    private void checkLogin() {
        if (!loginHandler.isLogged()) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
//            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivityForResult(loginIntent, LOGIN_REQUEST_CODE);
        } else {
            //TODO getSession
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
                player = data.getParcelableExtra("player");

                mTxtPlayerName.setText(player.getDisplayName());
                mImgNavPlayerAvatar.setOnClickListener(this);
                //get player from server via id
            }
        }
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment fragment = null;

        if (id == R.id.nav_camera) {
            fragment = new GamesFragment();
        } else if (id == R.id.nav_gallery) {
            fragment = new TournamentsFragment();
        }

        if (null != fragment) {
            ft.replace(R.id.contentContainer, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.nav_header_player_avatar:
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                Fragment fragment = new PlayerFragment();
                ft.replace(R.id.contentContainer, fragment).commit();
                break;
        }
    }
}
