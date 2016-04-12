package com.g_art.kickerapp.fragment.game;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.g_art.kickerapp.R;
import com.g_art.kickerapp.activity.KickerAppActivity;
import com.g_art.kickerapp.fragment.dialog.EditGameMaxScoreDialog;
import com.g_art.kickerapp.fragment.dialog.EditGameNameDialog;
import com.g_art.kickerapp.fragment.dialog.EditPlayersDialog;
import com.g_art.kickerapp.model.Game;
import com.g_art.kickerapp.model.GameState;
import com.g_art.kickerapp.model.Player;
import com.g_art.kickerapp.model.Team;
import com.g_art.kickerapp.services.GameService;
import com.g_art.kickerapp.services.impl.DTOTransformerImpl;
import com.g_art.kickerapp.services.impl.GameServiceImpl;
import com.g_art.kickerapp.services.transformer.DTOTransformer;
import com.g_art.kickerapp.utils.api.GameApi;
import com.g_art.kickerapp.utils.api.UserApi;
import com.g_art.kickerapp.utils.rest.RestClient;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Kicker App
 * Created by G_Art on 23/2/2016.
 */
public class GameFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {

    private static final String EDIT_PLAYERS_DIALOG = "editPlayersDialog";
    private static final String EDIT_NAME_DIALOG = "editNameDialog";
    private static final String EDIT_SCORE_DIALOG = "editScoreDialog";
    private static final int DIALOG_FRAGMENT = 1;
    private static final int GAME_NAME_REQUEST = 2;
    private static final int GAME_WIN_SCORE_REQUEST = 3;

    public static final String GAME_NAME = "game_name";
    public static final String GAME_SCORE = "game_score";

    private View view;

    private Player mPlayer;
    private boolean mIsNewGame;
    private String mGameId;
    private int tempClickId;
    private Game mGame;

    private ProgressBar mProgressBar;
    private ProgressDialog mProgressDialog;

    private GameViewHolder mGameViewHolder;
    private GameService mGameService;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton okFab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mGameService = new GameServiceImpl();
        view = inflater.inflate(R.layout.fragment_game_screen, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.game_screen_swipe);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        //Init game card
        mGameViewHolder = new GameViewHolder(view);
        mGameViewHolder.setOnClickListener(this);
        mGameViewHolder.setOnFocusChanged(this);

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            mPlayer = bundle.getParcelable(KickerAppActivity.PLAYER_KEY);
            mIsNewGame = bundle.getBoolean(KickerAppActivity.NEW_GAME_KEY);
            mGameId = bundle.getString(KickerAppActivity.GAME_KEY_ID);
        }

        if (mIsNewGame) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(false);

            Game newGame = mGameService.createGame(mPlayer);
            updateUI(newGame);
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

        ((KickerAppActivity) getActivity()).disableNavigation();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        okFab = (FloatingActionButton) getActivity().findViewById(R.id.okFab);
        if (okFab != null) {
            updateFabs();
            okFab.setOnClickListener(this);
        }
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
        super.onDestroy();
        hideOkFAB();
        ((KickerAppActivity) getActivity()).showAddFab();
        ((KickerAppActivity) getActivity()).enableNavigation();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.rl_team1:
                saveTempClickId(id);
                onTeamClick(mGame.getFTeam());
                break;

            case R.id.rl_team2:
                saveTempClickId(id);
                onTeamClick(mGame.getSTeam());
                break;

            case R.id.okFab:
                startTheGame();
                break;
        }
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();
        if (id == R.id.game_name && !hasFocus) {
            changeGameName(((TextInputEditText)v).getText().toString());
        } else if (id == R.id.game_win_score && !hasFocus) {
            String value = (((TextInputEditText)v).getText().toString());
            int newScore = Integer.parseInt(value);
            if (newScore != 0 && newScore <= 10) {
                changeGameWinScore(newScore);
            } else {
                ((TextInputEditText)v).setError(getResources().getString(R.string.edit_game_win_score_dialog_min_val));
            }
        }
    }

    public void changeGameName(String name) {
        if (mGame != null && name != null) {
            mGame.setName(name);
        }
    }

    public void changeGameWinScore(int newScore) {
        if (mGame != null && newScore != 0) {
            mGame.setWins(newScore);
        }
    }

    public void startTheGame() {
        if (mGame != null) {
            changeGameState(GameState.ACTIVE);
            saveGame();
        }
    }

    private void changeGameState(GameState state) {
        mGame.setState(state);
    }

    private void onTeamClick(Team team) {
        if (mIsNewGame) {
            getAvailablePlayers(mGame, team);
        } else {
            GameState gState = mGame.getState();
            switch (gState) {
                case ACTIVE:
                    int wScore = mGame.getWins();
                    int tScore = team.getScores();
                    if (tScore < wScore) {
                        addScore(team.get_id());
                    }
                    break;
                case CREATED:
                    getAvailablePlayers(mGame, team);
                    break;
                case READY:
                    getAvailablePlayers(mGame, team);
                    break;
                case FINISHED:
                    Toast.makeText(getActivity(), "Game is already finished", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void saveTempClickId(int clickId) {
        tempClickId = clickId;
    }

    private void addScore(String teamId) {
        GameApi gameApi = RestClient.getGameApi();
        Map<String, String> params = new HashMap<>();
        params.put("gameId", mGameId);
        params.put("teamId", teamId);
        gameApi.addScore(params, new Callback<Game>() {
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

    private void getAvailablePlayers(Game mGame, final Team team) {
        GameApi gameApi = RestClient.getGameApi();
        showProgressDialog();
        gameApi.getPlayersForTheGame(mGame, new Callback<List<Player>>() {
            @Override
            public void success(List<Player> players, Response response) {
                if (!players.isEmpty()) {
                    openPlayersDialog(players, team);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openPlayersDialog(List<Player> players, Team team) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        Fragment prev = getFragmentManager().findFragmentByTag(EDIT_PLAYERS_DIALOG);

        if (prev != null) {
            transaction.remove(prev);
        }

        transaction.addToBackStack(null);

        ArrayList<Player> aPlayers = new ArrayList<Player>(players);

        ArrayList<String> inGameIdList = new ArrayList<>(2);
        String teamId = null;
        if (team != null) {
            teamId = team.get_id();
            List<Player> tPlayers = team.getPlayers();
            if (tPlayers != null && !tPlayers.isEmpty()) {
                for (Player pl : tPlayers) {
                    aPlayers.add(0, pl);
                    inGameIdList.add(pl.get_id());
                }
            }
        }


        DialogFragment newFragment = EditPlayersDialog.newInstance(aPlayers, inGameIdList, teamId);
        newFragment.setTargetFragment(this, DIALOG_FRAGMENT);
        hideProgressDialog();
        newFragment.show(transaction, EDIT_PLAYERS_DIALOG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case DIALOG_FRAGMENT:
                if (resultCode == Activity.RESULT_OK) {
                    changePlayersPositiveClick(data);
                }
                break;
            case GAME_NAME_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    changeGameNamePositiveClick(data);
                }
                break;
            case GAME_WIN_SCORE_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    changeGameScorePositiveClick(data);
                }
                break;
        }
    }

    private void changeGameScorePositiveClick(Intent data) {
        if (mGame != null && data != null) {
            int newScore = data.getIntExtra(GAME_SCORE, 10);
            mGame.setWins(newScore);
            saveGame();
        }
    }

    private void changeGameNamePositiveClick(Intent data) {
        if (mGame != null && data != null) {
            String newName = data.getStringExtra(GAME_NAME);
            if (newName != null && !newName.isEmpty()) {
                mGame.setName(newName);
                saveGame();
            }
        }
    }

    private void changePlayersPositiveClick(Intent data) {
        if (data != null) {
            List<String> playersIds = data.getStringArrayListExtra(EditPlayersDialog.PLAYERS_ID_LIST);
            String teamId = data.getStringExtra(EditPlayersDialog.TEAM_ID);

            if (teamId == null) {
                //todo create new team and add players there
                getPlayers(playersIds, null);
            } else {
                //todo get this team from game and change players there
                getPlayers(playersIds, teamId);
            }

        }
    }

    private void getPlayers(List<String> playersIds, final String teamId) {
        UserApi userApi = RestClient.getUserApi();
        showProgressDialog();
        userApi.getPlayers(playersIds, new Callback<List<Player>>() {
            @Override
            public void success(List<Player> players, Response response) {
                if (players != null) {
                    changePlayers(players, teamId);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                hideProgressDialog();
            }
        });
    }

    private void changePlayers(List<Player> players, String teamId) {
        hideProgressDialog();
        if (mIsNewGame) {
            Player fPlayer = players.get(0);
            Player sPlayer = null;
            if (players.size() > 1) {
                sPlayer = players.get(1);
            }
            if (tempClickId == R.id.rl_team1) {
                mGameService.changePlayersInFTeam(mGame, fPlayer, sPlayer);
            } else {
                mGameService.changePlayersInSTeam(mGame, fPlayer, sPlayer);
            }
            updateUI(mGame);
        } else {
            List<Team> teams = mGame.getTeams();
            for (Team team : teams) {
                if (team.get_id().equals(teamId)) {
                    mGameService.changePlayersInTeam(mGame, team, players);
                }
            }
            saveGame();
        }
    }

    private void saveGame() {
        GameApi gameApi = RestClient.getGameApi();

        DTOTransformer tr = new DTOTransformerImpl();

        showProgressDialog();
        gameApi.updateGame(tr.transform(mGame), new Callback<Game>() {
            @Override
            public void success(Game game, Response response) {
                updateUI(game);
            }

            @Override
            public void failure(RetrofitError error) {
                hideProgressDialog();
                Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

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
        hideProgressDialog();
        if (game != null) {
            if (game.get_id() != null && !game.get_id().isEmpty()) {
                mIsNewGame = false;
                if (!mSwipeRefreshLayout.isEnabled()) {
                    mSwipeRefreshLayout.setEnabled(true);
                }
            }
            mProgressBar.setVisibility(View.GONE);
            mGameViewHolder.setVisibility(View.VISIBLE);

            mGame = game;
            mGameId = mGame.get_id();
            mGameViewHolder.setGame(getActivity(), mGame);

            mSwipeRefreshLayout.setRefreshing(false);
        }

        updateFabs();
    }

    private void updateFabs() {
        if (mIsNewGame) {
            showOkFAB();
            ((KickerAppActivity) getActivity()).hideAddFab();
        } else {
            if (mGame != null) {
                switch (mGame.getState()) {
                    case READY:
                    case CREATED:
                        hideOkFAB();
                        ((KickerAppActivity) getActivity()).hideAddFab();
                        break;
                    default:
                        hideOkFAB();
                        ((KickerAppActivity) getActivity()).showAddFab();
                        break;
                }
            } else {
                hideOkFAB();
                ((KickerAppActivity) getActivity()).showAddFab();
            }
        }
    }

    private void showOkFAB() {
        if (okFab != null && !okFab.isShown()) {
            okFab.show();
        }
    }

    private void hideOkFAB() {
        if (okFab != null && okFab.isShown()) {
            okFab.hide();
        }
    }

    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    public static class GameViewHolder {
        public LinearLayout game_sheet;
        public TextInputEditText txtGameName;
        public TextInputEditText txtGameWinScore;
        public TextView txtGameCreatedDate;
        public TextView txtGameStatus;

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
            game_sheet = (LinearLayout) itemView.findViewById(R.id.game_sheet);
            txtGameName = (TextInputEditText) itemView.findViewById(R.id.game_name);
            txtGameWinScore = (TextInputEditText) itemView.findViewById(R.id.game_win_score);
            txtGameCreatedDate = (TextView) itemView.findViewById(R.id.game_created_date);
            txtGameStatus = (TextView) itemView.findViewById(R.id.game_status);

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

        public void setOnFocusChanged(View.OnFocusChangeListener listener) {
            txtGameName.setOnFocusChangeListener(listener);
            txtGameWinScore.setOnFocusChangeListener(listener);
        }

        public void setVisibility(int visibility) {
            game_sheet.setVisibility(visibility);
            txtGameName.setVisibility(visibility);
            txtGameWinScore.setVisibility(visibility);
            txtGameCreatedDate.setVisibility(visibility);
            cv_game_team1.setVisibility(visibility);
            cv_game_team2.setVisibility(visibility);
            txtGameScore.setVisibility(visibility);
            txtGameVS.setVisibility(visibility);
        }

        public void setGame(Context context, Game mGame) {
            if (mGame != null) {
                setGameDate(mGame);

                setGameWinScore(mGame);

                setGameStatus(context, mGame);

                isEditable(mGame);

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

        private void setGameStatus(Context context, Game mGame) {
            GameState state = mGame.getState();
            String[] states = context.getResources().getStringArray(R.array.game_state);
            if (states != null && states.length == 4) {
                StringBuilder builder = new StringBuilder();
                builder.append("Game State: ").append(states[state.getStatus()]);
                txtGameStatus.setText(builder.toString());
            }
        }

        private void isEditable(Game mGame) {
            if (mGame.get_id() != null && !mGame.get_id().isEmpty()) {
                txtGameName.setEnabled(false);
                txtGameWinScore.setEnabled(false);
            }
        }

        private void setGameWinScore(Game mGame) {
            txtGameName.setText(mGame.getName());
            String winScore = Integer.toString(mGame.getWins());
            txtGameWinScore.setText(winScore);
        }

        private void setGameDate(Game mGame) {
            Date created = mGame.getDate();
            SimpleDateFormat format = new SimpleDateFormat("HH:MM, EEE, MMM d, ''yy");
            StringBuilder builder = new StringBuilder();
            builder.append("Date Created: ").append(format.format(created));
            txtGameCreatedDate.setText(builder.toString());
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
