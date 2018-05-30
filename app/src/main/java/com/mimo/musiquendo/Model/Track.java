package com.mimo.musiquendo.Model;

/**
 * Clase modelo que almacena los datos necesarios para presentar una canción en la notificación superior
 */

public class Track {

    private String streamUrl;
    private String trackName;
    private String info;
    private int duration;
    private String durationInfo;

    public Track(String streamUrl, String trackName, String info, int duration, String durationInfo) {
        this.streamUrl = streamUrl;
        this.trackName = trackName;
        this.info = info;
        this.duration = duration;
        this.durationInfo = durationInfo;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDurationInfo() {
        return durationInfo;
    }

    public void setDurationInfo(String durationInfo) {
        this.durationInfo = durationInfo;
    }
}
