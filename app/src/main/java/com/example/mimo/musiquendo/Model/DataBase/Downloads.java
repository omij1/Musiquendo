package com.example.mimo.musiquendo.Model.DataBase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

/**
 * Clase que representa la tabla Downloads de la base de datos
 */

@Entity
public class Downloads {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "path")
    private String path;

    @ColumnInfo(name="name")
    private String name;

    @ColumnInfo(name = "cover")
    private String cover;

    @ColumnInfo(name = "type")
    private String type;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
