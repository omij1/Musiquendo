package com.example.mimo.musiquendo.Model.DataBase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Interfaz que contiene los métodos para acceder a la información de las canciones descargadas
 */

@Dao
public interface DownloadsDAO {

    @Query("SELECT * FROM downloads WHERE type LIKE :searchType")
    List<Downloads> getAll(String searchType);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertDownload(Downloads download);

    @Delete
    public void deleteDownload(Downloads download);
}
