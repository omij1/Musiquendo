package com.example.mimo.musiquendo.Player;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Singleton que permite reproducir las cancionesw de la aplicación.
 */

public class TrackPlayer {

    private static TrackPlayer trackPlayer;
    private static MediaPlayer player;
    private int position;

    public TrackPlayer() {
    }

    public static synchronized TrackPlayer getInstance() {
        if (trackPlayer == null) {
            trackPlayer = new TrackPlayer();
        }
        return trackPlayer;
    }

    public static synchronized MediaPlayer getPlayerInstance() {
        if (player == null) {
            player = new MediaPlayer();
        }
        return player;
    }

    /**
     * Método que carga el Mediaplayer
     * @param url Dirección de la canción
     * @param trackDuration Duración de la canción
     */
    public void playStreamTrack(String url, int trackDuration) {
        getPlayerInstance();
        if (player.isPlaying()) {
            resetPlayer();
        }
        startPlayer(url);
    }

    /**
     * Método que inicia la reproducción de la canción
     * @param url Ruta de donde se hace el streaming
     */
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
