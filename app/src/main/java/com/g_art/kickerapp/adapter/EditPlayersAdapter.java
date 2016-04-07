package com.g_art.kickerapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.g_art.kickerapp.R;
import com.g_art.kickerapp.model.Player;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Kicker App
 * Created by G_Art on 4/4/2016.
 */
public class EditPlayersAdapter extends RecyclerView.Adapter<EditPlayersAdapter.ViewHolder> implements CompoundButton.OnCheckedChangeListener {

    private List<Player> playersList;
    private List<String> inGameIdList;
    private Context context;
    private boolean onBind;

    public EditPlayersAdapter(Context context, List<Player> playersList, List<String> inGameIdList) {
        this.context = context;
        this.playersList = playersList;
        this.inGameIdList = inGameIdList;
    }

    @Override
    public EditPlayersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.edit_player_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.onChangeListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EditPlayersAdapter.ViewHolder holder, int position) {
        if (playersList != null && !playersList.isEmpty()) {
            Player player = playersList.get(position);
            if (inGameIdList != null && !inGameIdList.isEmpty()) {
                onBind = true;
                if (!holder.swParticipate.isChecked() && inGameIdList.contains(player.get_id())) {
                    holder.swParticipate.toggle();
                } else if (holder.swParticipate.isChecked() && !inGameIdList.contains(player.get_id())) {
                    holder.swParticipate.toggle();
                }
                holder.swParticipate.setTag(position);
                onBind = false;
            }
            setPlayer(context, player, holder.plName, holder.plAvatar);
        }
    }

    private void onPlayerClick(int position) {
        Player player = playersList.get(position);
        if (inGameIdList.contains(player.get_id())) {
            inGameIdList.remove(player.get_id());
            notifyItemChanged(position);
        } else {
            if (inGameIdList.size() < 2) {
                inGameIdList.add(player.get_id());
               notifyItemChanged(position);
            } else {
                Toast.makeText(context, "Only 2 players in team", Toast.LENGTH_SHORT).show();
            }
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
        if (null == playersList || playersList.isEmpty()) {
            return 0;
        }
        return playersList.size();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Switch sw = (Switch) buttonView;
        if (sw != null) {
            Object obj = sw.getTag();
            if (null != obj && !onBind) {
                if (isChecked && inGameIdList.size() < 2) {
                    onPlayerClick((Integer) obj);
                } else if (isChecked && inGameIdList.size() > 1) {
                    buttonView.setChecked(false);
                    Toast.makeText(context, "Only 2 players in team", Toast.LENGTH_SHORT).show();
                } else if (!isChecked) {
                    onPlayerClick((Integer) obj);
                }
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout rlPlayerItem;
        public Switch swParticipate;
        public ImageView plAvatar;
        public TextView plName;

        public ViewHolder(View itemView) {
            super(itemView);
            rlPlayerItem = (RelativeLayout) itemView.findViewById(R.id.rl_player_item);
            swParticipate = (Switch) itemView.findViewById(R.id.participate);
            plAvatar = (ImageView) itemView.findViewById(R.id.edit_item_player_avatar);
            plName = (TextView) itemView.findViewById(R.id.edit_item_player_name);
        }

        public void onChangeListener(CompoundButton.OnCheckedChangeListener listener) {
            this.swParticipate.setOnCheckedChangeListener(listener);
        }
    }
}
