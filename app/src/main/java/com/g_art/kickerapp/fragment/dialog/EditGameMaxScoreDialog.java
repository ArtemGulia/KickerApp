package com.g_art.kickerapp.fragment.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ObbInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.g_art.kickerapp.R;
import com.g_art.kickerapp.fragment.game.GameFragment;

/**
 * Kicker App
 * Created by G_Art on 8/4/2016.
 */
public class EditGameMaxScoreDialog extends DialogFragment {
    private EditText edGameScore;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.game_win_score_dialog, null);

        builder.setView(v);

        edGameScore = (EditText) v.findViewById(R.id.game_win_score);
        Integer currScore = getArguments().getInt(GameFragment.GAME_SCORE);
        edGameScore.setText(currScore.toString());

        builder.setPositiveButton(R.string.edit_game_name_dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (edGameScore.getText() != null) {
                            String value = edGameScore.getText().toString();
                            int newScore = Integer.parseInt(value);
                            if (newScore != 0) {
                                Intent intent = new Intent();
                                intent.putExtra(GameFragment.GAME_SCORE, newScore);
                                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                            } else {
                                Toast.makeText(getActivity(), R.string.edit_game_win_score_dialog_min_val, Toast.LENGTH_SHORT).show();
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


    public static EditGameMaxScoreDialog newInstance(int currScore) {
        EditGameMaxScoreDialog dialog = new EditGameMaxScoreDialog();

        Bundle args = new Bundle();
        args.putInt(GameFragment.GAME_SCORE, currScore);
        dialog.setArguments(args);
        return dialog;
    }
}
