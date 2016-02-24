package com.g_art.kickerapp.fragment;


import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.g_art.kickerapp.R;
import com.g_art.kickerapp.model.Player;
import com.g_art.kickerapp.utils.api.UserApi;
import com.g_art.kickerapp.utils.prefs.SharedPrefsHandler;
import com.g_art.kickerapp.utils.rest.RestClient;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;

/**
 * Kicker App
 * Created by G_Art on 19/2/2016.
 */
public class PlayerFragment extends Fragment {

    public static final String PLAYER_KEY = "player";
    private View view;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Player mPlayer;
    private TextView mTxtPlayerName;
    private TextView mTxtGames;
    private TextView mTxtWins;
    private TextView mTxtLosses;
    private ImageView mPlayerAvatar;

    public PlayerFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_player_profile, container, false);
        // keep the fragment and all its data across screen rotation
        setRetainInstance(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.player_profile_swipe);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                loadPlayer();
            }
        });

        mTxtPlayerName = (TextView) view.findViewById(R.id.txtPlayerNameValue);
        mTxtGames = (TextView) view.findViewById(R.id.txtGamesValue);
        mTxtWins = (TextView) view.findViewById(R.id.txtWinsValue);
        mTxtLosses = (TextView) view.findViewById(R.id.txtLossesValue);
        mPlayerAvatar = (ImageView) view.findViewById(R.id.playerAvatar);

        if (getArguments() != null) {
            mPlayer = getArguments().getParcelable(PLAYER_KEY);
            updateUI();
        }

        if (null == mPlayer) {
            loadPlayer();
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mPlayer = getArguments().getParcelable(PLAYER_KEY);
            updateUI();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's state here
        outState.putParcelable(PLAYER_KEY, mPlayer);
    }

    private void loadPlayer() {
        UserApi userApi = RestClient.getUserApi();
        userApi.authUser(new Callback<Player>() {
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

                mPlayer = player;
                onItemsLoadComplete();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void updateUI() {
        if (mPlayer != null) {
            String avatarUrl = mPlayer.getImage();
            if (null != avatarUrl && !avatarUrl.isEmpty()) {
                Picasso.with(getActivity()).load(avatarUrl)
                        .placeholder(R.drawable.account)
                        .error(R.drawable.ic_info_black_48px)
                        .fit().into(mPlayerAvatar);
            }
            mTxtPlayerName.setText(mPlayer.getDisplayName());
            mTxtGames.setText(""+mPlayer.getGames());
            mTxtWins.setText(""+mPlayer.getWins());
            mTxtLosses.setText(""+mPlayer.getLosses());
        }
    }

    void onItemsLoadComplete() {
        // Update the UI and notify data set changed
        updateUI();
        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
