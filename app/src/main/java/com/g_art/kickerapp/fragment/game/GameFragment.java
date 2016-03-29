package com.g_art.kickerapp.fragment.game;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.g_art.kickerapp.R;
import com.g_art.kickerapp.activity.KickerAppActivity;
import com.g_art.kickerapp.model.Game;
import com.g_art.kickerapp.model.Player;
import com.g_art.kickerapp.model.Team;
import com.g_art.kickerapp.services.GameService;
import com.g_art.kickerapp.utils.api.GameApi;
import com.g_art.kickerapp.utils.rest.RestClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Kicker App
 * Created by G_Art on 23/2/2016.
 */
public class GameFragment extends Fragment implements View.OnClickListener {

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
        mGameViewHolder.setOnClickListener(this);

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            mPlayer = bundle.getParcelable(KickerAppActivity.PLAYER_KEY);
            mIsNewGame = bundle.getBoolean(KickerAppActivity.NEW_GAME_KEY);
            mGameId = bundle.getString(KickerAppActivity.GAME_KEY_ID);
        }

        if (mIsNewGame) {
            ((KickerAppActivity) getActivity()).hideAddFab();
            ((KickerAppActivity) getActivity()).showOkFab();
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(false);

            Game newGame = createGame(mPlayer);
            updateUI(newGame);
        } else {
            ((KickerAppActivity) getActivity()).hideOkFab();
            ((KickerAppActivity) getActivity()).showAddFab();
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

        ((KickerAppActivity) getActivity()).disableNavigation();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ActionBar actionBar = ((KickerAppActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.game_screen);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    public void onDestroy() {
        ((KickerAppActivity) getActivity()).hideOkFab();
        ((KickerAppActivity) getActivity()).showAddFab();
        ((KickerAppActivity) getActivity()).enableNavigation();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.rl_team1:
                showPlayerInfo(mGame.getTeams().get(0).getPlayers().get(0));
                break;

            case R.id.rl_team2:
                showPlayerInfo(mGame.getTeams().get(1).getPlayers().get(0));
                break;

            case R.id.cv_game_team1:
                showPlayerInfo(mGame.getTeams().get(0).getPlayers().get(0));
                break;

            case R.id.cv_game_team2:
                showPlayerInfo(mGame.getTeams().get(1).getPlayers().get(0));
                break;

            case R.id.mr_game_team1:
                showPlayerInfo(mGame.getTeams().get(0).getPlayers().get(0));
                break;

            case R.id.mr_game_team2:
                showPlayerInfo(mGame.getTeams().get(1).getPlayers().get(0));
                break;
        }
    }

    private Game createGame(Player player) {
        if (player != null) {
            Game game = new Game();

            game.setCreatedBy(player);
            game.addPlayer(player);

            Team team_1 = new Team();
            team_1.addPlayer(player);
            Team team_2 = new Team();
            List<Team> teamList = new ArrayList<>();

            teamList.add(team_1);
            teamList.add(team_2);

            game.setTeams(teamList);
            return game;
        } else {
            return null;
        }
    }

    private void showPlayerInfo(Player player) {
        if (null != player) {
            Toast.makeText(getActivity(), player.getDisplayName(), Toast.LENGTH_SHORT).show();
        }
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

        public CardView cv_game_team1;
        public CardView cv_game_team2;

        public MaterialRippleLayout mr_game_team1;
        public MaterialRippleLayout mr_game_team2;

        public ImageView imgPlayer1;
        public TextView txtPlayer1Name;

        public ImageView imgPlayer2;
        public TextView txtPlayer2Name;

        public ImageView imgPlayer3;
        public TextView txtPlayer3Name;

        public ImageView imgPlayer4;
        public TextView txtPlayer4Name;

        public TextView txtGameScore;
        public TextView txtGameVS;

        // inner class to hold a reference to each item of RecyclerView
        public GameViewHolder(View itemView) {
            cv_game_team1 = (CardView) itemView.findViewById(R.id.cv_game_team1);
            cv_game_team2 = (CardView) itemView.findViewById(R.id.cv_game_team2);
            mr_game_team1 = (MaterialRippleLayout) itemView.findViewById(R.id.mr_game_team1);
            mr_game_team2 = (MaterialRippleLayout) itemView.findViewById(R.id.mr_game_team2);

            imgPlayer1 = (ImageView) itemView.findViewById(R.id.cv_game_player1_avatar);
            txtPlayer1Name = (TextView) itemView.findViewById(R.id.cv_game_player1_name);

            imgPlayer2 = (ImageView) itemView.findViewById(R.id.cv_game_player2_avatar);
            txtPlayer2Name = (TextView) itemView.findViewById(R.id.cv_game_player2_name);

            imgPlayer3 = (ImageView) itemView.findViewById(R.id.cv_game_player3_avatar);
            txtPlayer3Name = (TextView) itemView.findViewById(R.id.cv_game_player3_name);

            imgPlayer4 = (ImageView) itemView.findViewById(R.id.cv_game_player4_avatar);
            txtPlayer4Name = (TextView) itemView.findViewById(R.id.cv_game_player4_name);

            txtGameVS = (TextView) itemView.findViewById(R.id.cv_game_vs);
            txtGameScore = (TextView) itemView.findViewById(R.id.cv_game_score);
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            cv_game_team1.setOnClickListener(onClickListener);
            cv_game_team2.setOnClickListener(onClickListener);
            mr_game_team1.setOnClickListener(onClickListener);
            mr_game_team2.setOnClickListener(onClickListener);
        }

        public void setVisibility(int visibility) {
            cv_game_team1.setVisibility(visibility);
            cv_game_team2.setVisibility(visibility);
            txtGameScore.setVisibility(visibility);
            txtGameVS.setVisibility(visibility);
        }

        public void setGame(Context context, Game mGame) {
            if (mGame != null) {
                if (mGame.getTeams() != null && !mGame.getTeams().isEmpty()) {
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
                } else {
                    List<Player> players = mGame.getPlayers();
                    if (players != null && !players.isEmpty()) {
                        Player playerF = players.get(0);
                        setPlayerF(context, playerF);

                        if (players.size() > 1) {
                            Player playerS = players.get(1);
                            setPlayerS(context, playerS);
                            if (players.size() > 2) {
                                Player playerT = players.get(2);
                                setPlayerT(context, playerT);
                            }
                        }
                    }
                }
            }
        }

        public void setTeamF(Context context, Team teamF) {
            if (teamF != null && teamF.getPlayers() != null && !teamF.getPlayers().isEmpty()) {
                List<Player> playersTeamF = teamF.getPlayers();

                Player playerF = playersTeamF.get(0);
                setPlayerF(context, playerF);

                if (playersTeamF.size() > 1) {
                    Player playerS = playersTeamF.get(1);
                    setPlayerS(context, playerS);
                }
            }
        }

        public void setTeamS(Context context, Team teamS) {
            if (teamS != null && teamS.getPlayers() != null && !teamS.getPlayers().isEmpty()) {
                List<Player> playersTeamS = teamS.getPlayers();

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
