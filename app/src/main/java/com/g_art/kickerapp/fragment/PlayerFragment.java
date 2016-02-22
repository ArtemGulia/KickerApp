package com.g_art.kickerapp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.g_art.kickerapp.R;

/**
 * Kicker App
 * Created by G_Art on 19/2/2016.
 */
public class PlayerFragment extends Fragment {

    private View view;

    public PlayerFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_player_profile, container, false);
        return view;
    }
}
