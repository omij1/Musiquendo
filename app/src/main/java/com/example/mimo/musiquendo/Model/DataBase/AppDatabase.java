package com.example.mimo.musiquendo.Model.DataBase;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Clase que representa la base de datos de la aplicación
 */

@Database(entities = {Downloads.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract DownloadsDAO downloadsDAO();
    private static AppDatabase appDatabase;


}
