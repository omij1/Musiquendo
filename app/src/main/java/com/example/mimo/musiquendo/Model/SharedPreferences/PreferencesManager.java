package com.example.mimo.musiquendo.Model.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mimo.musiquendo.R;


/**
 * Clase que se encarga de la gestión del fichero de preferencias de la aplicación
 */

public class PreferencesManager {

    private Context mContext;
    private static final String SHARED_PREFS_FILE = "MusiquendoSettings";
    private static final String PLAYLIST_MODE = "playlist_mode";
    private static final String DISPLAY = "display_mode";
    private static final String DOWNLOAD = "download_settings";


    public PreferencesManager(Context context) {
        this.mContext = context;
    }


    private SharedPreferences getPreferences() {
        return mContext.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
    }


    public boolean getPlaylistMode() {
        return getPreferences().getBoolean(PLAYLIST_MODE, true);
    }


    public void setPlaylistMode(boolean mode) {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putBoolean(PLAYLIST_MODE, mode);
        edit.apply();
    }


    public String getDisplayMode() {
        return getPreferences().getString(DISPLAY, mContext.getString(R.string.grid));
    }


    public void setDisplayMode(String display) {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putString(DISPLAY, display);
        edit.apply();
    }


    public boolean getDownloadSettings() {
        return getPreferences().getBoolean(DOWNLOAD, true);
    }


    public void setDownloadSettings(boolean mode) {
        SharedPreferences.Editor edit = getPreferences().edit();
        edit.putBoolean(DOWNLOAD, mode);
        edit.apply();
    }
}
