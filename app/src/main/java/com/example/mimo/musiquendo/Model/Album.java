package com.example.mimo.musiquendo.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Clase que contiene la información básica de un álbum
 */
public class Album {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("releasedate")
    private String release_date;
    @SerializedName("artist_id")
    private String artist_id;
    @SerializedName("artist_name")
    private String artist_name;
    @SerializedName("image")
    private String image;
    @SerializedName("zip")
    private String zip;

    public Album(String id, String name, String release_date, String artist_id, String artist_name, String image, String zip) {
        this.id = id;
        this.name = name;
        this.release_date = release_date;
        this.artist_id = artist_id;
        this.artist_name = artist_name;
        this.image = image;
        this.zip = zip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
