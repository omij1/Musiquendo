package com.example.mimo.musiquendo.Model.DataBase;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Clase que representa la base de datos de la aplicación
 */

@Database(entities = {DownloadItem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract DownloadsDAO downloadsDAO();
    private static AppDatabase appDatabase;
    private static final String DB_NAME = "downloads";


    public static synchronized AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).build();
        }
        return appDatabase;
    }
}
