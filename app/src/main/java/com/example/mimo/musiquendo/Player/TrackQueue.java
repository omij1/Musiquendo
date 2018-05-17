package com.example.mimo.musiquendo.Player;

import com.example.mimo.musiquendo.Model.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton que almacena la cola de canciones que van a reproducirse
 */

public class TrackQueue {

    private static TrackQueue trackQueue;
    private static List<Track> queue;
    public static String SECTION = "";
    public static int currentTrack;

    public TrackQueue() {}

    public static synchronized TrackQueue getInstance() {
        if (trackQueue == null) {
            trackQueue = new TrackQueue();
        }
        return trackQueue;
    }

    public static synchronized List<Track> getTrackQueue() {
        return queue;
    }

    /**
     * Método que añade la canción que se va a reproducir en el modo normal de los ajustes
     * @param track Canción que va a reproducirse
     */
    public void addTrack(Track track) {
        currentTrack = 0;
        if (queue == null) {
            queue = new ArrayList<>();
        }
        else {
            queue.clear();
        }
        queue.add(track);
    }

    /**
     * Método que añade la lista con las nuevas canciones a la cola cuando se está en el modo automático
     * @param list Nueva lista de canciones
     * @param position Posición de la canción que debe reproducirse en primer lugar
     */
    public void addTrackList(List<Track> list, int position) {
        currentTrack = position;
        if (queue == null) {
            queue = new ArrayList<>();
        }
        else {
            queue.clear();
        }
        queue = list;
    }

    /**
     * Método que establece el tipo de elementos que se van a reproducir
     * @param section Álbumes, artistas o listas de reproducción
     */
    public void setSection(String section) {
        if (!section.equals(SECTION)) {
            if (queue != null)
                deleteQueue();
            SECTION = section;
        }
    }

    /**
     * Borra la cola de canciones actual
     */
    public void deleteQueue() {
        queue.clear();
        queue = null;
    }
}
