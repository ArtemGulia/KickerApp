package com.g_art.kickerapp.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.g_art.kickerapp.R;

import jp.co.recruit_lifestyle.android.widget.BeerSwipeRefreshLayout;

/**
 * Kicker App
 * Created by G_Art on 19/2/2016.
 */
public class PlayerFragment extends Fragment {

    private View view;
    private BeerSwipeRefreshLayout mBeerSwipeRefreshLayout;

    public PlayerFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_player_profile, container, false);
        // keep the fragment and all its data across screen rotation
        setRetainInstance(true);


        mBeerSwipeRefreshLayout = (BeerSwipeRefreshLayout) view.findViewById(R.id.main_swipe);
        mBeerSwipeRefreshLayout.setOnRefreshListener(new BeerSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                new Task().execute();
            }
        });
        return view;
    }

    private class Task extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            return new String[0];
        }

        @Override protected void onPostExecute(String[] result) {
            // Call setRefreshing(false) when the list has been refreshed.

            mBeerSwipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(result);
        }
    }
}
