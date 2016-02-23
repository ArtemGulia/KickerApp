package com.g_art.kickerapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.g_art.kickerapp.R;
import com.g_art.kickerapp.model.Game;
import com.g_art.kickerapp.model.Team;

import java.util.List;

/**
 * Kicker App
 * Created by G_Art on 21/2/2016.
 */
public class GamesViewAdapter extends RecyclerView.Adapter<GamesViewAdapter.ViewHolder>  {

    private List<Game> gameList;

    public GamesViewAdapter(List<Game> gameList) {
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

            //Set Players details
            holder.txtPlayer1Name.setText(team1.getPlayerList().get(0).getDisplayName());
            holder.txtPlayer2Name.setText(team1.getPlayerList().get(1).getDisplayName());
            holder.txtPlayer3Name.setText(team2.getPlayerList().get(0).getDisplayName());
            holder.txtPlayer4Name.setText(team2.getPlayerList().get(1).getDisplayName());

            //Set Game Score
            String score = team1.getScore() + ":" + team2.getScore();
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
