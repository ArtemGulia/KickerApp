package com.g_art.kickerapp.fragment.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.g_art.kickerapp.R;
import com.g_art.kickerapp.adapter.EditPlayersAdapter;
import com.g_art.kickerapp.fragment.game.GameFragment;
import com.g_art.kickerapp.model.Player;
import com.g_art.kickerapp.utils.RecyclerItemClickListener;

import java.util.ArrayList;

/**
 * Kicker App
 * Created by G_Art on 4/4/2016.
 */
public class EditPlayersDialog extends DialogFragment {
    public static final String PLAYERS_LIST = "playersList";
    public static final String PLAYERS_ID_LIST = "playersIdList";
    public static final String TEAM_ID = "teamId";

    private RecyclerView mRecyclerView;
    private EditPlayersAdapter mAdapter;
    private ArrayList<Player> playersList;
    private ArrayList<String> inGameIdList;
    private String teamId;

    private void onPlayerClick(int position) {
        Player player = playersList.get(position);
        if (inGameIdList.contains(player.get_id())) {
            inGameIdList.remove(player.get_id());
            mAdapter.notifyItemChanged(position);
        } else {
            if (inGameIdList.size() < 2) {
                inGameIdList.add(player.get_id());
                mAdapter.notifyItemChanged(position);
            } else {
                Toast.makeText(getActivity(), "Only 2 players in team", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = getActivity().getLayoutInflater().inflate(R.layout.edit_players_list, null);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.rv_edit_players);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            playersList = bundle.getParcelableArrayList(PLAYERS_LIST);
            inGameIdList = bundle.getStringArrayList(PLAYERS_ID_LIST);
            teamId = bundle.getString(TEAM_ID);
        }

        //set adapter
        mAdapter = new EditPlayersAdapter(getActivity(), playersList, inGameIdList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onPlayerClick(position);
            }
        }));



        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.account)
                .setTitle(R.string.players_edit_dialog_title)
                .setView(v)
                .setPositiveButton(R.string.players_edit_dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putStringArrayListExtra(PLAYERS_ID_LIST, inGameIdList);
                        intent.putExtra(TEAM_ID, teamId);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    }
                })
                .create();
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
    }

    public static DialogFragment newInstance(ArrayList<Player> players, ArrayList<String> inGameIdList, String teamId) {
        DialogFragment dialog = new EditPlayersDialog();

        Bundle args = new Bundle();
        args.putParcelableArrayList(PLAYERS_LIST, players);
        args.putStringArrayList(PLAYERS_ID_LIST, inGameIdList);
        args.putString(TEAM_ID, teamId);
        dialog.setArguments(args);
        return dialog;
    }
}
