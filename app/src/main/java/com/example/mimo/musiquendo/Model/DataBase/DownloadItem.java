package com.example.mimo.musiquendo.Model.DataBase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

/**
 * Clase que representa la tabla DownloadItem de la base de datos
 */

@Entity
public class DownloadItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "path")
    private String path;

    @ColumnInfo(name="trackName")
    private String trackName;

    @ColumnInfo(name = "parentName")
    private String parentName;

    @ColumnInfo(name = "duration")
    private int duration;

    @ColumnInfo(name = "minutes")
    private String minutes;

    @ColumnInfo(name = "cover")
    private String cover;

    public DownloadItem(String path, String trackName, String parentName, int duration, String minutes, String cover) {
        this.path = path;
        this.trackName = trackName;
        this.parentName = parentName;
        this.duration = duration;
        this.minutes = minutes;
        this.cover = cover;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
