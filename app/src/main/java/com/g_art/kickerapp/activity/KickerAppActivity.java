package com.g_art.kickerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.g_art.kickerapp.R;
import com.g_art.kickerapp.fragment.profile.PlayerFragment;
import com.g_art.kickerapp.fragment.game.GameFragment;
import com.g_art.kickerapp.fragment.game.GamesFragment;
import com.g_art.kickerapp.fragment.tournament.TournamentsFragment;
import com.g_art.kickerapp.model.Player;
import com.g_art.kickerapp.utils.api.UserApi;
import com.g_art.kickerapp.utils.prefs.SharedPrefsHandler;
import com.g_art.kickerapp.utils.rest.RestClient;
import com.g_art.kickerapp.utils.ui.Fab;
import com.github.nkzawa.socketio.client.Socket;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;


/**
 * Kicker App
 * Created by G_Art on 16/2/2016.
 */
public class KickerAppActivity extends AppCompatActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    public static final String PLAYER_KEY = "player";
    public static final String SIGN_UP = "signUp";
    public static final String CHECKED_ITEM_ID = "checkedItemId";
    public static final String NEW_GAME_KEY = "is_new_game";
    public static final String GAME_KEY_ID = "game_id";
    public int item_id = 0;
    private SharedPrefsHandler loginHandler;
    public static int LOGIN_REQUEST_CODE = 1;
    public final static String FRAGMENT_TAG = "Fragment_Tag";
    private boolean signUp = false;

    private TextView mTxtPlayerName;
    private CircleImageView mImgNavPlayerAvatar;
    private ActionBarDrawerToggle toggle;

    private FloatingActionButton addFab;

    private MaterialSheetFab<Fab> materialSheetFab;
    private Fab editFab;

    private DrawerLayout drawer;

    private Fragment mFragment;

    private Player mPlayer;

    private Socket mSocket;

    private int statusBarColor;
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

        setContentView(R.layout.activity_home);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = null;
        if (navigationView != null) {
            header = navigationView.getHeaderView(0);
            navigationView.setNavigationItemSelectedListener(this);
        }

        if (header != null) {
            header.setOnClickListener(this);
            mTxtPlayerName = (TextView) header.findViewById(R.id.txt_nav_header_player_name);
            mImgNavPlayerAvatar = (CircleImageView) header.findViewById(R.id.nav_header_player_avatar);
        }
        mImgNavPlayerAvatar.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Player value = extras.getParcelable(PLAYER_KEY);
            if (value != null) {
                mPlayer = value;
                signUp = false;

                Snackbar.make(findViewById(R.id.contentContainer),
                        mPlayer.getDisplayName() + " via " + mPlayer.getProvider(),
                        Snackbar.LENGTH_SHORT).show();
            }
        }

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            signUp = savedInstanceState.getBoolean(SIGN_UP);
            item_id = savedInstanceState.getInt(CHECKED_ITEM_ID, 0);
            mPlayer = savedInstanceState.getParcelable(PLAYER_KEY);
            mFragment = getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_TAG);
            if (item_id == 0) {
                uncheckAllMenuItems(navigationView);
            } else {
                if (navigationView != null) {
                    navigationView.setCheckedItem(item_id);
                }
            }
        } else {
            checkLogin();
        }
        updateNavHeader();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addFab = (FloatingActionButton) findViewById(R.id.fab);
        if (addFab != null) {
            addFab.setOnClickListener(this);
        }

        FloatingActionButton okFab = (FloatingActionButton) findViewById(R.id.okFab);
        if (okFab != null) {
            okFab.hide();
        }

        editFab = (Fab) findViewById(R.id.editFab);
        if (editFab != null) {
            editFab.hide();
        }
        initEditFab();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        registerBackStackListener();

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStack();
                    } else {
                        drawer.openDrawer(GravityCompat.START);
                    }
                }
            });
        }
    }

    private void checkLogin() {
        if (!loginHandler.isLogged()) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            signUp = true;
            startActivityForResult(loginIntent, LOGIN_REQUEST_CODE);
        } else {
            signUp = false;
            if (mPlayer == null || RestClient.getsSessionId() == null) {
                authorizeUser();
            } else {
                openPlayerProfile(mPlayer);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else if (materialSheetFab.isSheetVisible()) {
                materialSheetFab.hideSheet();
            } else {
                super.onBackPressed();
            }
        } else if (materialSheetFab.isSheetVisible()) {
            materialSheetFab.hideSheet();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (!signUp) {
            //Save instance
            outState.putParcelable(PLAYER_KEY, mPlayer);
            getSupportFragmentManager().putFragment(outState, FRAGMENT_TAG, mFragment);
        }
        outState.putInt(CHECKED_ITEM_ID, item_id);
        outState.putBoolean(SIGN_UP, signUp);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mPlayer = data.getParcelableExtra("player");
                signUp = false;

                Snackbar.make(findViewById(R.id.contentContainer),
                        mPlayer.getDisplayName() + " via " + mPlayer.getProvider(),
                        Snackbar.LENGTH_LONG).show();
                openPlayerProfile(mPlayer);

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), data.getStringExtra("resultMsg"),
                        Toast.LENGTH_LONG).show();
            }
        }
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

                Snackbar.make(findViewById(R.id.contentContainer),
                        player.getDisplayName() + " via " + player.getProvider(),
                        Snackbar.LENGTH_LONG).show();

                openPlayerProfile(player);
            }

            @Override
            public void failure(RetrofitError error) {
                if (error != null) {
                    Toast.makeText(getApplicationContext(), error.getUrl(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void openPlayerProfile(Player player) {
        mPlayer = player;
        updateNavHeader();
        //get player from server via id
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (fragment == null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(PlayerFragment.PLAYER_KEY, mPlayer);
            FragmentTransaction ft = fragmentManager.beginTransaction();
            fragment = new PlayerFragment();
            mFragment = fragment;
            fragment.setArguments(bundle);
            ft.replace(R.id.contentContainer, fragment, FRAGMENT_TAG).commit();
        } else {
            mFragment = fragment;
        }
    }

    private void updateNavHeader() {
        if (mPlayer != null) {
            mTxtPlayerName.setText(mPlayer.getDisplayName());
            String avatarUrl = mPlayer.getImage();
            if (null != avatarUrl && !avatarUrl.isEmpty()) {
                Picasso.with(this).load(avatarUrl)
                        .placeholder(R.drawable.account)
                        .error(R.drawable.ic_info_black_48px)
                        .fit()
                        .into(mImgNavPlayerAvatar);
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
        item_id = id;
        // now set clicked menu item to checked
        item.setChecked(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment fragment = null;

        GameFragment gFr = (GameFragment) fragmentManager.findFragmentByTag(GamesFragment.GAME_FRAGMENT);
        if (gFr != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(gFr);
            transaction.commit();
        }

        if (id == R.id.nav_camera) {
            fragment = new GamesFragment();
        } else if (id == R.id.nav_gallery) {
            fragment = new TournamentsFragment();
        }

        if (null != fragment) {

            Bundle bundle = new Bundle();
            bundle.putParcelable(PLAYER_KEY, mPlayer);
            fragment.setArguments(bundle);

            mFragment = fragment;
            ft.replace(R.id.contentContainer, fragment, FRAGMENT_TAG).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                openGameFragment();
                break;
            case R.id.nav_header_player_avatar:
                openPlayerProfileFromNV();
                break;
            case R.id.nav_header:
                openPlayerProfileFromNV();
                break;
            case R.id.toolbar:
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.fab_sheet_item_play:
                break;
            case R.id.fab_sheet_item_name:
                break;
            case R.id.fab_sheet_item_win_score:
                break;
        }
    }

    private void openGameFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PLAYER_KEY, mPlayer);

        bundle.putBoolean(NEW_GAME_KEY, true);

        // Create new fragment and transaction
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        Fragment newFragment = new GameFragment();
        newFragment.setArguments(bundle);

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        ft.replace(R.id.contentContainer, newFragment, GamesFragment.GAME_FRAGMENT);
        ft.addToBackStack(null);

        // Commit the transaction
        ft.commit();
    }

    public void registerBackStackListener() {
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        actionBar.setDisplayHomeAsUpEnabled(true);
                    } else {
                        actionBar.setDisplayHomeAsUpEnabled(false);
                        toggle.syncState();
                    }
                }
            }
        });

    }

    private void openPlayerProfileFromNV() {
        item_id = 0;
        Bundle bundle = new Bundle();
        bundle.putParcelable(PlayerFragment.PLAYER_KEY, mPlayer);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment fragment = new PlayerFragment();
        fragment.setArguments(bundle);
        mFragment = fragment;
        ft.replace(R.id.contentContainer, fragment).commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        uncheckAllMenuItems(navigationView);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    private void uncheckAllMenuItems(NavigationView navigationView) {
        item_id = 0;
        final Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                SubMenu subMenu = item.getSubMenu();
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    subMenuItem.setChecked(false);
                }
            } else {
                item.setChecked(false);
            }
        }
    }

    public void enableNavigation() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void disableNavigation() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    private void initEditFab() {
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);

        // Create material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(editFab, sheetView, overlay,
                R.color.background_card, R.color.colorPrimary);

        // Set material sheet event listener
        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                // Save current status bar color
                statusBarColor = getStatusBarColor();
                // Set darker status bar color to match the dim overlay
                setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }

            @Override
            public void onHideSheet() {
                // Restore status bar color
                setStatusBarColor(statusBarColor);
            }
        });

        TextView edGameName = (TextView) findViewById(R.id.fab_sheet_item_name);
        TextView edGameWins = (TextView) findViewById(R.id.fab_sheet_item_win_score);
        TextView edGamePlay = (TextView) findViewById(R.id.fab_sheet_item_play);
        // Set material sheet item click listeners

        if (edGameName != null) {
            edGameName.setOnClickListener(this);
        }
        if (edGameWins != null) {
            edGameWins.setOnClickListener(this);
        }
        if (edGamePlay != null) {
            edGamePlay.setOnClickListener(this);
        }
    }

    public void showOkFab() {
//        if (okFab != null && !okFab.isShown()) {
//            okFab.show();
//        }
    }

    public void hideOkFab() {
//        if (okFab != null && okFab.isShown()) {
//            okFab.hide();
//        }
    }

    public void showEditFAB() {
        if (editFab != null && !editFab.isShown()) {
            editFab.show();
        }
    }

    public void hideEditFAB() {
        if (editFab != null && editFab.isShown()) {
            editFab.hide();
        }
    }

    public void showAddFab() {
        if (addFab != null && !addFab.isShown()) {
            addFab.show();
        }
    }

    public void hideAddFab() {
        if (addFab != null && addFab.isShown()) {
            addFab.hide();
        }
    }


    private int getStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getWindow().getStatusBarColor();
        }
        return 0;
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }
}
