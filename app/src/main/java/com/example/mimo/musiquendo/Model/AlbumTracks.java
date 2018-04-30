package com.example.mimo.musiquendo.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Clase que almacena los datos de las canciones de un álbum
 */

public class AlbumTracks {

    @SerializedName("id")
    private String albumId;
    @SerializedName("position")
    private int position;
    @SerializedName("name")
    private String trackName;
    @SerializedName("duration")
    private int trackDuration;
    @SerializedName("audio")
    private String audio;
    @SerializedName("audiodownload")
    private String audioDownload;

    public AlbumTracks(String albumId, int position, String trackName, int trackDuration, String audio, String audioDownload) {
        this.albumId = albumId;
        this.position = position;
        this.trackName = trackName;
        this.trackDuration = trackDuration;
        this.audio = audio;
        this.audioDownload = audioDownload;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public int getTrackDuration() {
        return trackDuration;
    }

    public void setTrackDuration(int trackDuration) {
        this.trackDuration = trackDuration;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getAudioDownload() {
        return audioDownload;
    }

    public void setAudioDownload(String audioDownload) {
        this.audioDownload = audioDownload;
    }
}
