package com.mimo.musiquendo.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Clase que contiene la información básica de un artista
 */

public class Artist {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("website")
    private String website;
    @SerializedName("joindate")
    private String joindate;
    @SerializedName("image")
    private String image;

    public Artist(String id, String name, String website, String joindate, String image) {
        this.id = id;
        this.name = name;
        this.website = website;
        this.joindate = joindate;
        this.image = image;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getJoindate() {
        return joindate;
    }

    public void setJoindate(String joindate) {
        this.joindate = joindate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
