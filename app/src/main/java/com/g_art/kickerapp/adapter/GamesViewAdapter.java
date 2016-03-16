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
            Team team1 = game.getTeams().get(0);
            Team team2 = game.getTeams().get(1);

            Player player1 = team1.getPlayerList().get(0);
            Player player2 = team1.getPlayerList().get(1);
            Player player3 = team2.getPlayerList().get(0);
            Player player4 = team2.getPlayerList().get(1);

            //Set Players details
            holder.txtPlayer1Name.setText(player1.getDisplayName());
            String pl1Url = player1.getImage();
            Picasso.with(context)
                    .setIndicatorsEnabled(true);
            if (null != pl1Url && !pl1Url.isEmpty()) {
                Picasso.with(context).load(pl1Url)
                        .placeholder(R.drawable.account)
                        .error(R.drawable.ic_info_black_48px)
                        .fit().into(holder.imgPlayer1);
            }
            holder.txtPlayer2Name.setText(player2.getDisplayName());
            String pl2Url = player2.getImage();
            if (null != pl2Url && !pl2Url.isEmpty()) {
                Picasso.with(context).load(pl2Url)
                        .placeholder(R.drawable.account)
                        .error(R.drawable.ic_info_black_48px)
                        .fit().into(holder.imgPlayer2);
            }
            holder.txtPlayer3Name.setText(player3.getDisplayName());
            String pl3Url = player3.getImage();
            if (null != pl3Url && !pl3Url.isEmpty()) {
                Picasso.with(context).load(pl3Url)
                        .placeholder(R.drawable.account)
                        .error(R.drawable.ic_info_black_48px)
                        .fit().into(holder.imgPlayer3);
            }
            holder.txtPlayer4Name.setText(player4.getDisplayName());
            String pl4Url = player4.getImage();
            if (null != pl4Url && !pl4Url.isEmpty()) {
                Picasso.with(context).load(pl4Url)
                        .placeholder(R.drawable.account)
                        .error(R.drawable.ic_info_black_48px)
                        .fit().into(holder.imgPlayer4);
            }

            //Set Game Score
            String score = team1.getScores() + ":" + team2.getScores();
            holder.txtGameScore.setText(score);
        }
    }

    @Override
    public int getItemCount() {
        if (null == gameList || gameList.isEmpty()) {
            return 0;
        }
        return gameList.size();
    }

    public void updateData(List<Game> gameList) {
        this.gameList = gameList;
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
