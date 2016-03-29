package com.g_art.kickerapp.fragment.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.g_art.kickerapp.R;
import com.g_art.kickerapp.activity.KickerAppActivity;
import com.g_art.kickerapp.adapter.GamesViewAdapter;
import com.g_art.kickerapp.fragment.PlayerFragment;
import com.g_art.kickerapp.model.Game;
import com.g_art.kickerapp.model.Player;
import com.g_art.kickerapp.utils.RecyclerItemClickListener;
import com.g_art.kickerapp.utils.api.GameApi;
import com.g_art.kickerapp.utils.rest.RestClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Kicker App
 * Created by G_Art on 19/2/2016.
 */
public class GamesFragment extends Fragment {

    public static final String GAME_FRAGMENT = "game_fragment";
    private View view;

    private RecyclerView mRecyclerView;
    private GamesViewAdapter mAdapter;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Player mPlayer;

    public GamesFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_games_list, container, false);
        // keep the fragment and all its data across screen rotation
        setRetainInstance(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.game_list_swipe);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                requestForData();
            }
        });

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            mPlayer = bundle.getParcelable(PlayerFragment.PLAYER_KEY);
        }

        // 1. get a reference to recyclerView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_games_list);
        // 2. set layoutManger
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // 3. create an adapter
        mAdapter = new GamesViewAdapter(getActivity(), new ArrayList<Game>());
        // 4. set adapter
        mRecyclerView.setAdapter(mAdapter);
        // 5. set item animator to DefaultAnimator
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                openGameFragment(position);
            }
        }));

        mRecyclerView.setVisibility(View.GONE);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar_game_list);

        requestForData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ActionBar actionBar = ((KickerAppActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.games_screen);
        }
    }

    private void openGameFragment(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KickerAppActivity.PLAYER_KEY, mPlayer);

        Game selectedGame = mAdapter.getGameByPosition(position);
        boolean isNewGame = selectedGame == null;
        bundle.putBoolean(KickerAppActivity.NEW_GAME_KEY, isNewGame);

        if (selectedGame != null) {
            bundle.putString(KickerAppActivity.GAME_KEY_ID, selectedGame.get_id());
        }

        // Create new fragment and transaction
        Fragment newFragment = new GameFragment();
        newFragment.setArguments(bundle);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.contentContainer, newFragment, GAME_FRAGMENT);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private void requestForData() {
        GameApi gameApi = RestClient.getGameApi();

        gameApi.getAllGames(new Callback<List<Game>>() {
            @Override
            public void success(List<Game> games, Response response) {
                if (response != null) {
                    mAdapter.clear();
                    mAdapter.addAll(games);
                    onItemsLoadComplete();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (error != null) {
                    onItemsLoadComplete();
                    Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void onItemsLoadComplete() {
        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
        mProgressBar.setVisibility(View.GONE);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setVisibility(View.VISIBLE);
    }
}
