package com.g_art.kickerapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Kicker App
 * Created by G_Art on 16/2/2016.
 */
public class Player implements Parcelable {
    private String _id;
    private String displayName;
    private String provider;
    private String providerId;
    private int games;
    private int wins;
    private int losses;
    private List<Achievement> achievements;

    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    public Player() {
    }

    public Player(String _id, String displayName) {
        this._id = _id;
        this.displayName = displayName;
    }

    public Player(String _id, String displayName, String provider, String providerId) {
        this._id = _id;
        this.displayName = displayName;
        this.provider = provider;
        this.providerId = providerId;
    }

    public Player(String _id, String displayName, String provider, String providerId, int games, int wins, int losses) {
        this._id = _id;
        this.displayName = displayName;
        this.provider = provider;
        this.providerId = providerId;
        this.games = games;
        this.wins = wins;
        this.losses = losses;
    }

    public Player(String _id, String displayName, String provider, String providerId, int games, int wins, int losses, List<Achievement> achievements) {
        this._id = _id;
        this.displayName = displayName;
        this.provider = provider;
        this.providerId = providerId;
        this.games = games;
        this.wins = wins;
        this.losses = losses;
        this.achievements = achievements;
    }

    public Player(Parcel in) {
        this._id = in.readString();
        this.displayName = in.readString();
        this.provider = in.readString();
        this.providerId = in.readString();
        this.games = in.readInt();
        this.wins = in.readInt();
        this.losses = in.readInt();
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(displayName);
        dest.writeString(provider);
        dest.writeString(providerId);
        dest.writeInt(games);
        dest.writeInt(wins);
        dest.writeInt(losses);
    }
}
