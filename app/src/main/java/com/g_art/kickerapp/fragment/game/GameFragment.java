package com.g_art.kickerapp.fragment.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.g_art.kickerapp.R;
import com.g_art.kickerapp.activity.KickerAppActivity;
import com.g_art.kickerapp.model.Player;
import com.g_art.kickerapp.services.GameService;

/**
 * Kicker App
 * Created by G_Art on 23/2/2016.
 */
public class GameFragment extends Fragment {

    private View view;
    private Player mPlayer;
    private boolean mIsNewGame;
    private String mGameId;
    private ProgressBar mProgressBar;
    private GameViewHolder mGameViewHolder;
    private GameService mgameService;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_game_screen, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        //Init game card
        mGameViewHolder = new GameViewHolder(view);
        mGameViewHolder.setVisibility(View.GONE);

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            mPlayer = bundle.getParcelable(KickerAppActivity.PLAYER_KEY);
            mIsNewGame = bundle.getBoolean(KickerAppActivity.NEW_GAME_KEY);
            mGameId = bundle.getString(KickerAppActivity.GAME_KEY_ID);
        }



        return view;
    }

    public static class GameViewHolder {

        public CardView gameCardView;

        public ImageView imgPlayer1;
        public TextView txtPlayer1Name;

        public ImageView imgPlayer2;
        public TextView txtPlayer2Name;

        public ImageView imgPlayer3;
        public TextView txtPlayer3Name;

        public ImageView imgPlayer4;
        public TextView txtPlayer4Name;

        public TextView txtGameScore;

        // inner class to hold a reference to each item of RecyclerView
        public GameViewHolder(View itemView) {
            gameCardView = (CardView) itemView.findViewById(R.id.cv_game);

            imgPlayer1 = (ImageView) itemView.findViewById(R.id.cv_game_player1_avatar);
            txtPlayer1Name = (TextView) itemView.findViewById(R.id.cv_game_player1_name);

            imgPlayer2 = (ImageView) itemView.findViewById(R.id.cv_game_player2_avatar);
            txtPlayer2Name = (TextView) itemView.findViewById(R.id.cv_game_player2_name);

            imgPlayer3 = (ImageView) itemView.findViewById(R.id.cv_game_player3_avatar);
            txtPlayer3Name = (TextView) itemView.findViewById(R.id.cv_game_player3_name);

            imgPlayer4 = (ImageView) itemView.findViewById(R.id.cv_game_player4_avatar);
            txtPlayer4Name = (TextView) itemView.findViewById(R.id.cv_game_player4_name);

            txtGameScore = (TextView) itemView.findViewById(R.id.cv_game_score);
        }

        public void setVisibility(int visibility) {
            gameCardView.setVisibility(visibility);
        }

    }
}
