package com.g_art.kickerapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.g_art.kickerapp.R;
import com.g_art.kickerapp.adapter.GamesViewAdapter;
import com.g_art.kickerapp.model.Game;
import com.g_art.kickerapp.model.Player;
import com.g_art.kickerapp.model.Team;
import com.g_art.kickerapp.utils.api.GameApi;
import com.g_art.kickerapp.utils.rest.RestClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Kicker App
 * Created by G_Art on 19/2/2016.
 */
public class GamesFragment extends Fragment {

    private View view;

    private RecyclerView mRecyclerView;
    private GamesViewAdapter mAdapter;

    public GamesFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_games_list, container, false);
        // keep the fragment and all its data across screen rotation
        setRetainInstance(true);

        // 1. get a reference to recyclerView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_games_list);
        // 2. set layoutManger
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // 3. create an adapter
        mAdapter = new GamesViewAdapter(Collections.<Game>emptyList());
        // 4. set adapter
        mRecyclerView.setAdapter(mAdapter);
        // 5. set item animator to DefaultAnimator
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        requestForData();

        return view;
    }

    private void requestForData() {
        GameApi gameApi = RestClient.getGameApi();

        gameApi.getAllGames(new retrofit.Callback<List<Game>>() {
            @Override
            public void success(List<Game> games, Response response) {
                if (response != null) {
                    mAdapter.updateData(games);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (error != null) {
                    Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.d("Response", error.getResponse().toString());
                }
            }
        });
    }
}
