package com.g_art.kickerapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.g_art.kickerapp.R;
import com.g_art.kickerapp.model.Game;
import com.g_art.kickerapp.model.Player;
import com.g_art.kickerapp.model.Team;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Kicker App
 * Created by G_Art on 21/2/2016.
 */
public class GamesViewAdapter extends RecyclerView.Adapter<GamesViewAdapter.ViewHolder>  {

    private List<Game> gameList;
    private Context context;

    public GamesViewAdapter(Context context, List<Game> gameList) {
        this.context = context;
        this.gameList = gameList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.game_card_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        if (null != gameList && !gameList.isEmpty()) {
            Game game = gameList.get(position);
            Player player1 = null;
            Player player2 = null;
            Player player3 = null;
            Player player4 = null;
            String score = "0:0";

            if (game.getTeams() == null || game.getTeams().isEmpty()) {
                List<Player> players = game.getPlayers();
                if (players != null && !players.isEmpty()) {
                    player1 = players.get(0);
                }
            } else {
                Team team1 = game.getTeams().get(0);
                Team team2 = game.getTeams().get(1);

                player1 = team1.getPlayerList().get(0);
                player2 = team1.getPlayerList().get(1);
                player3 = team2.getPlayerList().get(0);
                player4 = team2.getPlayerList().get(1);

                score = team1.getScores() + ":" + team2.getScores();
            }


            //Set Players details

            if (null != player1) {
                setPlayer(context, player1, holder.txtPlayer1Name, holder.imgPlayer1);
            }

            if (null != player2) {
                setPlayer(context, player2, holder.txtPlayer2Name, holder.imgPlayer3);
            }

            if (null != player3) {
                setPlayer(context, player3, holder.txtPlayer3Name, holder.imgPlayer3);
            }

            if (null != player4) {
                setPlayer(context, player4, holder.txtPlayer4Name, holder.imgPlayer4);
            }

            //Set Game Score
            holder.txtGameScore.setText(score);
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
                .fit().into(imgAvatar);
    }

    @Override
    public int getItemCount() {
        if (null == gameList || gameList.isEmpty()) {
            return 0;
        }
        return gameList.size();
    }

    public void clear(){
        if (this.gameList != null) {
            this.gameList.clear();
        }
    }

    public void addAll(List<Game> gameList) {
        if (this.gameList != null) {
            this.gameList.addAll(gameList);
        }
    }

    public Game getGameByPosition(int position) {
        if (null != gameList && !gameList.isEmpty()) {
            if (gameList.size() > position) {
                return gameList.get(position);
            }
        }

        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

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
        public ViewHolder(View itemView) {
            super(itemView);
            imgPlayer1 = (ImageView) itemView.findViewById(R.id.game_player1_avatar);
            txtPlayer1Name = (TextView) itemView.findViewById(R.id.game_player1_name);
            imgPlayer2 = (ImageView) itemView.findViewById(R.id.game_player2_avatar);
            txtPlayer2Name = (TextView) itemView.findViewById(R.id.game_player2_name);
            imgPlayer3 = (ImageView) itemView.findViewById(R.id.game_player3_avatar);
            txtPlayer3Name = (TextView) itemView.findViewById(R.id.game_player3_name);
            imgPlayer4 = (ImageView) itemView.findViewById(R.id.game_player4_avatar);
            txtPlayer4Name = (TextView) itemView.findViewById(R.id.game_player4_name);
            txtGameScore = (TextView) itemView.findViewById(R.id.game_score);
        }


    }
}
