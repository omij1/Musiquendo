package com.mimo.musiquendo.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Clase que almacena los datos de las canciones pertenecientes a una lista de reproducción
 */

public class PlayListTracks {

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
    @SerializedName("image")
    private String image;
    @SerializedName("artist_name")
    private String artistName;
    private String minutes;

    public PlayListTracks(String albumId, int position, String trackName, int trackDuration, String audio, String audioDownload, String image, String artistName, String minutes) {
        this.albumId = albumId;
        this.position = position;
        this.trackName = trackName;
        this.trackDuration = trackDuration;
        this.audio = audio;
        this.audioDownload = audioDownload;
        this.image = image;
        this.artistName = artistName;
        this.minutes = minutes;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }
}
