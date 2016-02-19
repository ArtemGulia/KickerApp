package com.g_art.kickerapp.utils;

import android.content.SharedPreferences;

import java.util.Map;

/**
 * Kicker App
 * Created by G_Art on 18/2/2016.
 */
public class SharedPrefsHandler {

    public static final String LOGIN_PROVIDER = "provider";
    public static final String PROVIDER_ID = "providerId";
    public static final String LOGGED = "logged";
    public static final int IS_LOGGED = 1;
    public static final int NOT_LOGGED = 0;
    public static final String PLAYER_ID = "playerId";
    public static final String PLAYER_NAME = "displayName";
    public static final String TOKEN_ID = "tokenId";


    private static SharedPrefsHandler handlerInstance;

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor prefsEditor;

    private SharedPrefsHandler(SharedPreferences mPrefs) {
        this.mPrefs = mPrefs;
    }

    public static SharedPrefsHandler getInstance(SharedPreferences mPrefs) {
        if (handlerInstance == null) {
            handlerInstance = new SharedPrefsHandler(mPrefs);
        } else {
            handlerInstance.setSharedPreferences(mPrefs);
        }
        return handlerInstance;
    }

    public boolean isLogged() {
        if (!mPrefs.contains(LOGGED)) {
            save(LOGGED, NOT_LOGGED);
        }
        int value = mPrefs.getInt(LOGGED, NOT_LOGGED);
        return value == IS_LOGGED;
    }

    public void save(String key, String value) {
        prefsEditor = mPrefs.edit();
        prefsEditor.putString(key, value);
        try {
            prefsEditor.apply();
        } catch (Exception ex) {
            //TODO log and toast(return boolean)
        }
    }

    public void save(String key, int value) {
        prefsEditor = mPrefs.edit();
        prefsEditor.putInt(key, value);
        try {
            prefsEditor.apply();
        } catch (Exception ex) {
            //TODO log and toast(return boolean)
        }
    }

    public String get(String key, String defValue) {
        if (!mPrefs.contains(key)) {
            save(key, defValue);
        }
        return mPrefs.getString(key, defValue);
    }

    public int get(String key, int defValue) {
        if (!mPrefs.contains(key)) {
            save(key, defValue);
        }
        return mPrefs.getInt(key, defValue);
    }

    public Map<String, ?> getAll() {
       return mPrefs.getAll();
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.mPrefs = sharedPreferences;
    }
}
