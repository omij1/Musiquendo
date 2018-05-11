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

    public void addTrack(Track track) {
        currentTrack = 0;
        if (queue == null) {
            queue = new ArrayList<>();
        }
        else {
            queue.remove(0);
        }
        queue.add(track);
    }

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

    public void setSection(String section) {
        if (!section.equals(SECTION)) {
            if (queue != null)
                deleteQueue();
            SECTION = section;
        }
    }

    public void deleteQueue() {
        queue.clear();
        queue = null;
    }
}