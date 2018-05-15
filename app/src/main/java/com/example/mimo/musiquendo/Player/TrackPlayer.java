package com.example.mimo.musiquendo.Player;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.mimo.musiquendo.BuildConfig;

import java.io.IOException;
import java.util.Objects;

/**
 * Singleton que permite reproducir las cancionesw de la aplicación.
 */

public class TrackPlayer extends Service implements MediaPlayer.OnCompletionListener{

    private MediaPlayer player;
    private int position;

    public TrackPlayer() {}


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (player == null)
            player = new MediaPlayer();
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
        switch (Objects.requireNonNull(intent.getAction())){
            case BuildConfig.PLAY:
                Thread musicThread = new Thread(() -> {
                    if (player == null)
                        player = new MediaPlayer();
                    else
                        resetPlayer();
                    showNotification();
                    playStreamTrack();
                });
                musicThread.start();
                break;
            case BuildConfig.STARTSTOP:
                if (player.isPlaying())
                    pausePlayer();
                else
                    resumePlayer();
                break;
            case BuildConfig.NEXT:
                nextTrack();
                break;
            case BuildConfig.PREVIOUS:
                previousTrack();
                break;
            case BuildConfig.DELETE:
                disableForegroundMode();
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void enableForegroundMode() {
        showNotification();
    }

    private void disableForegroundMode() {
        if (player != null) {
            player.release();
            player = null;
        }
        stopForeground(true);
    }

    /**
     * Método que muestra la notificación y actualiza su contenido acorde a la canción que está sonando
     */
    private void showNotification() {
        String trackName = TrackQueue.getTrackQueue().get(TrackQueue.currentTrack).getTrackName();
        String artist = TrackQueue.getTrackQueue().get(TrackQueue.currentTrack).getInfo();
        String minutes = TrackQueue.getTrackQueue().get(TrackQueue.currentTrack).getDurationInfo();
        NotificationBuilder notif = new NotificationBuilder(getApplicationContext(), trackName, artist, minutes);
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
            player.setDataSource(TrackQueue.getTrackQueue().get(TrackQueue.currentTrack).getStreamUrl());
            player.prepareAsync();
            player.setOnPreparedListener(mediaPlayer -> {
                player.start();
                player.setOnCompletionListener(this);
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
        stopForeground(false);
    }

    /**
     * Método que reanuda la reproducción de la canción
     */
    public void resumePlayer() {
        player.seekTo(position);
        player.start();
        showNotification();
    }

    /**
     * Método que resetea el Mediaplayer para cambiar de canción
     */
    private void resetPlayer() {
        player.stop();
        player.reset();
    }

    /**
     * Método que reproduce la siguiente canción
     */
    private void nextTrack() {
        if (TrackQueue.currentTrack + 1 < TrackQueue.getTrackQueue().size()) {
            resetPlayer();
            TrackQueue.currentTrack++;
            showNotification();
            playStreamTrack();
        }
    }

    /**
     * Método que reproduce la canción anterior
     */
    private void previousTrack() {
        if (TrackQueue.currentTrack - 1 >= 0) {
            resetPlayer();
            TrackQueue.currentTrack--;
            showNotification();
            playStreamTrack();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (TrackQueue.getTrackQueue().size() > 1) {//hay más de una canción en la cola
            nextTrack();
        }
        else {
            disableForegroundMode();
        }
    }
}

