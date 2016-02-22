package com.g_art.kickerapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.g_art.kickerapp.R;
import com.g_art.kickerapp.adapter.GamesViewAdapter;
import com.g_art.kickerapp.model.Game;
import com.g_art.kickerapp.model.Player;
import com.g_art.kickerapp.model.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Kicker App
 * Created by G_Art on 19/2/2016.
 */
public class GamesFragment extends Fragment {

    private View view;

    private RecyclerView mRecyclerView;
    private GamesViewAdapter mGamesViewAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    public GamesFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_games_list, container, false);

        // Set some data

        List<Game> itemsData = new ArrayList<>();

        for (int i = 0; i < 20; i++ ) {
            Game game = new Game();
            List<Team> teams = new ArrayList<>();
            for (int y = 0; y < 2; y++) {
                Team team = new Team();
                List<Player> teamPlayers = new ArrayList<>();
                for (int j = 0; j < 2; j ++) {
                    Player player = new Player("id", "player"+y+""+j);
                    teamPlayers.add(player);
                }
                team.setPlayerList(teamPlayers);
                teams.add(team);
            }
            if (i<=10) {
                game.setScore("0:"+i);
            } else {
                game.setScore(""+i+":0");
            }
            game.setTeams(teams);
            itemsData.add(game);
        }


        // 1. get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_games_list);
        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // 3. create an adapter
        GamesViewAdapter mAdapter = new GamesViewAdapter(itemsData);
        // 4. set adapter
        recyclerView.setAdapter(mAdapter);
        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }
}
