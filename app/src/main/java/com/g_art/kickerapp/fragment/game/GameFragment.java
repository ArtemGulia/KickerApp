package com.g_art.kickerapp.fragment.game;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.g_art.kickerapp.R;
import com.g_art.kickerapp.activity.KickerAppActivity;
import com.g_art.kickerapp.model.Game;
import com.g_art.kickerapp.model.Player;
import com.g_art.kickerapp.model.Team;
import com.g_art.kickerapp.services.GameService;
import com.g_art.kickerapp.utils.api.GameApi;
import com.g_art.kickerapp.utils.rest.RestClient;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Kicker App
 * Created by G_Art on 23/2/2016.
 */
public class GameFragment extends Fragment {

    private View view;
    private Player mPlayer;
    private boolean mIsNewGame;
    private String mGameId;
    private Game mGame;
    private ProgressBar mProgressBar;
    private GameViewHolder mGameViewHolder;
    private GameService mGameService;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_game_screen, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.game_screen_swipe);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        //Init game card
        mGameViewHolder = new GameViewHolder(view);

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            mPlayer = bundle.getParcelable(KickerAppActivity.PLAYER_KEY);
            mIsNewGame = bundle.getBoolean(KickerAppActivity.NEW_GAME_KEY);
            mGameId = bundle.getString(KickerAppActivity.GAME_KEY_ID);
        }

        if (mIsNewGame) {

        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            mGameViewHolder.setVisibility(View.GONE);
            requestForData();
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh game
                if (!mIsNewGame) {
                    requestForData();
                }
            }
        });

        return view;
    }

    private void requestForData() {
        GameApi gameApi = RestClient.getGameApi();

        Game tempGame = new Game(mGameId);
        gameApi.getGame(tempGame, new Callback<Game>() {
            @Override
            public void success(Game game, Response response) {
                updateUI(game);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateUI(Game game) {
        if (game != null) {
            mProgressBar.setVisibility(View.GONE);
            mGameViewHolder.setVisibility(View.VISIBLE);

            mGame = game;
            mGameViewHolder.setGame(getActivity(), mGame);

            mSwipeRefreshLayout.setRefreshing(false);
        }
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

        public void setGame(Context context, Game mGame) {
            if (mGame != null && mGame.getTeams() != null && !mGame.getTeams().isEmpty()) {
                List<Team> teams = mGame.getTeams();

                Team teamF = teams.get(0);
                setTeamF(context, teamF);
                int scoreF = teamF.getScores();
                int scoreS = 0;

                if (teams.size() > 1) {
                    Team teamS = teams.get(1);
                    scoreS = teamS.getScores();
                    setTeamS(context, teamS);
                }
                //Set Game Score
                String score = scoreF + ":" + scoreS;
                txtGameScore.setText(score);

            }
        }

        public void setTeamF(Context context, Team teamF) {
            if (teamF != null && teamF.getPlayerList() != null && !teamF.getPlayerList().isEmpty()) {
                List<Player> playersTeamF = teamF.getPlayerList();

                Player playerF = playersTeamF.get(0);
                setPlayerF(context, playerF);

                if (playersTeamF.size() > 1) {
                    Player playerS = playersTeamF.get(1);
                    setPlayerS(context, playerS);
                }
            }
        }

        public void setTeamS(Context context, Team teamS) {
            if (teamS != null && teamS.getPlayerList() != null && !teamS.getPlayerList().isEmpty()) {
                List<Player> playersTeamS = teamS.getPlayerList();

                Player playerT = playersTeamS.get(0);
                setPlayerT(context, playerT);

                if (playersTeamS.size() > 1) {
                    Player playerFo = playersTeamS.get(1);
                    setPlayerFo(context, playerFo);
                }
            }
        }

        public void setPlayerF(Context context, Player playerF) {
            if (playerF != null) {
                setPlayer(context, playerF, txtPlayer1Name, imgPlayer1);
            }
        }

        public void setPlayerS(Context context, Player playerS) {
            if (playerS != null) {
                setPlayer(context, playerS, txtPlayer2Name, imgPlayer2);

            }
        }

        public void setPlayerT(Context context, Player playerT) {
            if (playerT != null) {
                setPlayer(context, playerT, txtPlayer3Name, imgPlayer3);

            }
        }

        public void setPlayerFo(Context context, Player playerFo) {
            if (playerFo != null) {
                setPlayer(context, playerFo, txtPlayer4Name, imgPlayer4);

            }
        }


        private void setPlayer(Context context, Player player, TextView txtName, ImageView imgAvatar) {
            if (player == null || context == null) {
                return;
            }

            String avatarUrl = player.getImage();
            txtName.setText(player.getDisplayName());
            Picasso.with(context).load(avatarUrl)
                    .placeholder(R.drawable.account)
                    .error(R.drawable.ic_info_black_48px)
                    .fit()
                    .into(imgAvatar);
        }
    }
}
