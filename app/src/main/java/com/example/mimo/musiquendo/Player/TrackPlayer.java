package com.example.mimo.musiquendo.Player;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

/**
 * Singleton que permite reproducir las cancionesw de la aplicación.
 */

public class TrackPlayer extends Service {

    private MediaPlayer player;
    private int position;
    private NotificationBuilder notif;

    public TrackPlayer() {}


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        enableForegroundMode();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disableForegroundMode();
        if (player!= null) {
            player.stop();
            player = null;
        }
        TrackQueue.SECTION = "";
        TrackQueue.getInstance().deleteQueue();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread musicThread = new Thread(() -> {
            if (player == null)
                player = new MediaPlayer();
            showNotification();
            playStreamTrack();
        });
        musicThread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    private void enableForegroundMode() {
        showNotification();
    }

    private void disableForegroundMode() {
        stopForeground(true);
    }

    /**
     * Método que muestra la notificación y actualiza su contenido acorde a la canción que está sonando
     */
    private void showNotification() {
        String trackName = TrackQueue.getTrackQueue().get(0).getTrackName();
        String artist = TrackQueue.getTrackQueue().get(0).getInfo();
        String minutes = TrackQueue.getTrackQueue().get(0).getDurationInfo();
        notif = new NotificationBuilder(getApplicationContext(), trackName, artist, minutes);
        startForeground(NotificationBuilder.ID, notif.showNotification());
    }

    /**
     * Método que carga el Mediaplayer
     */
    private void playStreamTrack() {
        if (player.isPlaying()) {
            resetPlayer();
        }
        startPlayer();
    }

    /**
     * Método que inicia la reproducción de la canción
     */
    private void startPlayer() {
        try {
            player.setDataSource(TrackQueue.getTrackQueue().get(0).getStreamUrl());
            player.prepareAsync();
            player.setOnPreparedListener(mediaPlayer -> {
                player.start();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que pausa la reproducción de la canción
     */
    public void pausePlayer() {
        player.pause();
        position = player.getCurrentPosition();
    }

    /**
     * Método que reanuda la reproducción de la canción
     */
    public void resumePlayer() {
        player.seekTo(position);
        player.start();
    }

    /**
     * Método que resetea el Mediaplayer para cambiar de canción
     */
    private void resetPlayer() {
        player.stop();
        player.reset();
    }

    /**
     * Método que elimina la instancia del objeto Mediaplayer
     */
    public void deletePlayer() {
        if (player.isPlaying())
            player.stop();
        player.release();
        player = null;
    }
}

