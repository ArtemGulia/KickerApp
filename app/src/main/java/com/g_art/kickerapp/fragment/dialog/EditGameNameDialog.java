package com.g_art.kickerapp.fragment.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.g_art.kickerapp.R;
import com.g_art.kickerapp.fragment.game.GameFragment;

import java.util.ArrayList;

/**
 * Kicker App
 * Created by G_Art on 8/4/2016.
 */
public class EditGameNameDialog extends DialogFragment {
    private EditText edGameName;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.game_name_dialog, null);

        builder.setView(v);

        edGameName = (EditText) v.findViewById(R.id.game_name);
        String currName = getArguments().getString(GameFragment.GAME_NAME);
        if (currName != null && !currName.isEmpty()) {
            edGameName.setText(currName);
        }
        builder.setPositiveButton(R.string.edit_game_name_dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (edGameName.getText() != null) {
                    String newName = edGameName.getText().toString();
                    if (!newName.isEmpty()) {
                        Intent intent = new Intent();
                        intent.putExtra(GameFragment.GAME_NAME, newName);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    }
                }
            }
        }).setNegativeButton(R.string.edit_game_name_dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                negativeClick();
            }
        });

        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        negativeClick();
        super.onDismiss(dialog);
    }

    private void negativeClick() {
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
    }


    public static EditGameNameDialog newInstance(String currName) {
        EditGameNameDialog dialog = new EditGameNameDialog();

        Bundle args = new Bundle();
        args.putString(GameFragment.GAME_NAME, currName);
        dialog.setArguments(args);
        return dialog;
    }
}
