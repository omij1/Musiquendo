package com.example.mimo.musiquendo.Player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.mimo.musiquendo.BuildConfig;

import java.util.Objects;

/**
 * Clase que recibe los eventos que se producen en la notificación que muestra la canción que se está reproduciendo
 */

public class MyBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (Objects.requireNonNull(intent.getAction())) {
            case BuildConfig.STARTSTOP:
                startStopTrack();
                break;
            case BuildConfig.PREVIOUS:
                previousTrack();
                break;
            case BuildConfig.NEXT:
                nextTrack();
                break;
            default:
                deleteTrack();
                break;
        }
    }

    private void startStopTrack() {
        if (TrackPlayer.getPlayerInstance().isPlaying()) {
            TrackPlayer.getInstance().pausePlayer();
        }
        else {
            TrackPlayer.getInstance().resumePlayer();
        }
    }

    private void previousTrack() {

    }

    private void nextTrack() {

    }

    private void deleteTrack() {
        TrackPlayer.getInstance().deletePlayer();
    }
}
