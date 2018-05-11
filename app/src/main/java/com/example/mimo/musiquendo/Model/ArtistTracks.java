package com.example.mimo.musiquendo.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Clase que almacena los datos de las canciones que pertenecen a un artista determinado
 */

public class ArtistTracks {

    @SerializedName("albumId")
    private String albumId;
    @SerializedName("albumName")
    private String albumName;
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
    private String minutes;


    public ArtistTracks(String albumId, String albumName, String trackId, String trackName, int trackDuration, String releaseDate, String album_image, String audio, String minutes) {
        this.albumId = albumId;
        this.albumName = albumName;
        this.trackId = trackId;
        this.trackName = trackName;
        this.trackDuration = trackDuration;
        this.releaseDate = releaseDate;
        this.album_image = album_image;
        this.audio = audio;
        this.minutes = minutes;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
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

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }
}
