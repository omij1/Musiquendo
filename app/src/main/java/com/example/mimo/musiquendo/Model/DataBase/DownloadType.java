package com.example.mimo.musiquendo.Model.DataBase;

public enum DownloadType {

    ALBUM("Album"),
    ARTIST("Artist"),
    PLAYLIST("Playlist"),
    SONG("Song");

    public final String type;

    DownloadType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
