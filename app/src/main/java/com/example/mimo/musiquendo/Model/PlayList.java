package com.example.mimo.musiquendo.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Clase que contiene la información básica de una lista de reproducción
 */

public class PlayList {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("creationdate")
    private String creation;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("user_name")
    private String user_name;
    @SerializedName("zip")
    private String zip;

    public PlayList(String id, String name, String creation, String user_id, String user_name, String zip) {
        this.id = id;
        this.name = name;
        this.creation = creation;
        this.user_id = user_id;
        this.user_name = user_name;
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

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
