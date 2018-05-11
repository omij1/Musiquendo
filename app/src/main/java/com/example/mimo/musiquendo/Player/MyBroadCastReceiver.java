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

    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
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
        Intent startStop = new Intent(mContext, TrackPlayer.class);
        startStop.setAction(BuildConfig.STARTSTOP);
        mContext.startService(startStop);
    }

    private void previousTrack() {
        Intent prev = new Intent(mContext, TrackPlayer.class);
        prev.setAction(BuildConfig.PREVIOUS);
        mContext.startService(prev);
    }

    private void nextTrack() {
        Intent next = new Intent(mContext, TrackPlayer.class);
        next.setAction(BuildConfig.NEXT);
        mContext.startService(next);
    }

    private void deleteTrack() {
        Intent delete = new Intent(mContext, TrackPlayer.class);
        delete.setAction(BuildConfig.DELETE);
        mContext.startService(delete);
    }
}
