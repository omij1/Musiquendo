package com.example.mimo.musiquendo.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Clase que almacena los datos de las canciones que pertenecen a un artista determinado
 */

public class ArtistTracks {

    @SerializedName("album_id")
    private String album_id;
    @SerializedName("album_name")
    private String album_name;
    @SerializedName("id")
    private String trackId;
    @SerializedName("name")
    private String trackName;
    @SerializedName("duration")
    private int trackDuration;
    @SerializedName("releasedate")
    private String releaseDate;
    @SerializedName("album_image")
    private String album_image;
    @SerializedName("audio")
    private String audio;


    public ArtistTracks(String album_id, String album_name, String trackId, String trackName, int trackDuration, String releaseDate, String album_image, String audio) {
        this.album_id = album_id;
        this.album_name = album_name;
        this.trackId = trackId;
        this.trackName = trackName;
        this.trackDuration = trackDuration;
        this.releaseDate = releaseDate;
        this.album_image = album_image;
        this.audio = audio;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getAlbum_image() {
        return album_image;
    }

    public void setAlbum_image(String album_image) {
        this.album_image = album_image;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
}
