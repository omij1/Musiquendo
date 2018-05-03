package com.example.mimo.musiquendo.Player;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Singleton que permite reproducir las cancionesw de la aplicación.
 */

public class TrackPlayer {

    private static TrackPlayer trackPlayer;
    private MediaPlayer player;

    public TrackPlayer() {
    }

    public static synchronized TrackPlayer getInstance() {
        if (trackPlayer == null) {
            trackPlayer = new TrackPlayer();
        }
        return trackPlayer;
    }

    public void playStreamTrack(String url) {
        preparePlayer();
        if (player.isPlaying()) {
            resetPlayer();
        }
        startPlayer(url);
    }

    private void startPlayer(String url) {
        try {
            player.setDataSource(url);
            player.prepareAsync();// carga la canción de forma asíncrona
            player.setOnPreparedListener(mediaPlayer -> {
                player.start();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resetPlayer() {
        player.stop();
        player.reset();
    }

    private void preparePlayer() {
        if (player == null) {
            player = new MediaPlayer();
        }
    }
}
